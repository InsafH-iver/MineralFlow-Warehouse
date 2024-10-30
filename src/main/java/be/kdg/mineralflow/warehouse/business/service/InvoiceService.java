package be.kdg.mineralflow.warehouse.business.service;

import be.kdg.mineralflow.warehouse.business.domain.Invoice;
import be.kdg.mineralflow.warehouse.business.domain.InvoiceLine;
import be.kdg.mineralflow.warehouse.business.domain.Vendor;
import be.kdg.mineralflow.warehouse.business.domain.Warehouse;
import be.kdg.mineralflow.warehouse.exception.NoItemFoundException;
import be.kdg.mineralflow.warehouse.persistence.InvoiceRepository;
import be.kdg.mineralflow.warehouse.persistence.VendorRepository;
import be.kdg.mineralflow.warehouse.persistence.WarehouseRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class InvoiceService {
    public static final Logger logger = Logger
            .getLogger(InvoiceService.class.getName());
    private final WarehouseRepository warehouseRepository;
    private final InvoiceRepository invoiceRepository;
    private final VendorRepository vendorRepository;

    public InvoiceService(WarehouseRepository warehouseRepository, InvoiceRepository invoiceRepository, VendorRepository vendorRepository) {
        this.warehouseRepository = warehouseRepository;
        this.invoiceRepository = invoiceRepository;
        this.vendorRepository = vendorRepository;
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void createInvoices(){
        logger.info("InvoiceService: createInvoices has been called");
        LocalDateTime now = LocalDateTime.now();

        List<Warehouse> warehouses = warehouseRepository.findAll();
        Map<Vendor, List<Warehouse>> warehousesPerVendor = warehouses.stream()
                .collect(Collectors.groupingBy(
                        Warehouse::getVendor)
                );
        warehousesPerVendor.forEach((v,warehouseList) ->
                invoiceRepository.save(
                        new Invoice(
                                now,
                                v,
                                warehouseList.stream()
                                        .flatMap(warehouse -> warehouse.getStockPortions().stream()
                                                .map(stockPortion -> new InvoiceLine(warehouse.getResource(), stockPortion))
                                        )
                                        .collect(Collectors.toList())
                        )));
    }

    public Invoice getInvoice(String vendorName, LocalDate date) {
        Optional<Vendor> optionalVendor = vendorRepository.findByName(vendorName);
        if (optionalVendor.isEmpty()) {
            String messageException = String.format("No vendor was found for vendor %s", vendorName);
            logger.severe(messageException);
            throw new NoItemFoundException(messageException);
        }
        Vendor vendor = optionalVendor.get();

        Optional<Invoice> optionalInvoice = invoiceRepository.getInvoiceByVendorAndDate(vendor,date);
        if (optionalInvoice.isEmpty()) {
            String messageException = String.format("No invoice was found for vendor %s and date %s", vendor.getName(), date);
            logger.severe(messageException);
            throw new NoItemFoundException(messageException);
        }
        return optionalInvoice.get();
    }
}

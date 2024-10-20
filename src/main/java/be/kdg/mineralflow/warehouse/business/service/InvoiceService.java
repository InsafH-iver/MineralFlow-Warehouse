package be.kdg.mineralflow.warehouse.business.service;

import be.kdg.mineralflow.warehouse.business.domain.Invoice;
import be.kdg.mineralflow.warehouse.business.domain.Vendor;
import be.kdg.mineralflow.warehouse.business.domain.Warehouse;
import be.kdg.mineralflow.warehouse.persistence.InvoiceRepository;
import be.kdg.mineralflow.warehouse.persistence.WarehouseRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class InvoiceService {
    public static final Logger logger = Logger
            .getLogger(InvoiceService.class.getName());
    private final WarehouseRepository warehouseRepository;
    private final InvoiceRepository invoiceRepository;

    public InvoiceService(WarehouseRepository warehouseRepository, InvoiceRepository invoiceRepository) {
        this.warehouseRepository = warehouseRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void createInvoices(){
        logger.info("InvoiceService: createInvoices has been called");
        ZonedDateTime now = ZonedDateTime.now();
        List<Warehouse> warehouses = warehouseRepository.findAll();
        Map<Vendor, List<Warehouse>> warehousesPerVendor = warehouses.stream()
                .collect(Collectors.groupingBy(
                        Warehouse::getVendor)
                );
        warehousesPerVendor.values().forEach(w -> invoiceRepository.save(new Invoice(now,w)));
    }
}

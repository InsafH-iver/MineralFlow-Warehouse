package be.kdg.mineralflow.warehouse.business.service;

import be.kdg.mineralflow.warehouse.business.domain.*;
import be.kdg.mineralflow.warehouse.business.util.mapper.InvoiceMapper;
import be.kdg.mineralflow.warehouse.exception.NoItemFoundException;
import be.kdg.mineralflow.warehouse.persistence.InvoiceRepository;
import be.kdg.mineralflow.warehouse.persistence.VendorRepository;
import be.kdg.mineralflow.warehouse.persistence.WarehouseRepository;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.InvoiceDto;
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
    private final InvoiceGeneratingService invoiceGeneratingService;
    private final InvoiceMapper invoiceMapper = InvoiceMapper.INSTANCE;

    public InvoiceService(WarehouseRepository warehouseRepository, InvoiceRepository invoiceRepository, VendorRepository vendorRepository, InvoiceGeneratingService invoiceGeneratingService) {
        this.warehouseRepository = warehouseRepository;
        this.invoiceRepository = invoiceRepository;
        this.vendorRepository = vendorRepository;
        this.invoiceGeneratingService = invoiceGeneratingService;
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void createInvoices(){
        logger.info("InvoiceService: createInvoices has been called");
        LocalDateTime now = LocalDateTime.now();
        List<Warehouse> allWarehouses = warehouseRepository.findAll();
        Map<Vendor, List<Warehouse>> warehousesPerVendor = allWarehouses.stream()
                .collect(Collectors.groupingBy(
                        Warehouse::getVendor)
                );
        warehousesPerVendor.forEach((vendor,warehouseList) ->
                invoiceRepository.save(
                        createInvoice(now,vendor,warehouseList)
                )
        );
        logger.info("InvoiceService: Invoices have been created");
    }
    private Invoice createInvoice(LocalDateTime invoiceDate,Vendor vendor, List<Warehouse> warehouses){
        return new Invoice(
                invoiceDate,
                vendor,
                createInvoiceLinesFromWarehouses(warehouses)
        );
    }
    private List<InvoiceLine> createInvoiceLinesFromWarehouses(List<Warehouse> warehouses){
        return warehouses.stream()
                .flatMap(warehouse -> warehouse.getStockPortions().stream()
                        .map(stockPortion -> createInvoiceLine(warehouse.getResource(),stockPortion)))
                .collect(Collectors.toList());
    }
    private InvoiceLine createInvoiceLine(Resource resource, StockPortion stockPortion){
        return new InvoiceLine(resource, stockPortion);
    }

    public InvoiceDto getInvoice(String vendorName, LocalDate date) {
        logger.info(String.format("InvoiceService: invoice with vendor %s and date %s was requested",vendorName, date));
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
        InvoiceDto invoiceDto = invoiceMapper.invoiceToInvoiceDto(optionalInvoice.get());
        invoiceGeneratingService.generateInvoicePdf(invoiceDto);
        logger.info(String.format("InvoiceService: invoice with vendor %s and date %s was retrieved",vendorName, date));
        return invoiceDto;
    }
}

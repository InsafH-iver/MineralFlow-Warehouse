package be.kdg.mineralflow.warehouse.business.service;

import be.kdg.mineralflow.warehouse.business.domain.*;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.InvoiceDto;
import be.kdg.mineralflow.warehouse.presentation.controller.mapper.InvoiceMapper;
import be.kdg.mineralflow.warehouse.persistence.WarehouseRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class InvoiceService {
    public static final Logger logger = Logger
            .getLogger(InvoiceService.class.getName());
    private final WarehouseRepository warehouseRepository;
    private final InvoiceGeneratingService invoiceGeneratingService;
    private final InvoiceMapper invoiceMapper = InvoiceMapper.INSTANCE;

    public InvoiceService(WarehouseRepository warehouseRepository, InvoiceGeneratingService invoiceGeneratingService) {
        this.warehouseRepository = warehouseRepository;
        this.invoiceGeneratingService = invoiceGeneratingService;
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void createInvoices() {
        logger.info("InvoiceService: createInvoices has been called");
        LocalDateTime now = LocalDateTime.now();
        List<Warehouse> allWarehouses = warehouseRepository.findAll();
        Map<Vendor, List<Warehouse>> warehousesPerVendor = allWarehouses.stream()
                .collect(Collectors.groupingBy(
                        Warehouse::getVendor)
                );
        warehousesPerVendor.forEach(
                (vendor, warehouseList) ->
                {
                    Invoice invoice = createInvoice(now, vendor, warehouseList);
                    saveInvoicePdf(vendor, invoice);
                }
        );
        logger.info("InvoiceService: Invoices have been created");
    }

    private Invoice createInvoice(LocalDateTime invoiceDate, Vendor vendor, List<Warehouse> warehouses) {
        return new Invoice(
                invoiceDate,
                vendor,
                createInvoiceLinesFromWarehouses(warehouses)
        );
    }

    private List<InvoiceLine> createInvoiceLinesFromWarehouses(List<Warehouse> warehouses) {
        return warehouses.stream()
                .flatMap(warehouse -> warehouse.getStockPortions().stream()
                        .map(stockPortion -> createInvoiceLine(warehouse.getResource(), stockPortion)))
                .collect(Collectors.toList());
    }

    private InvoiceLine createInvoiceLine(Resource resource, StockPortion stockPortion) {
        return new InvoiceLine(resource, stockPortion);
    }

    private void saveInvoicePdf(Vendor vendor, Invoice invoice) {
        InvoiceDto invoiceDto = invoiceMapper.invoiceToInvoiceDto(invoice);
        invoiceGeneratingService.generateInvoicePdf(invoiceDto);
        logger.info(String.format("InvoiceService: invoice with vendor %s and date %s was retrieved", vendor.getName(), invoice.getCreationDate()));
    }
}

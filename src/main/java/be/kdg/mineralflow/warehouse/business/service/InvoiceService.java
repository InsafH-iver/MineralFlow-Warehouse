package be.kdg.mineralflow.warehouse.business.service;

import be.kdg.mineralflow.warehouse.business.domain.Vendor;
import be.kdg.mineralflow.warehouse.business.domain.Warehouse;
import be.kdg.mineralflow.warehouse.business.util.invoice.Invoice;
import be.kdg.mineralflow.warehouse.business.util.invoice.InvoiceFactory;
import be.kdg.mineralflow.warehouse.persistence.WarehouseRepository;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.InvoiceDto;
import be.kdg.mineralflow.warehouse.presentation.controller.mapper.InvoiceMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
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
    private final InvoiceGeneratingService invoiceGeneratingService;
    private final InvoiceMapper invoiceMapper;
    private final InvoiceFactory invoiceFactory;

    public InvoiceService(WarehouseRepository warehouseRepository,
                          InvoiceGeneratingService invoiceGeneratingService, InvoiceMapper invoiceMapper, InvoiceFactory invoiceFactory) {
        this.warehouseRepository = warehouseRepository;
        this.invoiceGeneratingService = invoiceGeneratingService;
        this.invoiceMapper = invoiceMapper;
        this.invoiceFactory = invoiceFactory;
    }

    @Scheduled(cron = "0 0 9 * * *")
    @Transactional
    public void createInvoices() {
        logger.info("InvoiceService: createInvoices has been called");
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        List<Warehouse> allWarehouses = warehouseRepository.findAll();
        Map<Vendor, List<Warehouse>> warehousesPerVendor = allWarehouses.stream()
                .collect(Collectors.groupingBy(
                        Warehouse::getVendor)
                );
        warehousesPerVendor.forEach(
                (vendor, warehouseList) ->
                {
                    Invoice invoice = invoiceFactory.createInvoice(now.toLocalDateTime(), vendor, warehouseList);
                    saveInvoicePdf(vendor, invoice);
                }
        );
        logger.info("InvoiceService: Invoices have been created");
    }

    private void saveInvoicePdf(Vendor vendor, Invoice invoice) {
        InvoiceDto invoiceDto = invoiceMapper.mapInvoiceToInvoiceDto(vendor, invoice);
        invoiceGeneratingService.generateInvoicePdf(invoiceDto);
        logger.info(String.format("InvoiceService: invoice with vendor %s and date %s was retrieved", vendor.getName(), invoice.getCreationDate()));
    }

}

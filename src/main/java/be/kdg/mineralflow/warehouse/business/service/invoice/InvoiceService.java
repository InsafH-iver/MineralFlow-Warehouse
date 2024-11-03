package be.kdg.mineralflow.warehouse.business.service.invoice;

import be.kdg.mineralflow.warehouse.business.domain.Commission;
import be.kdg.mineralflow.warehouse.business.domain.Vendor;
import be.kdg.mineralflow.warehouse.business.domain.Warehouse;
import be.kdg.mineralflow.warehouse.business.domain.Invoice;
import be.kdg.mineralflow.warehouse.business.service.pdf.InvoiceGeneratingService;
import be.kdg.mineralflow.warehouse.business.util.ExceptionHandlingHelper;
import be.kdg.mineralflow.warehouse.business.util.invoice.InvoiceFactory;
import be.kdg.mineralflow.warehouse.persistence.CommissionRepository;
import be.kdg.mineralflow.warehouse.persistence.InvoiceRepository;
import be.kdg.mineralflow.warehouse.persistence.WarehouseRepository;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.invoice.InvoiceDto;
import be.kdg.mineralflow.warehouse.presentation.controller.mapper.InvoiceMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
    private final InvoiceRepository invoiceRepository;
    private final CommissionService commissionService;
    private final CommissionRepository commissionRepository;

    public InvoiceService(WarehouseRepository warehouseRepository,
                          InvoiceGeneratingService invoiceGeneratingService, InvoiceMapper invoiceMapper, InvoiceFactory invoiceFactory, InvoiceRepository invoiceRepository, CommissionRepository commissionRepository) {
        this.warehouseRepository = warehouseRepository;
        this.invoiceGeneratingService = invoiceGeneratingService;
        this.invoiceMapper = invoiceMapper;
        this.invoiceFactory = invoiceFactory;
        this.invoiceRepository = invoiceRepository;
        this.commissionRepository = commissionRepository;
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
                    invoiceRepository.save(invoice);
                    List<Commission> commissions = getCommissions(vendor.getId(),now.toLocalDate().minusDays(1));
                    saveInvoicePdf(invoice,commissions);
                }
        );
        logger.info("Invoices have been created");
    }

    private List<Commission> getCommissions(UUID vendorId, LocalDate localDate) {
        return commissionRepository.findCommissionsByCreationDateAndPurchaseOrder_Vendor_Id(localDate,vendorId);
    }

    private void saveInvoicePdf(Invoice invoice, List<Commission> commissions) {
        InvoiceDto invoiceDto = invoiceMapper.mapInvoiceToInvoiceDto(invoice,commissions);
        invoiceGeneratingService.generateInvoicePdf(invoiceDto);
        logger.info(String.format("Invoice with vendor %s and date %s was saved to pdf", invoice.getVendor().getName(), invoice.getCreationDate()));
    }

    public InvoiceDto getInvoiceAndCommission(UUID vendorId, LocalDate date) {
        Invoice invoice = getInvoice(vendorId,date);
        List<Commission> commissions = getCommissions(vendorId,date.minusDays(1));
        return invoiceMapper.mapInvoiceToInvoiceDto(invoice,commissions);
    }
    private Invoice getInvoice(UUID vendorId, LocalDate date){
        return invoiceRepository.getInvoiceByVendorIdAndCreationDate(vendorId,date)
                .orElseThrow(()-> ExceptionHandlingHelper.logAndThrowNotFound("No Invoice was found for vendorId '%s' and date '%s'",vendorId,date));
    }
}

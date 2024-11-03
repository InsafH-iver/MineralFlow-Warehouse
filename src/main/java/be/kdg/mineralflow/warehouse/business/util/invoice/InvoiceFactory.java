package be.kdg.mineralflow.warehouse.business.util.invoice;

import be.kdg.mineralflow.warehouse.business.domain.*;
import be.kdg.mineralflow.warehouse.business.service.CommissionService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class InvoiceFactory {
    private final InvoiceLineFactory invoiceLineFactory;
    private final CommissionService commissionService;

    public InvoiceFactory(InvoiceLineFactory invoiceLineFactory, CommissionService commissionService) {
        this.invoiceLineFactory = invoiceLineFactory;
        this.commissionService = commissionService;
    }

    public Invoice createInvoice(LocalDateTime invoiceDate, Vendor vendor, List<Warehouse> warehouses) {
        List<InvoiceLine> invoiceLines = invoiceLineFactory.createInvoiceLinesFromWarehouses(warehouses);
        return new Invoice(invoiceDate, vendor, invoiceLines);
    }
}

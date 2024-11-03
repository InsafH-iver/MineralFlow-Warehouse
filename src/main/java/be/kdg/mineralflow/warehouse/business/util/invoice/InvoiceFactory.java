package be.kdg.mineralflow.warehouse.business.util.invoice;

import be.kdg.mineralflow.warehouse.business.domain.Vendor;
import be.kdg.mineralflow.warehouse.business.domain.Warehouse;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.invoice.InvoiceDto;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.invoice.InvoiceLineDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class InvoiceFactory {
    private final InvoiceLineFactory invoiceLineFactory;

    public InvoiceFactory(InvoiceLineFactory invoiceLineFactory) {
        this.invoiceLineFactory = invoiceLineFactory;
    }

    public Invoice createInvoice(LocalDateTime invoiceDate, Vendor vendor, List<Warehouse> warehouses) {
        List<InvoiceLine> invoiceLines = invoiceLineFactory.createInvoiceLinesFromWarehouses(warehouses);
        return new Invoice(invoiceDate, vendor, invoiceLines);
    }

    public InvoiceDto createInvoiceDto(Vendor vendor, Invoice invoice) {
        List<InvoiceLineDto> invoiceLines =
                invoiceLineFactory.toInvoiceLineDtos(
                        invoice.getInvoiceLines(),
                        invoice.getCreationDate());

        double totalStorageCost = invoiceLines.stream().mapToDouble(InvoiceLineDto::getStorageCost).sum();
        return new InvoiceDto(vendor.getName(), invoiceLines, invoice.getCreationDate(), totalStorageCost);
    }
}

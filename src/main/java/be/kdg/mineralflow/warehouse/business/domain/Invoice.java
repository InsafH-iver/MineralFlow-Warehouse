package be.kdg.mineralflow.warehouse.business.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public class Invoice {
    private LocalDateTime creationDate;
    private Vendor vendor;
    private List<InvoiceLine> invoiceLines;

    protected Invoice() {
    }

    public Invoice(LocalDateTime creationDate, Vendor vendor, List<InvoiceLine> invoiceLines) {
        this.creationDate = creationDate;
        this.vendor = vendor;
        this.invoiceLines = invoiceLines;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime created) {
        this.creationDate = created;
    }

    public List<InvoiceLine> getInvoiceLines() {
        return invoiceLines;
    }

    public void setInvoiceLines(List<InvoiceLine> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }
    public double getTotalStorageCost(){
        if (invoiceLines == null) return 0;
        return invoiceLines.stream()
                .mapToDouble(invoiceLine -> invoiceLine
                        .getStorageCost(creationDate))
                .sum();
    }
}

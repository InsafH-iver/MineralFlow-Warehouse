package be.kdg.mineralflow.warehouse.presentation.controller.dto;

import java.time.LocalDate;

public class InvoiceRequestFormDto {
    private int vendorName;
    private LocalDate invoiceDate;

    public int getVendorName() {
        return vendorName;
    }

    public void setVendorName(int vendorName) {
        this.vendorName = vendorName;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }
}

package be.kdg.mineralflow.warehouse.presentation.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

public class InvoiceDto {
    private String vendorName;
    private List<InvoiceLineDto> invoiceLines;
    private LocalDateTime creationDate;
    private double totalStorageCost;

    public InvoiceDto(String vendorName, List<InvoiceLineDto> invoiceLines, LocalDateTime creationDate, double totalStorageCost) {
        this.vendorName = vendorName;
        this.invoiceLines = invoiceLines;
        this.creationDate = creationDate;
        this.totalStorageCost = totalStorageCost;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public double getTotalStorageCost() {
        return totalStorageCost;
    }

    public void setTotalStorageCost(double totalStorageCost) {
        this.totalStorageCost = totalStorageCost;
    }

    public List<InvoiceLineDto> getInvoiceLines() {
        return invoiceLines;
    }

    public void setInvoiceLines(List<InvoiceLineDto> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }
}

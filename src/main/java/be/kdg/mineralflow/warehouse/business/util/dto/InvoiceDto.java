package be.kdg.mineralflow.warehouse.business.util.dto;

import java.time.LocalDateTime;
import java.util.List;

public class InvoiceDto {
    private String vendorName;
    private List<InvoiceLineDto> invoiceLines;
    private LocalDateTime creationDate;
    private double totalStorageCost;

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

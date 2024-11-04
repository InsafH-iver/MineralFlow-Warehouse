package be.kdg.mineralflow.warehouse.business.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private LocalDateTime creationDate;
    @ManyToOne
    private Vendor vendor;
    @OneToMany(cascade = CascadeType.ALL)
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

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}

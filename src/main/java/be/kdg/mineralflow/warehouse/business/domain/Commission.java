package be.kdg.mineralflow.warehouse.business.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Commission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @OneToOne
    private PurchaseOrder purchaseOrder;
    private LocalDateTime creationDate;
    private double commisionPrice;
    @ManyToOne
    private Invoice invoice;

    public Commission() {
    }

    public Commission(PurchaseOrder purchaseOrder, double commisionPrice) {
        this.purchaseOrder = purchaseOrder;
        this.commisionPrice = commisionPrice;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getCommisionPrice() {
        return commisionPrice;
    }

    public void setCommisionPrice(double commisionPrice) {
        this.commisionPrice = commisionPrice;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}

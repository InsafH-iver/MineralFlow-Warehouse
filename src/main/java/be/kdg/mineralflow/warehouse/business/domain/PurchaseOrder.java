package be.kdg.mineralflow.warehouse.business.domain;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(unique = true)
    private String purchaseOrderNumber;
    private String vesselNumber;
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderLine> orderLines;
    @ManyToOne
    private Vendor vendor;
    @ManyToOne
    private Buyer buyer;

    public PurchaseOrder() {
    }

    public PurchaseOrder(String poNumber, String vesselNumber, Status status, List<OrderLine> orderLines, Vendor vendor, Buyer buyer) {
        this.poNumber = poNumber;
        this.vesselNumber = vesselNumber;
        this.status = status;
        this.orderLines = orderLines;
        this.vendor = vendor;
        this.buyer = buyer;
    }

    public PurchaseOrder() {
    }

    public PurchaseOrder(UUID id, List<OrderLine> orderLines, String purchaseOrderNumber, Status status, Vendor vendor, String vesselNumber) {
        this.id = id;
        this.orderLines = orderLines;
        this.purchaseOrderNumber = purchaseOrderNumber;
        this.status = status;
        this.vendor = vendor;
        this.vesselNumber = vesselNumber;
    }
    public UUID getId() {
        return id;
    }
    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    public void setPoNumber(String poNumber) {
        this.purchaseOrderNumber = poNumber;
    }

    public String getPoNumber() {
        return purchaseOrderNumber;
    }

    public String getVesselNumber() {
        return vesselNumber;
    }

    public void setVesselNumber(String vesselNumber) {
        this.vesselNumber = vesselNumber;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    public void updateStatusBasedOnFulfillment(boolean hasAllOrderLinesFulFilled) {
        if (hasAllOrderLinesFulFilled) {
            this.status = Status.COMPLETED;
        } else {
            this.status = Status.WAITING;
        }
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public Status getStatus() {
        return status;
    }

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public boolean isNotWaiting() {
        return status != Status.WAITING;
    }

    public boolean areAllPurchaseOrderLinesFulfilled() {
        return orderLines.stream()
                .allMatch(OrderLine::hasBeenCompleted);
    }
}

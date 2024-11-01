package be.kdg.mineralflow.warehouse.business.domain;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String poNumber;
    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderLine> orderLines;

    @ManyToOne
    private Vendor vendor;
    @ManyToOne
    private Buyer buyer;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
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
        this.poNumber = poNumber;
    }

    public String getPoNumber() {
        return poNumber;
    }
}

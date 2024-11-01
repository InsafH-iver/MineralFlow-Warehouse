package be.kdg.mineralflow.warehouse.business.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.util.UUID;

@Entity
public class OrderLine {
    @Id
    private UUID id;
    private double amountInTon;
    private double sellingPricePerTon;
    @ManyToOne
    private Resource resource;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public double getSellingPricePerTon() {
        return sellingPricePerTon;
    }

    public void setSellingPricePerTon(double sellingPricePerTon) {
        this.sellingPricePerTon = sellingPricePerTon;
    }

    public double getAmountInTon() {
        return amountInTon;
    }

    public void setAmountInTon(double amountInTon) {
        this.amountInTon = amountInTon;
    }
}

package be.kdg.mineralflow.warehouse.business.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class OrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private double amountInTon;
    private double sellingPricePerTon;
    private boolean hasBeenCompleted;
    @ManyToOne
    private Resource resource;

    public OrderLine() {
    }

    public OrderLine(double amountInTon, double sellingPricePerTon, Resource resource) {
        this.amountInTon = amountInTon;
        this.sellingPricePerTon = sellingPricePerTon;
        this.resource = resource;
    }

    public OrderLine(double amountInTon, boolean hasBeenCompleted, UUID id, double sellingPricePerTon, Resource resource) {
        this.amountInTon = amountInTon;
        this.hasBeenCompleted = hasBeenCompleted;
        this.id = id;
        this.sellingPricePerTon = sellingPricePerTon;
        this.resource = resource;
    }

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

    public boolean hasBeenCompleted() {
        return hasBeenCompleted;
    }

    public void markAsCompleted() {
        this.hasBeenCompleted = true;
    }
}

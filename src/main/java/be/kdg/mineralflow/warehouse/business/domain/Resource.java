package be.kdg.mineralflow.warehouse.business.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private double storagePricePerTonPerDay;
    private double sellingPricePerTon;
    private String description;

    protected Resource() {
    }


    public Resource(UUID id, String description, String name, double sellingPricePerTon, double storagePricePerTonPerDay) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.sellingPricePerTon = sellingPricePerTon;
        this.storagePricePerTonPerDay = storagePricePerTonPerDay;
    }

    public Resource(String description, String name, double sellingPricePerTon, double storagePricePerTonPerDay) {
        this.name = name;
        this.storagePricePerTonPerDay = storagePricePerTonPerDay;
        this.sellingPricePerTon = sellingPricePerTon;
        this.description = description;
    }

    public double getStoragePricePerTonPerDay() {
        return storagePricePerTonPerDay;
    }
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getSellingPricePerTon() {
        return sellingPricePerTon;
    }

    public String getDescription() {
        return description;
    }
}

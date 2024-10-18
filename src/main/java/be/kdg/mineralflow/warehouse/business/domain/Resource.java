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

    public double getStoragePricePerTonPerDay() {
        return storagePricePerTonPerDay;
    }

    public Resource(UUID id, String description, String name, double sellingPricePerTon, double storagePricePerTonPerDay) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.sellingPricePerTon = sellingPricePerTon;
        this.storagePricePerTonPerDay = storagePricePerTonPerDay;
    }
}

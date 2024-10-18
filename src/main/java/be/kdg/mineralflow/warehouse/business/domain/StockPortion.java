package be.kdg.mineralflow.warehouse.business.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
public class StockPortion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private ZonedDateTime arrivalTime;
    private double amountInTon;
    private double storageCostPerTonPerDay;

    protected StockPortion() {
    }

    public StockPortion(double amountInTon, ZonedDateTime deliveryTime, double storageCostPerTonPerDay) {
        this.amountInTon = amountInTon;
        this.arrivalTime = deliveryTime;
        this.storageCostPerTonPerDay = storageCostPerTonPerDay;
    }

    public double getAmountInTon() {
        return amountInTon;
    }

    public ZonedDateTime getArrivalTime() {
        return arrivalTime;
    }

    public double getStorageCostPerTonPerDay() {
        return storageCostPerTonPerDay;
    }
}

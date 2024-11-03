package be.kdg.mineralflow.warehouse.business.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity
public class InvoiceLine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @ManyToOne
    private Resource resource;
    private ZonedDateTime arrivalTime;
    private double amountInTon;
    private double storageCostPerTonPerDay;
    protected InvoiceLine() {
    }

    public InvoiceLine(Resource resource, ZonedDateTime arrivalTime, double amountInTon, double storageCostPerTonPerDay) {
        this.resource = resource;
        this.arrivalTime = arrivalTime;
        this.amountInTon = amountInTon;
        this.storageCostPerTonPerDay = storageCostPerTonPerDay;
    }
    public Resource getResource() {
        return resource;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public ZonedDateTime getArrivalTime() {
        return arrivalTime;
    }

    public double getAmountInTon() {
        return amountInTon;
    }

    public double getStorageCostPerTonPerDay() {
        return storageCostPerTonPerDay;
    }

    public long getDaysInStorage(LocalDate creationDate) {
        return ChronoUnit.DAYS.between(arrivalTime.toLocalDate(),creationDate);
    }
}

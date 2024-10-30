package be.kdg.mineralflow.warehouse.presentation.controller.dto;

import java.time.LocalDateTime;

public class InvoiceLineDto {
    private LocalDateTime arrivalTime;
    private String resource;
    private double weightInTon;
    private double storageCostPerDayPerTon;
    private int days;
    private double storageCost;

    public InvoiceLineDto(LocalDateTime arrivalTime, String resource, double weightInTon, double storageCostPerDayPerTon, int days, double storageCost) {
        this.arrivalTime = arrivalTime;
        this.resource = resource;
        this.weightInTon = weightInTon;
        this.storageCostPerDayPerTon = storageCostPerDayPerTon;
        this.days = days;
        this.storageCost = storageCost;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public double getWeightInTon() {
        return weightInTon;
    }

    public void setWeightInTon(double weightInTon) {
        this.weightInTon = weightInTon;
    }

    public double getStorageCostPerDayPerTon() {
        return storageCostPerDayPerTon;
    }

    public void setStorageCostPerDayPerTon(double storageCostPerDayPerTon) {
        this.storageCostPerDayPerTon = storageCostPerDayPerTon;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public double getStorageCost() {
        return storageCost;
    }

    public void setStorageCost(double storageCost) {
        this.storageCost = storageCost;
    }
}

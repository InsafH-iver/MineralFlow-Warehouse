package be.kdg.mineralflow.warehouse.business.util.storageCost;

import be.kdg.mineralflow.warehouse.business.domain.InvoiceLine;

import java.time.LocalDate;

public interface StorageCostCalculator {
    double calculateStorageCost(InvoiceLine invoiceLine, LocalDate time);
}

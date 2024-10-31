package be.kdg.mineralflow.warehouse.business.util.storageCost;

import be.kdg.mineralflow.warehouse.business.domain.StockPortion;

import java.time.LocalDateTime;

public interface StorageCostCalculator {
    double calculateStorageCost(StockPortion stockPortion, LocalDateTime time);
}

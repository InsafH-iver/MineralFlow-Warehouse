package be.kdg.mineralflow.warehouse.business.util.storageCost;

import be.kdg.mineralflow.warehouse.business.domain.StockPortion;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@ConditionalOnProperty(name = "storage-cost.strategy", havingValue = "daily_rate")
public class DailyRateStorageCostCalculator implements StorageCostCalculator {
    @Override
    public double calculateStorageCost(StockPortion stockPortion, LocalDateTime time) {
        long daysBetween = stockPortion.getDaysBetween(time);
        return daysBetween * stockPortion.getStorageCostPerTonPerDay();
    }
}

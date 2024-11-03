package be.kdg.mineralflow.warehouse.business.util.storageCost;

import be.kdg.mineralflow.warehouse.business.domain.InvoiceLine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@ConditionalOnProperty(name = "storage-cost.strategy", havingValue = "daily_rate")
public class DailyRateStorageCostCalculator implements StorageCostCalculator {
    @Override
    public double calculateStorageCost(InvoiceLine invoiceLine, LocalDate time) {
        long daysBetween = invoiceLine.getDaysInStorage(time);
        return  daysBetween *
                invoiceLine.getStorageCostPerTonPerDay() *
                invoiceLine.getAmountInTon();
    }
}

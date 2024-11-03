package be.kdg.mineralflow.warehouse.business.util.storageCost;

import be.kdg.mineralflow.warehouse.TestContainer;
import be.kdg.mineralflow.warehouse.business.domain.Resource;
import be.kdg.mineralflow.warehouse.business.domain.StockPortion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DailyRateStorageCostCalculatorTest extends TestContainer {
    @Autowired
    private DailyRateStorageCostCalculator storageCostCalculator;
    @Test
    void getTotalStorageCost_happyPath_with_DailyRateStorageCostCalculator() {
        //ARRANGE

        ZonedDateTime date =ZonedDateTime.of(2024,10,30,15,58,0,0, ZoneId.of("UTC"));
        UUID resourceId1 = UUID.randomUUID();
        Resource resource1 = new Resource(resourceId1,"A resource used in testing","TestResource",5,3);
        StockPortion stockPortion1 = new StockPortion(20, date.minusDays(12),resource1.getStoragePricePerTonPerDay());
        //ACT
        double totalStorageCost = storageCostCalculator.calculateStorageCost(stockPortion1,date.toLocalDateTime());
        //ASSERT
        assertThat(totalStorageCost).isEqualTo(
                stockPortion1.getAmountLeftInTon()*
                stockPortion1.getDaysBetween(date.toLocalDateTime())*
                stockPortion1.getStorageCostPerTonPerDay());
    }
    @Test
    void getTotalStorageCost_should_return_zero_when_no_days_in_storage_with_DailyRateStorageCostCalculator() {
        //ARRANGE

        ZonedDateTime date =ZonedDateTime.of(2024,10,30,15,58,0,0, ZoneId.of("UTC"));
        UUID resourceId1 = UUID.randomUUID();
        Resource resource1 = new Resource(resourceId1,"A resource used in testing","TestResource",5,3);
        StockPortion stockPortion1 = new StockPortion(20, date,resource1.getStoragePricePerTonPerDay());
        //ACT
        double totalStorageCost = storageCostCalculator.calculateStorageCost(stockPortion1,date.toLocalDateTime());
        //ASSERT
        assertThat(totalStorageCost).isEqualTo(0);
    }
}
package be.kdg.mineralflow.warehouse.business.util.storageCost;

import be.kdg.mineralflow.warehouse.TestContainer;
import be.kdg.mineralflow.warehouse.business.domain.InvoiceLine;
import be.kdg.mineralflow.warehouse.business.domain.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DailyRateStorageCostCalculatorTest extends TestContainer {
    @Autowired
    private DailyRateStorageCostCalculator storageCostCalculator;
    @Test
    void getTotalStorageCost_happyPath_with_DailyRateStorageCostCalculator() {
        //ARRANGE

        ZonedDateTime date =ZonedDateTime.of(2024,10,30,15,58,0,0, ZoneId.of("UTC"));
        UUID resourceId1 = UUID.randomUUID();
        Resource resource1 = new Resource(resourceId1,"A resource used in testing","TestResource",5,3);
        InvoiceLine invoiceLine = new InvoiceLine(resource1, date.minusDays(12),20,resource1.getStoragePricePerTonPerDay());
        //ACT
        double totalStorageCost = storageCostCalculator.calculateStorageCost(invoiceLine,date.toLocalDate());
        //ASSERT
        assertThat(totalStorageCost).isEqualTo(
                invoiceLine.getAmountInTon()*
                invoiceLine.getDaysInStorage(date.toLocalDate())*
                invoiceLine.getStorageCostPerTonPerDay());
    }
    @Test
    void getTotalStorageCost_should_return_zero_when_no_days_in_storage_with_DailyRateStorageCostCalculator() {
        //ARRANGE

        ZonedDateTime date =ZonedDateTime.of(2024,10,30,15,58,0,0, ZoneId.of("UTC"));
        UUID resourceId1 = UUID.randomUUID();
        Resource resource1 = new Resource(resourceId1,"A resource used in testing","TestResource",5,3);
        InvoiceLine invoiceLine = new InvoiceLine(resource1, date,20,resource1.getStoragePricePerTonPerDay());
        //ACT
        double totalStorageCost = storageCostCalculator.calculateStorageCost(invoiceLine,date.toLocalDate());
        //ASSERT
        assertThat(totalStorageCost).isEqualTo(0);
    }
}
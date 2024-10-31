package be.kdg.mineralflow.warehouse.handler;

import be.kdg.mineralflow.warehouse.TestContainer;
import be.kdg.mineralflow.warehouse.business.domain.StockPortion;
import be.kdg.mineralflow.warehouse.business.domain.Warehouse;
import be.kdg.mineralflow.warehouse.exception.IncorrectDomainException;
import be.kdg.mineralflow.warehouse.exception.NoItemFoundException;
import be.kdg.mineralflow.warehouse.persistence.WarehouseRepository;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.TruckArrivalAtWarehouseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TruckArrivalHandlerTest extends TestContainer {
    @Autowired
    private TruckArrivalHandler truckArrivalHandler;
    @Autowired
    private WarehouseRepository warehouseRepository;

    @Test
    void handleStockPortionAtDelivery_Should_Succeed_When_Warehouse_Exists_And_Weight_Is_Positive_And_DeliveryTime_exists() {
        //ARRANGE
        double usedCapacity = 123;
        double storagePricePerTon = 12.4;
        double amountInTon = 30;
        ZonedDateTime deliveryTime = ZonedDateTime.of(2024, 2, 2, 2, 2, 2, 0, ZoneOffset.UTC);
        ZonedDateTime endWeightTime = ZonedDateTime.of(2024, 2, 2, 2, 12, 2, 2, ZoneOffset.UTC);
        UUID resourceId = UUID.fromString("11111111-1111-1111-1111-111111111120");
        UUID vendorId = UUID.fromString("11111111-1111-1111-1111-111111111121");
        UUID unloadingRequestId = UUID.fromString("11111111-1111-1111-1111-111111111131");
        TruckArrivalAtWarehouseDto truckArrivalAtWarehouseDto =
                new TruckArrivalAtWarehouseDto(vendorId, resourceId, amountInTon, endWeightTime, unloadingRequestId);


        //ACT
        truckArrivalHandler.truckArrival(truckArrivalAtWarehouseDto);

        //ASSERT
        Warehouse warehouse = warehouseRepository
                .findWarehouseWithPortionsById(UUID.fromString("11111111-1111-1111-1111-111111111122"))
                .get();
        List<StockPortion> stockPortions = warehouse.getStockPortions();
        assertThat(stockPortions.size()).isEqualTo(1);
        StockPortion actualStockPortion = stockPortions.getFirst();
        assertThat(actualStockPortion.getAmountInTon()).isEqualTo(amountInTon);
        assertThat(actualStockPortion.getArrivalTime()).isEqualTo(deliveryTime);
        assertThat(warehouse.getUsedCapacityInTon()).isEqualTo(usedCapacity + amountInTon);
        assertThat(actualStockPortion.getStorageCostPerTonPerDay()).isEqualTo(storagePricePerTon);
    }


    @Test
    void handleStockPortionAtDelivery_Should_Succeed_When_Warehouse_Exists_And_Weight_Is_Positive_And_DeliveryTime_Does_Not_Exists() {
        //ARRANGE
        double usedCapacity = 123;
        double storagePricePerTon = 12.4;
        double amountInTon = 30;
        ZonedDateTime endWeightTime = ZonedDateTime.of(2024, 2, 2, 2, 12, 2, 0, ZoneOffset.UTC);
        UUID resourceId = UUID.fromString("11111111-1111-1111-1111-111111111120");
        UUID vendorId = UUID.fromString("11111111-1111-1111-1111-111111111121");
        UUID unloadingRequestId = UUID.fromString("24111111-1111-1111-1111-111111111131");
        TruckArrivalAtWarehouseDto truckArrivalAtWarehouseDto =
                new TruckArrivalAtWarehouseDto(vendorId, resourceId, amountInTon, endWeightTime, unloadingRequestId);


        //ACT
        truckArrivalHandler.truckArrival(truckArrivalAtWarehouseDto);

        //ASSERT
        Warehouse warehouse = warehouseRepository
                .findWarehouseWithPortionsById(UUID.fromString("11111111-1111-1111-1111-111111111122"))
                .get();
        List<StockPortion> stockPortions = warehouse.getStockPortions();
        assertThat(stockPortions.size()).isEqualTo(1);
        StockPortion actualStockPortion = stockPortions.getFirst();
        assertThat(actualStockPortion.getAmountInTon()).isEqualTo(amountInTon);
        assertThat(actualStockPortion.getArrivalTime()).isEqualTo(endWeightTime);
        assertThat(warehouse.getUsedCapacityInTon()).isEqualTo(usedCapacity + amountInTon);
        assertThat(actualStockPortion.getStorageCostPerTonPerDay()).isEqualTo(storagePricePerTon);
    }

    @Test
    void handleStockPortionAtDelivery_Should_Throw_Exception_When_Warehouse_Does_Not_Exist() {
        //ARRANGE
        double amountInTon = 30;
        ZonedDateTime endWeightTime = ZonedDateTime.of(2024, 2, 2, 2, 12, 2, 0, ZoneOffset.UTC);
        UUID resourceId = UUID.fromString("11111111-1111-1111-1111-111111111120");
        UUID vendorId = UUID.fromString("34011111-1111-1111-1111-111111111120");
        UUID unloadingRequestId = UUID.fromString("24111111-1111-1111-1111-111111111131");

        TruckArrivalAtWarehouseDto truckArrivalAtWarehouseDto =
                new TruckArrivalAtWarehouseDto(vendorId, resourceId, amountInTon, endWeightTime, unloadingRequestId);

        //ACT
        // ASSERT
        assertThrows(NoItemFoundException.class, () ->
                truckArrivalHandler.truckArrival(truckArrivalAtWarehouseDto));
    }

    @Test
    void handleStockPortionAtDelivery_Should_Throw_Exception_When_Resource_Does_Not_Exist() {
        //ARRANGE
        double amountInTon = 30;
        ZonedDateTime endWeightTime = ZonedDateTime.of(2024, 2, 2, 2, 12, 2, 0, ZoneOffset.UTC);
        UUID resourceId = UUID.fromString("34011111-1111-1111-1111-111111111121");
        UUID vendorId = UUID.fromString("34011111-1111-1111-1111-111111111120");
        UUID unloadingRequestId = UUID.fromString("24111111-1111-1111-1111-111111111131");

        TruckArrivalAtWarehouseDto truckArrivalAtWarehouseDto =
                new TruckArrivalAtWarehouseDto(vendorId, resourceId, amountInTon, endWeightTime, unloadingRequestId);

        //ACT
        // ASSERT
        assertThrows(NoItemFoundException.class, () ->
                truckArrivalHandler.truckArrival(truckArrivalAtWarehouseDto));
    }

    @Test
    void handleStockPortionAtDelivery_Should_Throw_Exception_When_Weight_Is_Negative() {
        //ARRANGE
        double amountInTon = -30;
        ZonedDateTime endWeightTime = ZonedDateTime.of(2024, 2, 2, 2, 12, 2, 2, ZoneOffset.UTC);
        UUID resourceId = UUID.fromString("11111111-1111-1111-1111-111111111120");
        UUID vendorId = UUID.fromString("11111111-1111-1111-1111-111111111121");
        UUID unloadingRequestId = UUID.fromString("11111111-1111-1111-1111-111111111131");
        TruckArrivalAtWarehouseDto truckArrivalAtWarehouseDto =
                new TruckArrivalAtWarehouseDto(vendorId, resourceId, amountInTon, endWeightTime, unloadingRequestId);

        //ACT
        // ASSERT
        assertThrows(IncorrectDomainException.class, () ->
                truckArrivalHandler.truckArrival(truckArrivalAtWarehouseDto));
    }
}
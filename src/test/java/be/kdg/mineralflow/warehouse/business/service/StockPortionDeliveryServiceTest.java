package be.kdg.mineralflow.warehouse.business.service;

import be.kdg.mineralflow.warehouse.TestContainer;
import be.kdg.mineralflow.warehouse.business.domain.Resource;
import be.kdg.mineralflow.warehouse.business.domain.StockPortion;
import be.kdg.mineralflow.warehouse.business.domain.Warehouse;
import be.kdg.mineralflow.warehouse.config.ConfigProperties;
import be.kdg.mineralflow.warehouse.exception.IncorrectDomainException;
import be.kdg.mineralflow.warehouse.exception.NoItemFoundException;
import be.kdg.mineralflow.warehouse.persistence.ResourceRepository;
import be.kdg.mineralflow.warehouse.persistence.WarehouseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StockPortionDeliveryServiceTest extends TestContainer {

    @MockBean
    private WarehouseRepository warehouseRepository;
    @MockBean
    private ResourceRepository resourceRepository;
    @Autowired
    private StockPortionDeliveryService stockPortionDeliveryService;
    @Autowired
    private ConfigProperties configProperties;

    @Test
    void handleStockPortionAtDelivery_Should_Succeed_When_Warehouse_Exists_And_Weight_Is_Positive() {
        //ARRANGE
        double usedCapacity = 123;
        double storagePricePerTon = 12.4;
        double amountInTon = 30;
        ZonedDateTime deliveryTime = ZonedDateTime.of(2024, 2, 2, 2, 2, 2, 2, ZoneOffset.UTC);
        UUID resourceId = UUID.randomUUID();
        UUID vendorId = UUID.randomUUID();
        Warehouse warehouse = new Warehouse(UUID.randomUUID(), 3,
                usedCapacity,configProperties.warehouseMaxCapacityInTon);
        Resource resource = new Resource(resourceId, "description",
                "Betton", 345, storagePricePerTon);


        Mockito.when(warehouseRepository.
                        findFirstByVendorIdAndResourceId(vendorId, resourceId))
                .thenReturn(Optional.of(warehouse));
        Mockito.when(warehouseRepository.save(warehouse)).thenReturn(warehouse);
        Mockito.when(resourceRepository.findById(resourceId))
                .thenReturn(Optional.of(resource));
        //ACT
        stockPortionDeliveryService.handleStockPortionAtDelivery(vendorId, resourceId, amountInTon, deliveryTime);

        //ASSERT
        List<StockPortion> stockPortions = warehouse.getStockPortions();
        assertThat(stockPortions.size()).isEqualTo(1);
        StockPortion actualStockPortion = stockPortions.getFirst();
        assertThat(actualStockPortion.getAmountInTon()).isEqualTo(amountInTon);
        assertThat(actualStockPortion.getArrivalTime()).isEqualTo(deliveryTime);
        assertThat(actualStockPortion.getStorageCostPerTonPerDay()).isEqualTo(storagePricePerTon);
        Mockito.verify(warehouseRepository, Mockito.times(1)).findFirstByVendorIdAndResourceId(vendorId, resourceId);
    }

    @Test
    void handleStockPortionAtDelivery_Should_Throw_Exception_When_Warehouse_Does_Not_Exist() {
        //ARRANGE
        double storagePricePerTon = 12.4;
        double amountInTon = 30;
        ZonedDateTime deliveryTime = ZonedDateTime.of(2024, 2, 2, 2, 2, 2, 2, ZoneOffset.UTC);
        UUID resourceId = UUID.randomUUID();
        UUID vendorId = UUID.randomUUID();
        Resource resource = new Resource(resourceId, "description",
                "Betton", 345, storagePricePerTon);


        Mockito.when(warehouseRepository.
                        findFirstByVendorIdAndResourceId(vendorId, resourceId))
                .thenReturn(Optional.empty());
        Mockito.when(resourceRepository.findById(resourceId))
                .thenReturn(Optional.of(resource));
        //ACT
        // ASSERT
        assertThrows(NoItemFoundException.class, () ->
                stockPortionDeliveryService.handleStockPortionAtDelivery(
                        vendorId, resourceId, amountInTon, deliveryTime));
    }

    @Test
    void handleStockPortionAtDelivery_Should_Throw_Exception_When_Resource_Does_Not_Exist() {
        //ARRANGE
        double amountInTon = 30;
        ZonedDateTime deliveryTime = ZonedDateTime.of(2024, 2, 2, 2, 2, 2, 2, ZoneOffset.UTC);
        UUID resourceId = UUID.randomUUID();
        UUID vendorId = UUID.randomUUID();

        Mockito.when(warehouseRepository.
                        findFirstByVendorIdAndResourceId(vendorId, resourceId))
                .thenReturn(Optional.empty());
        Mockito.when(resourceRepository.findById(resourceId))
                .thenReturn(Optional.empty());
        //ACT
        // ASSERT
        assertThrows(NoItemFoundException.class, () ->
                stockPortionDeliveryService.handleStockPortionAtDelivery(
                        vendorId, resourceId, amountInTon, deliveryTime));
    }


    @Test
    void handleStockPortionAtDelivery_Should_Throw_Exception_When_Weight_Is_Negative() {
        //ARRANGE
        double usedCapacity = 123;
        double storagePricePerTon = 12.4;
        double amountInTon = -10;
        ZonedDateTime deliveryTime = ZonedDateTime.of(2024, 2, 2, 2, 2, 2, 2, ZoneOffset.UTC);
        UUID resourceId = UUID.randomUUID();
        UUID vendorId = UUID.randomUUID();
        Warehouse warehouse = new Warehouse(UUID.randomUUID(), 3,
                usedCapacity,configProperties.warehouseMaxCapacityInTon);
        Resource resource = new Resource(resourceId, "description",
                "Betton", 345, storagePricePerTon);


        Mockito.when(warehouseRepository.
                        findFirstByVendorIdAndResourceId(vendorId, resourceId))
                .thenReturn(Optional.of(warehouse));
        Mockito.when(warehouseRepository.save(warehouse)).thenReturn(warehouse);
        Mockito.when(resourceRepository.findById(resourceId))
                .thenReturn(Optional.of(resource));
        //ACT
        // ASSERT
        assertThrows(IncorrectDomainException.class, () ->
                stockPortionDeliveryService.handleStockPortionAtDelivery(
                        vendorId, resourceId, amountInTon, deliveryTime));
    }
}
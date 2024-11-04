package be.kdg.mineralflow.warehouse.handler;

import be.kdg.mineralflow.warehouse.TestContainer;
import be.kdg.mineralflow.warehouse.business.domain.*;
import be.kdg.mineralflow.warehouse.business.service.externalApi.EndOfPurchaseOrderPickUpPublisher;
import be.kdg.mineralflow.warehouse.business.service.warehouse.StockPortionDeliveryService;
import be.kdg.mineralflow.warehouse.business.util.provider.ZonedDateTimeProvider;
import be.kdg.mineralflow.warehouse.exception.IncorrectDomainException;
import be.kdg.mineralflow.warehouse.exception.NoItemFoundException;
import be.kdg.mineralflow.warehouse.persistence.WarehouseRepository;
import be.kdg.mineralflow.warehouse.persistence.purchase.order.OrderLineRepository;
import be.kdg.mineralflow.warehouse.persistence.purchase.order.PurchaseOrderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

class TruckArrivalHandlerTest extends TestContainer {
    @Autowired
    private StockPortionDeliveryService stockPortionDeliveryService;
    @Autowired
    private WarehouseRepository warehouseRepository;
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;
    @Autowired
    private OrderLineRepository orderLineRepository;
    @MockBean
    private EndOfPurchaseOrderPickUpPublisher endOfPurchaseOrderPickUpPublisher;
    @MockBean
    private ZonedDateTimeProvider zonedDateTimeProvider;

    @Test
    void handleStockPortionAtDelivery_Should_Succeed_When_Warehouse_Exists_And_Weight_Is_Positive_And_DeliveryTime_exists() {
        //ARRANGE
        ZonedDateTime endTime = ZonedDateTime.of(2024, 11, 2, 2, 2, 2, 0, ZoneOffset.UTC);
        try (MockedStatic<ZonedDateTime> mockedStatic = mockStatic(ZonedDateTime.class)) {

            mockedStatic.when(ZonedDateTime::now).thenReturn(endTime);

            double usedCapacity = 123;
            double storagePricePerTon = 12.4;
            double amountInTon = 30;
            ZonedDateTime deliveryTime = ZonedDateTime.of(2024, 2, 2, 2, 2, 2, 0, ZoneOffset.UTC);
            ZonedDateTime endWeightTime = ZonedDateTime.of(2024, 2, 2, 2, 12, 2, 2, ZoneOffset.UTC);
            UUID resourceId = UUID.fromString("11111111-1111-1111-1111-111111111120");
            UUID vendorId = UUID.fromString("11111111-1111-1111-1111-111111111121");
            UUID unloadingRequestId = UUID.fromString("11111111-1111-1111-1111-111111111131");

            Mockito.doNothing().when(endOfPurchaseOrderPickUpPublisher)
                    .publishEndOfPurchaseOrderPickUp(null, endTime);
            //ACT
            stockPortionDeliveryService.handleStockPortionAtDelivery(
                    vendorId, resourceId, amountInTon, unloadingRequestId, endWeightTime);

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

            Mockito.verify(endOfPurchaseOrderPickUpPublisher, Mockito.times(0))
                    .publishEndOfPurchaseOrderPickUp(null, endTime);
        }
    }

    @Test
    void handleStockPortionAtDelivery_Should_Succeed_When_Warehouse_Exists_And_Weight_Is_Positive_And_DeliveryTime_Does_Not_Exists() {
        //ARRANGE
        ZonedDateTime endTime = ZonedDateTime.of(2024, 11, 2, 2, 2, 2, 0, ZoneOffset.UTC);
        try (MockedStatic<ZonedDateTime> mockedStatic = mockStatic(ZonedDateTime.class)) {

            mockedStatic.when(ZonedDateTime::now).thenReturn(endTime);
            double usedCapacity = 123;
            double storagePricePerTon = 12.4;
            double amountInTon = 30;
            ZonedDateTime endWeightTime = ZonedDateTime.of(2024, 2, 2, 2, 12, 2, 0, ZoneOffset.UTC);
            UUID resourceId = UUID.fromString("11111111-1111-1111-1111-111111111120");
            UUID vendorId = UUID.fromString("11111111-1111-1111-1111-111111111121");
            UUID unloadingRequestId = UUID.fromString("24111111-1111-1111-1111-111111111131");

            Mockito.doNothing().when(endOfPurchaseOrderPickUpPublisher)
                    .publishEndOfPurchaseOrderPickUp(null, endTime);

            //ACT
            stockPortionDeliveryService.handleStockPortionAtDelivery(
                    vendorId, resourceId, amountInTon, unloadingRequestId, endWeightTime);

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
            Mockito.verify(endOfPurchaseOrderPickUpPublisher, Mockito.times(0))
                    .publishEndOfPurchaseOrderPickUp(null, endTime);
        }
    }

    @Test
    void handleStockPortionAtDelivery_Should_Succeed_And_Should_Reprocess_OrderLines_But_Not_End_PO() {
        //ARRANGE
        ZonedDateTime deliveryTime = ZonedDateTime.of(2024, 2, 2, 2, 2, 2, 0, ZoneOffset.UTC);
        ZonedDateTime endWeightTime = ZonedDateTime.of(2024, 2, 2, 2, 12, 2, 0, ZoneOffset.UTC);
        ZonedDateTime endTime = ZonedDateTime.of(2024, 11, 2, 2, 2, 2, 0, ZoneOffset.UTC);
        String poNumber = "PO2345";

        double usedCapacity = 0;
        double storagePricePerTon = 13;
        double amountInTon = 50;
        double endAmountInTon = 0;
        UUID poId = UUID.fromString("33333333-1111-1111-1111-111111111130");
        UUID resourceId = UUID.fromString("33333333-1111-1111-1111-111111111201");
        UUID vendorId = UUID.fromString("33333333-1111-1111-1111-111111111200");
        UUID unloadingRequestId = UUID.fromString("33333311-1111-1111-1111-111111111131");
          UUID orderLineId = UUID.fromString("22222233-1111-1111-1111-511111111131");
        Mockito.when(zonedDateTimeProvider.now()).thenReturn(endTime);
        Mockito.doNothing().when(endOfPurchaseOrderPickUpPublisher)
                .publishEndOfPurchaseOrderPickUp(poNumber, endTime);
        //ACT
        stockPortionDeliveryService.handleStockPortionAtDelivery(
                vendorId, resourceId, amountInTon, unloadingRequestId, endWeightTime);

        //ASSERT

        Warehouse warehouse = warehouseRepository
                .findWarehouseWithPortionsById(UUID.fromString("33333333-1111-1111-1111-111111111202"))
                .get();
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(poId).get();
        OrderLine orderLine = orderLineRepository.findById(orderLineId).get();
        List<StockPortion> stockPortions = warehouse.getStockPortions();
        assertThat(stockPortions.size()).isEqualTo(1);
        StockPortion actualStockPortion = stockPortions.getFirst();
        assertThat(actualStockPortion.getAmountInTon()).isEqualTo(amountInTon);
        assertThat(actualStockPortion.getAmountLeftInTon()).isEqualTo(endAmountInTon);
        assertThat(actualStockPortion.getArrivalTime()).isEqualTo(deliveryTime);
        assertThat(warehouse.getUsedCapacityInTon()).isEqualTo(usedCapacity);
        assertThat(actualStockPortion.getStorageCostPerTonPerDay()).isEqualTo(storagePricePerTon);
        assertThat(purchaseOrder.getStatus()).isEqualTo(Status.COMPLETED);
        assertThat(orderLine.hasBeenCompleted()).isTrue();

        Mockito.verify(endOfPurchaseOrderPickUpPublisher, Mockito.times(1))
                .publishEndOfPurchaseOrderPickUp(poNumber, endTime);

    }

    @Test
    void handleStockPortionAtDelivery_Should_Throw_Exception_When_Warehouse_Does_Not_Exist() {
        //ARRANGE
        double amountInTon = 30;
        ZonedDateTime endWeightTime = ZonedDateTime.of(2024, 2, 2, 2, 12, 2, 0, ZoneOffset.UTC);
        UUID resourceId = UUID.fromString("11111111-1111-1111-1111-111111111120");
        UUID vendorId = UUID.fromString("34011111-1111-1111-1111-111111111120");
        UUID unloadingRequestId = UUID.fromString("24111111-1111-1111-1111-111111111131");

        //ACT
        // ASSERT
        assertThrows(NoItemFoundException.class, () ->
                stockPortionDeliveryService.handleStockPortionAtDelivery(
                        vendorId, resourceId, amountInTon, unloadingRequestId, endWeightTime));
    }

    @Test
    void handleStockPortionAtDelivery_Should_Throw_Exception_When_Resource_Does_Not_Exist() {
        //ARRANGE
        double amountInTon = 30;
        ZonedDateTime endWeightTime = ZonedDateTime.of(2024, 2, 2, 2, 12, 2, 0, ZoneOffset.UTC);
        UUID resourceId = UUID.fromString("34011111-1111-1111-1111-111111111121");
        UUID vendorId = UUID.fromString("34011111-1111-1111-1111-111111111120");
        UUID unloadingRequestId = UUID.fromString("24111111-1111-1111-1111-111111111131");

        //ACT
        // ASSERT
        assertThrows(NoItemFoundException.class, () ->
                stockPortionDeliveryService.handleStockPortionAtDelivery(
                        vendorId, resourceId, amountInTon, unloadingRequestId, endWeightTime));
    }

    @Test
    void handleStockPortionAtDelivery_Should_Throw_Exception_When_Weight_Is_Negative() {
        //ARRANGE
        double amountInTon = -30;
        ZonedDateTime endWeightTime = ZonedDateTime.of(2024, 2, 2, 2, 12, 2, 2, ZoneOffset.UTC);
        UUID resourceId = UUID.fromString("11111111-1111-1111-1111-111111111120");
        UUID vendorId = UUID.fromString("11111111-1111-1111-1111-111111111121");
        UUID unloadingRequestId = UUID.fromString("11111111-1111-1111-1111-111111111131");

        //ACT
        // ASSERT
        assertThrows(IncorrectDomainException.class, () ->
                stockPortionDeliveryService.handleStockPortionAtDelivery(
                        vendorId, resourceId, amountInTon, unloadingRequestId, endWeightTime));
    }
}
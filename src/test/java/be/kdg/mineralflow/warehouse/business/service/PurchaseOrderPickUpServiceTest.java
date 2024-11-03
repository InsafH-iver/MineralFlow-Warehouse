package be.kdg.mineralflow.warehouse.business.service;

import be.kdg.mineralflow.warehouse.TestContainer;
import be.kdg.mineralflow.warehouse.business.domain.*;
import be.kdg.mineralflow.warehouse.business.service.externalApi.EndOfPurchaseOrderPickUpPublisher;
import be.kdg.mineralflow.warehouse.business.service.purchase.order.PurchaseOrderPickUpService;
import be.kdg.mineralflow.warehouse.exception.NoItemFoundException;
import be.kdg.mineralflow.warehouse.persistence.purchase.order.PurchaseOrderRepository;
import be.kdg.mineralflow.warehouse.persistence.WarehouseRepository;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

class PurchaseOrderPickUpServiceTest extends TestContainer {

    @MockBean
    private PurchaseOrderRepository purchaseOrderRepository;
    @MockBean
    private WarehouseRepository warehouseRepository;

    @MockBean
    private EndOfPurchaseOrderPickUpPublisher endOfPurchaseOrderPickUpPublisher;

    @Autowired
    private PurchaseOrderPickUpService purchaseOrderPickUpService;

    @Test
    void processPurchaseOrderPickUp_Should_Succeed_When_PurchaseOrder_Exists_And_Its_Lines_Are_For_Different_resources() {
        //ARRANGE
        ZonedDateTime stockPortion1Time = ZonedDateTime.of(2024, 2, 2, 2, 2, 2, 0, ZoneOffset.UTC);
        ZonedDateTime stockPortion2Time = ZonedDateTime.of(2024, 2, 3, 2, 2, 2, 0, ZoneOffset.UTC);
        try (MockedStatic<ZonedDateTime> mockedStatic = mockStatic(ZonedDateTime.class)) {

            ZonedDateTime endTime = ZonedDateTime.of(2024, 11, 2, 2, 2, 2, 0, ZoneOffset.UTC);

            mockedStatic.when(ZonedDateTime::now).thenReturn(endTime);


            UUID vendorId = UUID.randomUUID();
            UUID resourceId1 = UUID.randomUUID();
            UUID resourceId2 = UUID.randomUUID();

            UUID warehouseId1 = UUID.randomUUID();
            UUID warehouseId2 = UUID.randomUUID();

            UUID ol1 = UUID.randomUUID();
            UUID ol2 = UUID.randomUUID();

            UUID poId = UUID.randomUUID();
            double usedCapacityInTon1 = 50;
            double usedCapacityInTon2 = 80;
            double maxCapacityInTon = 200;
            double ol1Amount = 50;
            double ol2Amount = 50;

            String purchaseOrderNumber = "PO2345";
            String vesselNumber = "VE123456";
            Vendor vendorTest = new Vendor(vendorId, "johnson & Johnson");
            Resource resource1 = new Resource(resourceId1, "test description 1", "resourceName 1", 90, 12);
            Resource resource2 = new Resource(resourceId2, "test description 2", "resourceName 2", 90, 12);
            double stockPortion1Amount = 50;
            double stockPortion2Amount = 80;
            StockPortion stockPortion1 = new StockPortion(stockPortion1Amount, stockPortion1Time, 12);
            StockPortion stockPortion2 = new StockPortion(stockPortion2Amount, stockPortion2Time, 12);

            Warehouse warehouse1 = new Warehouse(warehouseId1, 1, usedCapacityInTon1, maxCapacityInTon);
            Warehouse warehouse2 = new Warehouse(warehouseId2, 2, usedCapacityInTon2, maxCapacityInTon);
            warehouse1.setStockPortions(new ArrayList<>(Collections.singletonList(stockPortion1)));
            warehouse2.setStockPortions(new ArrayList<>(Collections.singletonList(stockPortion2)));
            warehouse1.setVendor(vendorTest);
            warehouse1.setResource(resource1);

            warehouse2.setVendor(vendorTest);
            warehouse2.setResource(resource2);

            OrderLine orderLine1 = new OrderLine(ol1Amount, false, ol1, 12, resource1);
            OrderLine orderLine2 = new OrderLine(ol2Amount, false, ol2, 12, resource2);
            List<OrderLine> orderLines = new ArrayList<>();
            orderLines.add(orderLine1);
            orderLines.add(orderLine2);
            PurchaseOrder purchaseOrder = new PurchaseOrder(poId, orderLines, purchaseOrderNumber, Status.OPEN, vendorTest, vesselNumber);


            Mockito.when(purchaseOrderRepository
                            .findByPurchaseOrderNumberAndVendorIdAndStatus(purchaseOrderNumber, vendorId, Status.OPEN))
                    .thenReturn(Optional.of(purchaseOrder));
            Mockito.when(purchaseOrderRepository.save(purchaseOrder)).thenReturn(purchaseOrder);

            Mockito.when(warehouseRepository.findFirstByVendorIdAndResourceId(vendorId, resourceId1))
                    .thenReturn(Optional.of(warehouse1));

            Mockito.when(warehouseRepository.findFirstByVendorIdAndResourceId(vendorId, resourceId2))
                    .thenReturn(Optional.of(warehouse2));

            Mockito.when(warehouseRepository.save(warehouse1)).thenReturn(warehouse1);
            Mockito.when(warehouseRepository.save(warehouse2)).thenReturn(warehouse2);

            Mockito.doNothing().when(endOfPurchaseOrderPickUpPublisher)
                    .publishEndOfPurchaseOrderPickUp(purchaseOrderNumber, endTime);

            //ACT
            purchaseOrderPickUpService.processPurchaseOrderPickUp(purchaseOrderNumber, vendorId);
            //ASSERT
            assertThat(orderLine1.hasBeenCompleted()).isEqualTo(true);
            assertThat(orderLine2.hasBeenCompleted()).isEqualTo(true);

            assertThat(warehouse1.getUsedCapacityInTon()).isEqualTo(usedCapacityInTon1 - ol1Amount);
            assertThat(warehouse2.getUsedCapacityInTon()).isEqualTo(usedCapacityInTon2 - ol2Amount);

            assertThat(stockPortion1.getAmountLeftInTon()).isEqualTo(stockPortion1Amount - ol1Amount);
            assertThat(stockPortion2.getAmountLeftInTon()).isEqualTo(stockPortion2Amount - ol2Amount);

            assertThat(stockPortion1.getAmountInTon()).isEqualTo(stockPortion1Amount);
            assertThat(stockPortion2.getAmountInTon()).isEqualTo(stockPortion2Amount);


            assertThat(stockPortion1.isPortionEmpty()).isEqualTo(true);
            assertThat(stockPortion2.isPortionEmpty()).isEqualTo(false);

            assertThat(purchaseOrder.getStatus()).isEqualTo(Status.COMPLETED);


            Mockito.verify(endOfPurchaseOrderPickUpPublisher, Mockito.times(1))
                    .publishEndOfPurchaseOrderPickUp(purchaseOrderNumber, endTime);
        }
    }

    @Test
    void processPurchaseOrderPickUp_Should_Succeed_When_PurchaseOrder_Exists_And_Its_Lines_When_The_Amount_Taken_Is_Less_Than_The_One_StockPortion() {
        //ARRANGE
        ZonedDateTime stockPortion1Time = ZonedDateTime.of(2024, 2, 2, 2, 2, 2, 0, ZoneOffset.UTC);
        ZonedDateTime stockPortion2Time = ZonedDateTime.of(2024, 2, 3, 2, 2, 2, 0, ZoneOffset.UTC);
        try (MockedStatic<ZonedDateTime> mockedStatic = mockStatic(ZonedDateTime.class)) {
            ZonedDateTime endTime = ZonedDateTime.of(2024, 11, 2, 2, 2, 2, 0, ZoneOffset.UTC);

            mockedStatic.when(ZonedDateTime::now).thenReturn(endTime);


            UUID vendorId = UUID.randomUUID();
            UUID resourceId1 = UUID.randomUUID();
            UUID warehouseId1 = UUID.randomUUID();

            UUID ol1 = UUID.randomUUID();
            UUID ol2 = UUID.randomUUID();

            UUID poId = UUID.randomUUID();
            double usedCapacityInTon1 = 130;

            double maxCapacityInTon = 200;
            double ol1Amount = 50;
            double ol2Amount = 50;

            String purchaseOrderNumber = "PO2345";
            String vesselNumber = "VE123456";
            Vendor vendorTest = new Vendor(vendorId, "johnson & Johnson");
            Resource resource1 = new Resource(resourceId1, "test description 1", "resourceName 1", 90, 12);
            double stockPortion1Amount = 70;
            double stockPortion2Amount = 60;
            double endAmountSt1 = 0;
            double endAmountSt2 = (stockPortion2Amount + stockPortion1Amount) - (ol1Amount + ol2Amount);

            StockPortion stockPortion1 = new StockPortion(stockPortion1Amount, stockPortion1Time, 12);
            StockPortion stockPortion2 = new StockPortion(stockPortion2Amount, stockPortion2Time, 12);

            Warehouse warehouse1 = new Warehouse(warehouseId1, 1, usedCapacityInTon1, maxCapacityInTon);
            warehouse1.setStockPortions(new ArrayList<>(List.of(stockPortion1, stockPortion2)));
            warehouse1.setVendor(vendorTest);
            warehouse1.setResource(resource1);

            OrderLine orderLine1 = new OrderLine(ol1Amount, false, ol1, 12, resource1);
            OrderLine orderLine2 = new OrderLine(ol2Amount, false, ol2, 12, resource1);
            List<OrderLine> orderLines = new ArrayList<>();
            orderLines.add(orderLine1);
            orderLines.add(orderLine2);
            PurchaseOrder purchaseOrder = new PurchaseOrder(poId, orderLines, purchaseOrderNumber, Status.OPEN, vendorTest, vesselNumber);


            Mockito.when(purchaseOrderRepository
                            .findByPurchaseOrderNumberAndVendorIdAndStatus(purchaseOrderNumber, vendorId, Status.OPEN))
                    .thenReturn(Optional.of(purchaseOrder));
            Mockito.when(purchaseOrderRepository.save(purchaseOrder)).thenReturn(purchaseOrder);

            Mockito.when(warehouseRepository.findFirstByVendorIdAndResourceId(vendorId, resourceId1))
                    .thenReturn(Optional.of(warehouse1));

            Mockito.when(warehouseRepository.save(warehouse1)).thenReturn(warehouse1);

            Mockito.doNothing().when(endOfPurchaseOrderPickUpPublisher)
                    .publishEndOfPurchaseOrderPickUp(purchaseOrderNumber, endTime);

            //ACT
            purchaseOrderPickUpService.processPurchaseOrderPickUp(purchaseOrderNumber, vendorId);
            //ASSERT
            assertThat(orderLine1.hasBeenCompleted()).isEqualTo(true);
            assertThat(orderLine2.hasBeenCompleted()).isEqualTo(true);

            assertThat(warehouse1.getUsedCapacityInTon()).isEqualTo(usedCapacityInTon1 - (ol1Amount + ol2Amount));

            assertThat(stockPortion1.getAmountLeftInTon()).isEqualTo(endAmountSt1);
            assertThat(stockPortion2.getAmountLeftInTon()).isEqualTo(endAmountSt2);

            assertThat(stockPortion1.getAmountInTon()).isEqualTo(stockPortion1Amount);
            assertThat(stockPortion2.getAmountInTon()).isEqualTo(stockPortion2Amount);


            assertThat(stockPortion1.isPortionEmpty()).isEqualTo(true);
            assertThat(stockPortion2.isPortionEmpty()).isEqualTo(false);

            assertThat(purchaseOrder.getStatus()).isEqualTo(Status.COMPLETED);

            Mockito.verify(endOfPurchaseOrderPickUpPublisher, Mockito.times(1))
                    .publishEndOfPurchaseOrderPickUp(purchaseOrderNumber, endTime);
        }
    }

    @Test
    void processPurchaseOrderPickUp_Should_Succeed_When_PurchaseOrder_Exists_And_Its_Lines_Are_For_The_Same_Resource() {
        //ARRANGE
        ZonedDateTime stockPortion1Time = ZonedDateTime.of(2024, 2, 2, 2, 2, 2, 0, ZoneOffset.UTC);
        ZonedDateTime stockPortion2Time = ZonedDateTime.of(2024, 2, 3, 2, 2, 2, 0, ZoneOffset.UTC);
        try (MockedStatic<ZonedDateTime> mockedStatic = mockStatic(ZonedDateTime.class)) {
            ZonedDateTime endTime = ZonedDateTime.of(2024, 11, 2, 2, 2, 2, 0, ZoneOffset.UTC);

            mockedStatic.when(ZonedDateTime::now).thenReturn(endTime);


            UUID vendorId = UUID.randomUUID();
            UUID resourceId1 = UUID.randomUUID();
            UUID warehouseId1 = UUID.randomUUID();

            UUID ol1 = UUID.randomUUID();
            UUID ol2 = UUID.randomUUID();

            UUID poId = UUID.randomUUID();
            double usedCapacityInTon1 = 130;

            double maxCapacityInTon = 200;
            double ol1Amount = 50;
            double ol2Amount = 50;

            String purchaseOrderNumber = "PO2345";
            String vesselNumber = "VE123456";
            Vendor vendorTest = new Vendor(vendorId, "johnson & Johnson");
            Resource resource1 = new Resource(resourceId1, "test description 1", "resourceName 1", 90, 12);
            double stockPortion1Amount = 50;
            double stockPortion2Amount = 80;
            double endAmountSt1 = stockPortion1Amount - ol1Amount;
            double endAmountSt2 = (stockPortion2Amount + endAmountSt1) - ol1Amount;

            StockPortion stockPortion1 = new StockPortion(stockPortion1Amount, stockPortion1Time, 12);
            StockPortion stockPortion2 = new StockPortion(stockPortion2Amount, stockPortion2Time, 12);

            Warehouse warehouse1 = new Warehouse(warehouseId1, 1, usedCapacityInTon1, maxCapacityInTon);
            warehouse1.setStockPortions(new ArrayList<>(List.of(stockPortion1, stockPortion2)));
            warehouse1.setVendor(vendorTest);
            warehouse1.setResource(resource1);

            OrderLine orderLine1 = new OrderLine(ol1Amount, false, ol1, 12, resource1);
            OrderLine orderLine2 = new OrderLine(ol2Amount, false, ol2, 12, resource1);
            List<OrderLine> orderLines = new ArrayList<>();
            orderLines.add(orderLine1);
            orderLines.add(orderLine2);
            PurchaseOrder purchaseOrder = new PurchaseOrder(poId, orderLines, purchaseOrderNumber, Status.OPEN, vendorTest, vesselNumber);


            Mockito.when(purchaseOrderRepository
                            .findByPurchaseOrderNumberAndVendorIdAndStatus(purchaseOrderNumber, vendorId, Status.OPEN))
                    .thenReturn(Optional.of(purchaseOrder));
            Mockito.when(purchaseOrderRepository.save(purchaseOrder)).thenReturn(purchaseOrder);

            Mockito.when(warehouseRepository.findFirstByVendorIdAndResourceId(vendorId, resourceId1))
                    .thenReturn(Optional.of(warehouse1));

            Mockito.when(warehouseRepository.save(warehouse1)).thenReturn(warehouse1);

            Mockito.doNothing().when(endOfPurchaseOrderPickUpPublisher)
                    .publishEndOfPurchaseOrderPickUp(purchaseOrderNumber, endTime);

            //ACT
            purchaseOrderPickUpService.processPurchaseOrderPickUp(purchaseOrderNumber, vendorId);
            //ASSERT
            assertThat(orderLine1.hasBeenCompleted()).isEqualTo(true);
            assertThat(orderLine2.hasBeenCompleted()).isEqualTo(true);

            assertThat(warehouse1.getUsedCapacityInTon()).isEqualTo(usedCapacityInTon1 - (ol1Amount + ol2Amount));

            assertThat(stockPortion1.getAmountLeftInTon()).isEqualTo(endAmountSt1);
            assertThat(stockPortion2.getAmountLeftInTon()).isEqualTo(endAmountSt2);

            assertThat(stockPortion1.getAmountInTon()).isEqualTo(stockPortion1Amount);
            assertThat(stockPortion2.getAmountInTon()).isEqualTo(stockPortion2Amount);


            assertThat(stockPortion1.isPortionEmpty()).isEqualTo(true);
            assertThat(stockPortion2.isPortionEmpty()).isEqualTo(false);

            assertThat(purchaseOrder.getStatus()).isEqualTo(Status.COMPLETED);

            Mockito.verify(endOfPurchaseOrderPickUpPublisher, Mockito.times(1))
                    .publishEndOfPurchaseOrderPickUp(purchaseOrderNumber, endTime);
        }
    }

    @Test
    void processPurchaseOrderPickUp_Should_Have_Waiting_For_PO_Status_When_PurchaseOrder_Exists_And_Its_Lines_But_Not_Enough_StockPortions() {
        //ARRANGE
        ZonedDateTime stockPortion1Time = ZonedDateTime.of(2024, 2, 2, 2, 2, 2, 0, ZoneOffset.UTC);
        try (MockedStatic<ZonedDateTime> mockedStatic = mockStatic(ZonedDateTime.class)) {
            ZonedDateTime endTime = ZonedDateTime.of(2024, 11, 2, 2, 2, 2, 0, ZoneOffset.UTC);

            mockedStatic.when(ZonedDateTime::now).thenReturn(endTime);


            UUID vendorId = UUID.randomUUID();
            UUID resourceId1 = UUID.randomUUID();
            UUID warehouseId1 = UUID.randomUUID();

            UUID ol1 = UUID.randomUUID();
            UUID ol2 = UUID.randomUUID();

            UUID poId = UUID.randomUUID();
            double usedCapacityInTon1 = 50;

            double maxCapacityInTon = 200;
            double ol1Amount = 50;
            double ol2Amount = 50;

            String purchaseOrderNumber = "PO2345";
            String vesselNumber = "VE123456";
            Vendor vendorTest = new Vendor(vendorId, "johnson & Johnson");
            Resource resource1 = new Resource(resourceId1, "test description 1", "resourceName 1", 90, 12);
            double stockPortion1Amount = 50;

            double endAmountSt1 = stockPortion1Amount - ol1Amount;

            StockPortion stockPortion1 = new StockPortion(stockPortion1Amount, stockPortion1Time, 12);
            Warehouse warehouse1 = new Warehouse(warehouseId1, 1, usedCapacityInTon1, maxCapacityInTon);
            warehouse1.setStockPortions(new ArrayList<>(List.of(stockPortion1)));
            warehouse1.setVendor(vendorTest);
            warehouse1.setResource(resource1);

            OrderLine orderLine1 = new OrderLine(ol1Amount, false, ol1, 12, resource1);
            OrderLine orderLine2 = new OrderLine(ol2Amount, false, ol2, 12, resource1);
            List<OrderLine> orderLines = new ArrayList<>();
            orderLines.add(orderLine1);
            orderLines.add(orderLine2);
            PurchaseOrder purchaseOrder = new PurchaseOrder(poId, orderLines, purchaseOrderNumber, Status.OPEN, vendorTest, vesselNumber);


            Mockito.when(purchaseOrderRepository
                            .findByPurchaseOrderNumberAndVendorIdAndStatus(purchaseOrderNumber, vendorId, Status.OPEN))
                    .thenReturn(Optional.of(purchaseOrder));
            Mockito.when(purchaseOrderRepository.save(purchaseOrder)).thenReturn(purchaseOrder);

            Mockito.when(warehouseRepository.findFirstByVendorIdAndResourceId(vendorId, resourceId1))
                    .thenReturn(Optional.of(warehouse1));

            Mockito.when(warehouseRepository.save(warehouse1)).thenReturn(warehouse1);

            Mockito.doNothing().when(endOfPurchaseOrderPickUpPublisher)
                    .publishEndOfPurchaseOrderPickUp(purchaseOrderNumber, endTime);

            //ACT
            purchaseOrderPickUpService.processPurchaseOrderPickUp(purchaseOrderNumber, vendorId);
            //ASSERT
            assertThat(orderLine1.hasBeenCompleted()).isEqualTo(true);
            assertThat(orderLine2.hasBeenCompleted()).isEqualTo(false);

            assertThat(warehouse1.getUsedCapacityInTon()).isEqualTo(usedCapacityInTon1-ol1Amount);

            assertThat(stockPortion1.getAmountLeftInTon()).isEqualTo(endAmountSt1 );

            assertThat(stockPortion1.getAmountInTon()).isEqualTo(stockPortion1Amount);

            assertThat(stockPortion1.isPortionEmpty()).isEqualTo(true);

            assertThat(purchaseOrder.getStatus()).isEqualTo(Status.WAITING);

            Mockito.verify(endOfPurchaseOrderPickUpPublisher, Mockito.times(0))
                    .publishEndOfPurchaseOrderPickUp(purchaseOrderNumber, endTime);
        }
    }

    @Test
    void processPurchaseOrderPickUp_Should_Take_FIFO_Style_From_StockPortions_When_PurchaseOrder_Exists_And_Its_Lines_When_The_Amount_Taken_Is_More_Than_The_One_StockPortion
            () {
        //ARRANGE
        ZonedDateTime stockPortion1Time = ZonedDateTime.of(2024, 2, 2, 2, 2, 2, 0, ZoneOffset.UTC);
        ZonedDateTime stockPortion2Time = ZonedDateTime.of(2024, 2, 3, 2, 2, 2, 0, ZoneOffset.UTC);

        try (MockedStatic<ZonedDateTime> mockedStatic = mockStatic(ZonedDateTime.class)) {
            ZonedDateTime endTime = ZonedDateTime.of(2024, 2, 2, 2, 2, 2, 0, ZoneOffset.UTC);

            mockedStatic.when(ZonedDateTime::now).thenReturn(endTime);


            UUID vendorId = UUID.randomUUID();
            UUID resourceId1 = UUID.randomUUID();
            UUID warehouseId1 = UUID.randomUUID();

            UUID ol1 = UUID.randomUUID();

            UUID poId = UUID.randomUUID();
            double usedCapacityInTon1 = 130;

            double maxCapacityInTon = 200;
            double ol1Amount = 100;

            String purchaseOrderNumber = "PO2345";
            String vesselNumber = "VE123456";
            Vendor vendorTest = new Vendor(vendorId, "johnson & Johnson");
            Resource resource1 = new Resource(resourceId1, "test description 1", "resourceName 1", 90, 12);
            double stockPortion1Amount = 50;
            double stockPortion2Amount = 80;

            double endAmountSt1 = 0;
            double endAmountSt2 = (stockPortion2Amount + stockPortion1Amount) - ol1Amount;

            StockPortion stockPortion1 = new StockPortion(stockPortion1Amount, stockPortion1Time, 12);
            StockPortion stockPortion2 = new StockPortion(stockPortion2Amount, stockPortion2Time, 12);

            Warehouse warehouse1 = new Warehouse(warehouseId1, 1, usedCapacityInTon1, maxCapacityInTon);
            warehouse1.setStockPortions(new ArrayList<>(List.of(stockPortion1, stockPortion2)));
            warehouse1.setVendor(vendorTest);
            warehouse1.setResource(resource1);

            OrderLine orderLine1 = new OrderLine(ol1Amount, false, ol1, 12, resource1);
            List<OrderLine> orderLines = new ArrayList<>();
            orderLines.add(orderLine1);
            PurchaseOrder purchaseOrder = new PurchaseOrder(poId, orderLines, purchaseOrderNumber, Status.OPEN, vendorTest, vesselNumber);


            Mockito.when(purchaseOrderRepository
                            .findByPurchaseOrderNumberAndVendorIdAndStatus(purchaseOrderNumber, vendorId, Status.OPEN))
                    .thenReturn(Optional.of(purchaseOrder));
            Mockito.when(purchaseOrderRepository.save(purchaseOrder)).thenReturn(purchaseOrder);

            Mockito.when(warehouseRepository.findFirstByVendorIdAndResourceId(vendorId, resourceId1))
                    .thenReturn(Optional.of(warehouse1));

            Mockito.when(warehouseRepository.save(warehouse1)).thenReturn(warehouse1);

            Mockito.doNothing().when(endOfPurchaseOrderPickUpPublisher)
                    .publishEndOfPurchaseOrderPickUp(purchaseOrderNumber, endTime);

            //ACT
            purchaseOrderPickUpService.processPurchaseOrderPickUp(purchaseOrderNumber, vendorId);
            //ASSERT
            assertThat(orderLine1.hasBeenCompleted()).isEqualTo(true);

            assertThat(warehouse1.getUsedCapacityInTon()).isEqualTo(usedCapacityInTon1 - (ol1Amount));

            assertThat(stockPortion1.getAmountLeftInTon()).isEqualTo(endAmountSt1);
            assertThat(stockPortion2.getAmountLeftInTon()).isEqualTo(endAmountSt2);

            assertThat(stockPortion1.getAmountInTon()).isEqualTo(stockPortion1Amount);
            assertThat(stockPortion2.getAmountInTon()).isEqualTo(stockPortion2Amount);


            assertThat(stockPortion1.isPortionEmpty()).isEqualTo(true);
            assertThat(stockPortion2.isPortionEmpty()).isEqualTo(false);

            assertThat(purchaseOrder.getStatus()).isEqualTo(Status.COMPLETED);

            Mockito.verify(endOfPurchaseOrderPickUpPublisher, Mockito.times(1))
                    .publishEndOfPurchaseOrderPickUp(purchaseOrderNumber, endTime);
        }
    }

    @Test
    void processPurchaseOrderPickUp_Should_Throw_Exception_When_Warehouse_Does_Not_exist() {
        //ARRANGE
        ZonedDateTime stockPortion1Time = ZonedDateTime.of(2024, 2, 2, 2, 2, 2, 0, ZoneOffset.UTC);
        ZonedDateTime stockPortion2Time = ZonedDateTime.of(2024, 2, 3, 2, 2, 2, 0, ZoneOffset.UTC);


        UUID vendorId = UUID.randomUUID();
        UUID resourceId1 = UUID.randomUUID();
        UUID warehouseId1 = UUID.randomUUID();

        UUID ol1 = UUID.randomUUID();

        UUID poId = UUID.randomUUID();
        double usedCapacityInTon1 = 130;

        double maxCapacityInTon = 200;
        double ol1Amount = 100;

        String purchaseOrderNumber = "PO2345";
        String vesselNumber = "VE123456";
        Vendor vendorTest = new Vendor(vendorId, "johnson & Johnson");
        Resource resource1 = new Resource(resourceId1, "test description 1", "resourceName 1", 90, 12);
        double stockPortion1Amount = 50;
        double stockPortion2Amount = 80;

        StockPortion stockPortion1 = new StockPortion(stockPortion1Amount, stockPortion1Time, 12);
        StockPortion stockPortion2 = new StockPortion(stockPortion2Amount, stockPortion2Time, 12);

        Warehouse warehouse1 = new Warehouse(warehouseId1, 1, usedCapacityInTon1, maxCapacityInTon);
        warehouse1.setStockPortions(new ArrayList<>(List.of(stockPortion1, stockPortion2)));
        warehouse1.setVendor(vendorTest);
        warehouse1.setResource(resource1);

        OrderLine orderLine1 = new OrderLine(ol1Amount, false, ol1, 12, resource1);
        List<OrderLine> orderLines = new ArrayList<>();
        orderLines.add(orderLine1);
        PurchaseOrder purchaseOrder = new PurchaseOrder(poId, orderLines, purchaseOrderNumber, Status.OPEN, vendorTest, vesselNumber);


        Mockito.when(purchaseOrderRepository
                        .findByPurchaseOrderNumberAndVendorIdAndStatus(purchaseOrderNumber, vendorId, Status.OPEN))
                .thenReturn(Optional.of(purchaseOrder));
        Mockito.when(purchaseOrderRepository.save(purchaseOrder)).thenReturn(purchaseOrder);

        Mockito.when(warehouseRepository.findFirstByVendorIdAndResourceId(vendorId, resourceId1))
                .thenReturn(Optional.empty());

        //ACT
        // ASSERT

        assertThrows(NoItemFoundException.class, () ->
                purchaseOrderPickUpService.processPurchaseOrderPickUp(purchaseOrderNumber, vendorId));

        assertThat(orderLine1.hasBeenCompleted()).isEqualTo(false);

        assertThat(warehouse1.getUsedCapacityInTon()).isEqualTo(usedCapacityInTon1);

        assertThat(stockPortion1.getAmountLeftInTon()).isEqualTo(stockPortion1Amount);
        assertThat(stockPortion2.getAmountLeftInTon()).isEqualTo(stockPortion2Amount);

        assertThat(stockPortion1.getAmountInTon()).isEqualTo(stockPortion1Amount);
        assertThat(stockPortion2.getAmountInTon()).isEqualTo(stockPortion2Amount);


        assertThat(stockPortion1.isPortionEmpty()).isEqualTo(false);
        assertThat(stockPortion2.isPortionEmpty()).isEqualTo(false);

        assertThat(purchaseOrder.getStatus()).isEqualTo(Status.PENDING);
    }

    @Test
    void processPurchaseOrderPickUp_Should_Throw_Exception_When_PurchaseOrder_Does_Not_exist() {
        //ARRANGE
        ZonedDateTime stockPortion1Time = ZonedDateTime.of(2024, 2, 2, 2, 2, 2, 0, ZoneOffset.UTC);
        ZonedDateTime stockPortion2Time = ZonedDateTime.of(2024, 2, 3, 2, 2, 2, 0, ZoneOffset.UTC);


        UUID vendorId = UUID.randomUUID();
        UUID resourceId1 = UUID.randomUUID();
        UUID warehouseId1 = UUID.randomUUID();

        UUID ol1 = UUID.randomUUID();

        UUID poId = UUID.randomUUID();
        double usedCapacityInTon1 = 130;

        double maxCapacityInTon = 200;
        double ol1Amount = 100;

        String purchaseOrderNumber = "PO2345";
        String vesselNumber = "VE123456";
        Vendor vendorTest = new Vendor(vendorId, "johnson & Johnson");
        Resource resource1 = new Resource(resourceId1, "test description 1", "resourceName 1", 90, 12);
        double stockPortion1Amount = 50;
        double stockPortion2Amount = 80;

        StockPortion stockPortion1 = new StockPortion(stockPortion1Amount, stockPortion1Time, 12);
        StockPortion stockPortion2 = new StockPortion(stockPortion2Amount, stockPortion2Time, 12);

        Warehouse warehouse1 = new Warehouse(warehouseId1, 1, usedCapacityInTon1, maxCapacityInTon);
        warehouse1.setStockPortions(new ArrayList<>(List.of(stockPortion1, stockPortion2)));
        warehouse1.setVendor(vendorTest);
        warehouse1.setResource(resource1);

        OrderLine orderLine1 = new OrderLine(ol1Amount, false, ol1, 12, resource1);
        List<OrderLine> orderLines = new ArrayList<>();
        orderLines.add(orderLine1);
        PurchaseOrder purchaseOrder = new PurchaseOrder(poId, orderLines, purchaseOrderNumber, Status.OPEN, vendorTest, vesselNumber);


        Mockito.when(purchaseOrderRepository
                        .findByPurchaseOrderNumberAndVendorIdAndStatus(purchaseOrderNumber, vendorId, Status.OPEN))
                .thenReturn(Optional.empty());

        //ACT
        // ASSERT

        assertThrows(NoItemFoundException.class, () ->
                purchaseOrderPickUpService.processPurchaseOrderPickUp(purchaseOrderNumber, vendorId));

        assertThat(orderLine1.hasBeenCompleted()).isEqualTo(false);

        assertThat(warehouse1.getUsedCapacityInTon()).isEqualTo(usedCapacityInTon1);

        assertThat(stockPortion1.getAmountLeftInTon()).isEqualTo(stockPortion1Amount);
        assertThat(stockPortion2.getAmountLeftInTon()).isEqualTo(stockPortion2Amount);

        assertThat(stockPortion1.getAmountInTon()).isEqualTo(stockPortion1Amount);
        assertThat(stockPortion2.getAmountInTon()).isEqualTo(stockPortion2Amount);


        assertThat(stockPortion1.isPortionEmpty()).isEqualTo(false);
        assertThat(stockPortion2.isPortionEmpty()).isEqualTo(false);

        assertThat(purchaseOrder.getStatus()).isEqualTo(Status.OPEN);
    }
}
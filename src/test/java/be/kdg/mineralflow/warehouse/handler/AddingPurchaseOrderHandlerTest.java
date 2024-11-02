package be.kdg.mineralflow.warehouse.handler;

import be.kdg.mineralflow.warehouse.TestContainer;
import be.kdg.mineralflow.warehouse.business.domain.PurchaseOrder;
import be.kdg.mineralflow.warehouse.handler.purchase.order.AddingPurchaseOrderHandler;
import be.kdg.mineralflow.warehouse.persistence.purchase.order.PurchaseOrderRepository;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.purchase.order.OrderLineDto;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.PartyDto;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.purchase.order.PurchaseOrderDto;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AddingPurchaseOrderHandlerTest extends TestContainer {

    @Autowired
    private AddingPurchaseOrderHandler addingPurchaseOrderHandler;
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Test
    void addPurchaseOrder_should_add_purchase_order() {
        //ARRANGE
        PurchaseOrderDto dto = generatePurchaseOrderDto();
        List<PurchaseOrderDto> dtos = List.of(dto);
        //ACT
        addingPurchaseOrderHandler.addPurchaseOrder(dtos);

        //ASSERT
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
        assertThat(purchaseOrders).isNotNull().isNotEmpty();
        assertThat(purchaseOrders.stream().filter(purchaseOrder -> purchaseOrder.getPoNumber().equalsIgnoreCase(dto.poNumber())).findFirst()).isNotEmpty();
        PurchaseOrder result = purchaseOrders.stream().filter(purchaseOrder -> purchaseOrder.getPoNumber().equalsIgnoreCase(dto.poNumber())).findFirst().get();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getOrderLines().size()).isEqualTo(2);
        assertThat(result.getVendor().getName()).isEqualToIgnoringCase(dto.sellerParty().name());
        assertThat(result.getBuyer().getName()).isEqualToIgnoringCase(dto.customerParty().name());
        assertThat(result.getVesselNumber()).isEqualToIgnoringCase(dto.vesselNumber());
        assertThat(
                result.getOrderLines()
                        .stream().flatMap(orderLine ->
                                dto.orderLines().stream().map(orderLineDto ->
                                        orderLine.getResource().getName().equalsIgnoreCase(orderLineDto.description())
                                )
                        ).distinct().toList()
        ).contains(true);
    }
    @Transactional
    @Test
    void addPurchaseOrder_should_add_purchase_orders() {
        //ARRANGE
        PurchaseOrderDto dto = generatePurchaseOrderDto();
        List<PurchaseOrderDto> dtos = List.of(dto);
        //ACT
        addingPurchaseOrderHandler.addPurchaseOrder(dtos);

        //ASSERT
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
        assertThat(purchaseOrders).isNotNull().isNotEmpty();
        assertThat(purchaseOrders.stream().filter(purchaseOrder -> purchaseOrder.getPoNumber().equalsIgnoreCase(dto.poNumber())).findFirst()).isNotEmpty();
        PurchaseOrder result = purchaseOrders.stream().filter(purchaseOrder -> purchaseOrder.getPoNumber().equalsIgnoreCase(dto.poNumber())).findFirst().get();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getOrderLines().size()).isEqualTo(2);
        assertThat(result.getVendor().getName()).isEqualToIgnoringCase(dto.sellerParty().name());
        assertThat(result.getBuyer().getName()).isEqualToIgnoringCase(dto.customerParty().name());
        assertThat(result.getVesselNumber()).isEqualToIgnoringCase(dto.vesselNumber());
        assertThat(
                result.getOrderLines()
                        .stream().flatMap(orderLine ->
                                dto.orderLines().stream().map(orderLineDto ->
                                        orderLine.getResource().getName().equalsIgnoreCase(orderLineDto.description())
                                )
                        ).distinct().toList()
        ).contains(true);
    }

    @Test
    void addPurchaseOrder_should_not_add_purchase_order_when_customer_is_missing() {
        //ARRANGE
        PurchaseOrderDto full = generatePurchaseOrderDto();
        PurchaseOrderDto dto = new PurchaseOrderDto(
                full.poNumber(),
                full.referenceUUID(),
                null,
                full.sellerParty(),
                full.vesselNumber(),
                full.orderLines()
        );
        List<PurchaseOrderDto> dtos = List.of(dto);
        //ACT
        addingPurchaseOrderHandler.addPurchaseOrder(dtos);

        //ASSERT
        Optional<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findPurchaseOrderByPurchaseOrderNumber(full.poNumber());
        assertThat(purchaseOrders).isEmpty();
    }

    @Test
    void addPurchaseOrder_should_not_add_purchase_order_when_orderline_is_empty() {
        //ARRANGE
        PurchaseOrderDto full = generatePurchaseOrderDto();
        PurchaseOrderDto dto = new PurchaseOrderDto(
                full.poNumber(),
                full.referenceUUID(),
                full.customerParty(),
                full.sellerParty(),
                full.vesselNumber(),
                List.of(new OrderLineDto("", "", "", 0, ""))
        );
        List<PurchaseOrderDto> dtos = List.of(dto);
        //ACT
        addingPurchaseOrderHandler.addPurchaseOrder(dtos);

        //ASSERT
        Optional<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findPurchaseOrderByPurchaseOrderNumber(full.poNumber());
        assertThat(purchaseOrders).isEmpty();
    }

    @Test
    void addPurchaseOrder_should_not_add_purchase_order_when_vessekNumber_is_missing() {
        //ARRANGE
        PurchaseOrderDto full = generatePurchaseOrderDto();
        PurchaseOrderDto dto = new PurchaseOrderDto(
                full.poNumber(),
                full.referenceUUID(),
                full.customerParty(),
                full.sellerParty(),
                "",
                full.orderLines()
        );
        List<PurchaseOrderDto> dtos = List.of(dto);
        //ACT
        addingPurchaseOrderHandler.addPurchaseOrder(dtos);

        //ASSERT
        Optional<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findPurchaseOrderByPurchaseOrderNumber(full.poNumber());
        assertThat(purchaseOrders).isEmpty();
    }

    private static @NotNull PurchaseOrderDto generatePurchaseOrderDto() {
        String sellerUUID = "b33df3fe-71be-4c00-94fc-20f4b83dfe12";
        String buyerUUID = "56efaea4-953c-44bf-9f41-9700fffa2f28";
        return new PurchaseOrderDto(
                "PO7357",
                null,
                new PartyDto(
                        buyerUUID,
                        "buyer",
                        "somewhere over the rainbow"
                ),
                new PartyDto(
                        sellerUUID,
                        "seller",
                        "somewhere before the rainbow"
                ),
                "VES7357",
                List.of(new OrderLineDto(
                        "1",
                        "TM",
                        "TestMaterial",
                        100,
                        "t"
                ), new OrderLineDto(
                        "2",
                        "TM",
                        "TestMaterial",
                        10,
                        "kt"
                ))
        );
    }
}
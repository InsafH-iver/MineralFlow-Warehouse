package be.kdg.mineralflow.warehouse.business.service.purchase.order;

import be.kdg.mineralflow.warehouse.business.domain.OrderLine;
import be.kdg.mineralflow.warehouse.business.domain.PurchaseOrder;
import be.kdg.mineralflow.warehouse.business.domain.Status;
import be.kdg.mineralflow.warehouse.business.domain.Warehouse;
import be.kdg.mineralflow.warehouse.business.service.externalApi.EndOfPurchaseOrderPickUpPublisher;
import be.kdg.mineralflow.warehouse.business.util.ExceptionHandlingHelper;
import be.kdg.mineralflow.warehouse.business.util.provider.ZonedDateTimeProvider;
import be.kdg.mineralflow.warehouse.persistence.purchase.order.OrderLineRepository;
import be.kdg.mineralflow.warehouse.persistence.purchase.order.PurchaseOrderRepository;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UnfulfilledOrderLineService {

    private final OrderLineRepository orderLineRepository;
    private final OrderLineService orderLineService;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final EndOfPurchaseOrderPickUpPublisher endOfPurchaseOrderPickUpPublisher;
    private final ZonedDateTimeProvider zonedDateTimeProvider;

    public UnfulfilledOrderLineService(OrderLineRepository orderLineRepository, OrderLineService orderLineService, PurchaseOrderRepository purchaseOrderRepository, EndOfPurchaseOrderPickUpPublisher endOfPurchaseOrderPickUpPublisher, ZonedDateTimeProvider zonedDateTimeProvider) {
        this.orderLineRepository = orderLineRepository;
        this.orderLineService = orderLineService;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.endOfPurchaseOrderPickUpPublisher = endOfPurchaseOrderPickUpPublisher;
        this.zonedDateTimeProvider = zonedDateTimeProvider;
    }

    public void checkForUnfulfilledOrders(UUID resourceId, Warehouse warehouse) {
        List<OrderLine> unfulfilledOrderLines = orderLineRepository.findAllByResourceId(resourceId);
        unfulfilledOrderLines.forEach(orderLine -> reProcessOrderLine(orderLine, warehouse));
    }

    private void reProcessOrderLine(OrderLine orderLine, Warehouse warehouse) {
        PurchaseOrder purchaseOrder = findPurchaseOrderByOrderLineId(orderLine.getId());
        if (purchaseOrder.isNotWaiting()) return;

        updatePurchaseOrderStatusIfPending(purchaseOrder);
        boolean isOrderLineFulfilled = orderLineService.processOrderLine(orderLine, warehouse);
        orderLineRepository.save(orderLine);

        if (isOrderLineFulfilled) {
            updatePurchaseOrderStatusAndPublishIfComplete(purchaseOrder);
        }
    }

    private void updatePurchaseOrderStatusIfPending(PurchaseOrder purchaseOrder) {
            purchaseOrder.setStatus(Status.PENDING);
            purchaseOrderRepository.save(purchaseOrder);
    }

    private void updatePurchaseOrderStatusAndPublishIfComplete(PurchaseOrder purchaseOrder) {
        boolean allLinesFulfilled = purchaseOrder.areAllPurchaseOrderLinesFulfilled();
        purchaseOrder.updateStatusBasedOnFulfillment(allLinesFulfilled);
        purchaseOrderRepository.save(purchaseOrder);

        if (allLinesFulfilled) {
            ZonedDateTime endTime = zonedDateTimeProvider.now();
            endOfPurchaseOrderPickUpPublisher.publishEndOfPurchaseOrderPickUp(purchaseOrder.getPurchaseOrderNumber(), endTime);
        }
    }

    private PurchaseOrder findPurchaseOrderByOrderLineId(UUID orderLineId) {
        return purchaseOrderRepository.findByOrderLines_id(orderLineId)
                .orElseThrow(() -> ExceptionHandlingHelper.logAndThrowNotFound(
                        "No purchase order was found with order-line with id  %s",
                        orderLineId
                ));
    }
}

package be.kdg.mineralflow.warehouse.business.service.purchase.order;

import be.kdg.mineralflow.warehouse.business.domain.OrderLine;
import be.kdg.mineralflow.warehouse.business.domain.PurchaseOrder;
import be.kdg.mineralflow.warehouse.business.domain.Status;
import be.kdg.mineralflow.warehouse.business.domain.Warehouse;
import be.kdg.mineralflow.warehouse.business.service.externalApi.EndOfPurchaseOrderPickUpPublisher;
import be.kdg.mineralflow.warehouse.business.util.ExceptionHandlingHelper;
import be.kdg.mineralflow.warehouse.persistence.purchase.order.PurchaseOrderRepository;
import be.kdg.mineralflow.warehouse.persistence.WarehouseRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class PurchaseOrderPickUpService {
    public static final Logger logger = Logger
            .getLogger(PurchaseOrderPickUpService.class.getName());
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final WarehouseRepository warehouseRepository;
    private final OrderLineService orderLineService;
    private final EndOfPurchaseOrderPickUpPublisher endOfPurchaseOrderPickUpPublisher;

    public PurchaseOrderPickUpService(PurchaseOrderRepository purchaseOrderRepository, WarehouseRepository warehouseRepository, OrderLineService orderLineService, EndOfPurchaseOrderPickUpPublisher endOfPurchaseOrderPickUpPublisher) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.warehouseRepository = warehouseRepository;
        this.orderLineService = orderLineService;
        this.endOfPurchaseOrderPickUpPublisher = endOfPurchaseOrderPickUpPublisher;
    }

    @Transactional
    public void processPurchaseOrderPickUp(String purchaseOrderNumber, UUID vendorId) {
        logger.info(String.format("processing pick up for purchase order %s",
                purchaseOrderNumber));
        PurchaseOrder purchaseOrder = getPurchaseOrder(purchaseOrderNumber, vendorId);
        purchaseOrder.setStatus(Status.PENDING);
        boolean isFullyFulfilled = fulfillOrderLines(purchaseOrder, vendorId);
        saveStatusOfPurchaseOrder(purchaseOrder, isFullyFulfilled);
        processEndOfCargoIfFulfilled(isFullyFulfilled, purchaseOrderNumber);
    }

    private boolean fulfillOrderLines(PurchaseOrder purchaseOrder, UUID vendorId) {
        return purchaseOrder.getOrderLines()
                .stream()
                .allMatch(orderLine -> processOrderLine(orderLine, vendorId));
    }

    private boolean processOrderLine(OrderLine orderLine, UUID vendorId) {
        Warehouse warehouse = getWarehouse(orderLine.getResource().getId(), vendorId);
        boolean isFulfilled = orderLineService.processOrderLine(orderLine, warehouse);
        warehouseRepository.save(warehouse);
        return isFulfilled;
    }

    private void saveStatusOfPurchaseOrder(PurchaseOrder purchaseOrder, boolean isFullyFulfilled) {
        purchaseOrder.updateStatusBasedOnFulfillment(isFullyFulfilled);
        purchaseOrderRepository.save(purchaseOrder);
    }

    private void processEndOfCargoIfFulfilled(boolean isFulfilled, String purchaseOrderNumber) {
        if (isFulfilled) {
            logger.info(String.format("Purchase order %s was successfully completed",
                    purchaseOrderNumber));
            endOfPurchaseOrderPickUpPublisher.publishEndOfPurchaseOrderPickUp(purchaseOrderNumber, ZonedDateTime.now());
        } else {
            logger.info(String.format("Purchase order %s wasn't successfully completed",
                    purchaseOrderNumber));
        }
    }

    private Warehouse getWarehouse(UUID resourceId, UUID vendorId) {
        return warehouseRepository.findFirstByVendorIdAndResourceId(vendorId, resourceId)
                .orElseThrow(() -> ExceptionHandlingHelper.logAndThrowNotFound(
                        "No warehouse found for vendor ID %s with resource ID %s",
                        vendorId, resourceId
                ));
    }

    private PurchaseOrder getPurchaseOrder(String purchaseOrderNumber, UUID vendorId) {
        return purchaseOrderRepository
                .findByPurchaseOrderNumberAndVendorIdAndStatus(purchaseOrderNumber, vendorId, Status.OPEN)
                .orElseThrow(() -> ExceptionHandlingHelper.logAndThrowNotFound(
                        "No purchase-order was found with po-number %s and vendor with id %s",
                        purchaseOrderNumber, vendorId
                ));
    }
}

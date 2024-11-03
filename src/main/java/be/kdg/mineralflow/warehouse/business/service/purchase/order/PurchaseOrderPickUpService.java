package be.kdg.mineralflow.warehouse.business.service.purchase.order;

import be.kdg.mineralflow.warehouse.business.domain.OrderLine;
import be.kdg.mineralflow.warehouse.business.domain.PurchaseOrder;
import be.kdg.mineralflow.warehouse.business.domain.Status;
import be.kdg.mineralflow.warehouse.business.domain.Warehouse;
import be.kdg.mineralflow.warehouse.business.service.CommissionService;
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
    private final CommissionService commissionService;

    public PurchaseOrderPickUpService(PurchaseOrderRepository purchaseOrderRepository, WarehouseRepository warehouseRepository, OrderLineService orderLineService, EndOfPurchaseOrderPickUpPublisher endOfPurchaseOrderPickUpPublisher, CommissionService commissionService) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.warehouseRepository = warehouseRepository;
        this.orderLineService = orderLineService;
        this.endOfPurchaseOrderPickUpPublisher = endOfPurchaseOrderPickUpPublisher;
        this.commissionService = commissionService;
    }

    @Transactional
    public void processPurchaseOrderPickUp(String purchaseOrderNumber, UUID vendorId) {
        logger.info(String.format("processing pick up for purchase order %s",
                purchaseOrderNumber));
        PurchaseOrder purchaseOrder = getPurchaseOrder(purchaseOrderNumber, vendorId);
        purchaseOrder.setStatus(Status.PENDING);
        boolean isFullyFulfilled = fulfillOrderLines(purchaseOrder, vendorId);
        saveStatusOfPurchaseOrder(purchaseOrder, isFullyFulfilled);
        processEndOfPurchaseOrderPickup(isFullyFulfilled, purchaseOrder);
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

    private void processEndOfPurchaseOrderPickup(boolean isFulfilled, PurchaseOrder purchaseOrder) {
        if (isFulfilled) {
            logger.info(String.format("Purchase order %s was successfully completed",
                    purchaseOrder.getPurchaseOrderNumber()));
            commissionService.createAndSaveCommissionForPurchaseOrder(purchaseOrder);
            endOfPurchaseOrderPickUpPublisher.publishEndOfPurchaseOrderPickUp(purchaseOrder.getPurchaseOrderNumber(), ZonedDateTime.now());
        } else {
            logger.info(String.format("Purchase order %s wasn't successfully completed",
                    purchaseOrder.getPurchaseOrderNumber()));
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

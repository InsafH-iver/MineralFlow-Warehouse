package be.kdg.mineralflow.warehouse.business.service.purchase.order;

import be.kdg.mineralflow.warehouse.business.domain.PurchaseOrder;
import be.kdg.mineralflow.warehouse.business.domain.Status;
import be.kdg.mineralflow.warehouse.business.domain.Warehouse;
import be.kdg.mineralflow.warehouse.persistence.purchase.order.PurchaseOrderRepository;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.purchase.order.PurchaseOrderStatusDto;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.purchase.order.PurchaseOrdersOverviewDto;
import be.kdg.mineralflow.warehouse.presentation.controller.mapper.PurchaseOrderStatusMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PurchaseOrderStatusService {
    public static final Logger logger = Logger
            .getLogger(PurchaseOrderStatusService.class.getName());

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderStatusMapper purchaseOrderStatusMapper = PurchaseOrderStatusMapper.INSTANCE;

    public PurchaseOrderStatusService(PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    public PurchaseOrdersOverviewDto getAllPurchaseOrderPerStatus() {
        logger.info("Starting process to fetch all purchase orders per status");

        PurchaseOrdersOverviewDto purchaseOrdersOverviewDto =
                new PurchaseOrdersOverviewDto(
                        getAllOpenPurchaseOrders(),
                        getAllCompletedPurchaseOrders()
                );

        logger.info("Successfully fetched all purchase orders per status");
        return purchaseOrdersOverviewDto;
    }

    private List<PurchaseOrderStatusDto> getAllCompletedPurchaseOrders() {
        List<PurchaseOrder> inspectionOperations = purchaseOrderRepository
                .findAllByStatus(Status.COMPLETED);
        return purchaseOrderStatusMapper.mapPurchaseOrders(inspectionOperations);
    }

    private List<PurchaseOrderStatusDto> getAllOpenPurchaseOrders() {
        List<PurchaseOrder> inspectionOperations = purchaseOrderRepository
                .findAllByStatusNot(Status.COMPLETED);
        return purchaseOrderStatusMapper.mapPurchaseOrders(inspectionOperations);
    }
}

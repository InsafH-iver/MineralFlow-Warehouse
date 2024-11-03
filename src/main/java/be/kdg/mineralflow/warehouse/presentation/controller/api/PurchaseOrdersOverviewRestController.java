package be.kdg.mineralflow.warehouse.presentation.controller.api;

import be.kdg.mineralflow.warehouse.business.service.purchase.order.PurchaseOrderStatusService;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.purchase.order.PurchaseOrdersOverviewDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/purchase-orders-overview")
public class PurchaseOrdersOverviewRestController {
    public static final Logger logger = Logger
            .getLogger(PurchaseOrdersOverviewRestController.class.getName());

    private final PurchaseOrderStatusService purchaseOrderStatusService;

    public PurchaseOrdersOverviewRestController(PurchaseOrderStatusService purchaseOrderStatusService) {
        this.purchaseOrderStatusService = purchaseOrderStatusService;
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public PurchaseOrdersOverviewDto getPurchaseOrdersOverview() {
        logger.info("Call was made to fetch status of all purchase orders per status");
        return purchaseOrderStatusService.getAllPurchaseOrderPerStatus();
    }
}

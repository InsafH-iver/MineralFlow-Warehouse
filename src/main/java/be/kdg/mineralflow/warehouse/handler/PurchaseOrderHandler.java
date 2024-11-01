package be.kdg.mineralflow.warehouse.handler;

import be.kdg.mineralflow.warehouse.business.service.PurchaseOrderService;
import be.kdg.mineralflow.warehouse.business.service.PurchaseOrderValidationService;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.PurchaseOrderDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class PurchaseOrderHandler {
    public static final Logger logger = Logger
            .getLogger(PurchaseOrderHandler.class.getName());
    private final PurchaseOrderService purchaseOrderService;
    private final PurchaseOrderValidationService purchaseOrderValidationService;

    public PurchaseOrderHandler(PurchaseOrderService purchaseOrderService, PurchaseOrderValidationService purchaseOrderValidationService) {
        this.purchaseOrderService = purchaseOrderService;
        this.purchaseOrderValidationService = purchaseOrderValidationService;
    }


    @RabbitListener(queues = "#{@rabbitConfigProperties.addPurchaseOrderQueue}")
    public void addPurchaseOrder(PurchaseOrderDto purchaseOrderDto){
        try {
            purchaseOrderValidationService.validate(purchaseOrderDto);

            logger.info(String.format("addPurchaseOrder was called with dto: %s",purchaseOrderDto));
            purchaseOrderService.addPurchaseOrder(purchaseOrderDto);
        }catch (IllegalArgumentException illegalArgumentException){
            logger.severe(illegalArgumentException.getMessage());
        }
    }
}

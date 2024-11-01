package be.kdg.mineralflow.warehouse.handler;

import be.kdg.mineralflow.warehouse.business.service.PurchaseOrderService;
import be.kdg.mineralflow.warehouse.config.messaging.RabbitConfigProperties;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.PurchaseOrderDto;
import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class PurchaseOrderHandler {
    public static final Logger logger = Logger
            .getLogger(PurchaseOrderHandler.class.getName());
    private final PurchaseOrderService purchaseOrderService;

    public PurchaseOrderHandler(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }


    @RabbitListener(queues = "#{@rabbitConfigProperties.addPurchaseOrderQueue}")
    public void addPurchaseOrder(@Valid PurchaseOrderDto purchaseOrderDto){
        logger.info(String.format("addPurchaseOrder was called with dto: %s",purchaseOrderDto));
        purchaseOrderService.addPurchaseOrder(purchaseOrderDto);
    }
}

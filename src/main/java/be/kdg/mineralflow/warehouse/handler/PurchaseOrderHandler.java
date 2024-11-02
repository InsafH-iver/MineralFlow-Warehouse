package be.kdg.mineralflow.warehouse.handler;

import be.kdg.mineralflow.warehouse.business.service.PurchaseOrderService;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.PurchaseOrderDto;
import jakarta.validation.ConstraintViolationException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
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
    public void addPurchaseOrder(List<PurchaseOrderDto> purchaseOrderDtos){
        try {
            logger.info("addPurchaseOrder was called");
            purchaseOrderDtos.forEach(purchaseOrderService::addPurchaseOrder);

        }catch (Exception e){
            logger.severe(e.getMessage());
        }
    }
}

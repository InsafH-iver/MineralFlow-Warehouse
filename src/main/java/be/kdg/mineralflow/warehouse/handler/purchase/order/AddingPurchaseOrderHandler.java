package be.kdg.mineralflow.warehouse.handler.purchase.order;

import be.kdg.mineralflow.warehouse.business.service.purchase.order.PurchaseOrderService;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.purchase.order.PurchaseOrderDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Component
public class AddingPurchaseOrderHandler {
    public static final Logger logger = Logger
            .getLogger(AddingPurchaseOrderHandler.class.getName());
    private final PurchaseOrderService purchaseOrderService;

    public AddingPurchaseOrderHandler(PurchaseOrderService purchaseOrderService) {
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

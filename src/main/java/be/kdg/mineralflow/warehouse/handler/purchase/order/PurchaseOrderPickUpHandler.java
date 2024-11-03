package be.kdg.mineralflow.warehouse.handler.purchase.order;

import be.kdg.mineralflow.warehouse.business.service.purchase.order.PurchaseOrderPickUpService;
import be.kdg.mineralflow.warehouse.config.messaging.RabbitConfigProperties;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.purchase.order.PurchaseOrderPickupDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class PurchaseOrderPickUpHandler {
    public static final Logger logger = Logger
            .getLogger(PurchaseOrderPickUpHandler.class.getName());

    private final RabbitConfigProperties rabbitConfigProperties;
    private final PurchaseOrderPickUpService purchaseOrderPickUpService;


    public PurchaseOrderPickUpHandler(RabbitConfigProperties rabbitConfigProperties, PurchaseOrderPickUpService purchaseOrderPickUpService) {
        this.rabbitConfigProperties = rabbitConfigProperties;
        this.purchaseOrderPickUpService = purchaseOrderPickUpService;
    }

    @RabbitListener(queues = "#{@rabbitConfigProperties.purchaseOrderPickUpQueue}")
    public void purchaseOrderPickUp(PurchaseOrderPickupDto dto) {
        try {
            logger.info(String.format(
                    "Cargo pick-up event received for purchase order: %s (from vendor %s)",
                    dto.poNumber(), dto.vendorId()));

            purchaseOrderPickUpService.processPurchaseOrderPickUp(dto.poNumber(), dto.vendorId());
        } catch (Exception e) {
            logger.severe(String.format("Something went wrong while processing purchase order pick-up: %s"
                    , e.getMessage()));
        }
    }

}

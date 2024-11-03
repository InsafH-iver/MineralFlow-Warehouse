package be.kdg.mineralflow.warehouse.business.service.externalApi;

import be.kdg.mineralflow.warehouse.config.messaging.RabbitConfigProperties;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.purchase.order.EndOfPurchaseOrderPickupDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

@Service
public class EndOfPurchaseOrderPickUpPublisher {
    public static final Logger logger = Logger
            .getLogger(EndOfPurchaseOrderPickUpPublisher.class.getName());
    private final RabbitTemplate rabbitTemplate;
    private final RabbitConfigProperties configProperties;

    public EndOfPurchaseOrderPickUpPublisher(RabbitTemplate rabbitTemplate, RabbitConfigProperties configProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.configProperties = configProperties;
    }

    public void publishEndOfPurchaseOrderPickUp(String  purchaseOrderNumber, ZonedDateTime timeOfEndOfCargo) {
        var dto = new EndOfPurchaseOrderPickupDto(purchaseOrderNumber, timeOfEndOfCargo);
        rabbitTemplate.convertAndSend(configProperties.getWaterExchangeName(),
                configProperties.getPublisherEndOfPurchaseOrderPickUpRoutingKey(), dto);
        logger.info(String.format("Event end of cargo pick-up for purchase order %s has been published at %s",
                purchaseOrderNumber,timeOfEndOfCargo.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)));
    }
}

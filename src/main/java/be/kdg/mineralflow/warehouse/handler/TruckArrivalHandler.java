package be.kdg.mineralflow.warehouse.handler;

import be.kdg.mineralflow.warehouse.business.service.warehouse.StockPortionDeliveryService;
import be.kdg.mineralflow.warehouse.config.messaging.RabbitConfigProperties;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.TruckArrivalAtWarehouseDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class TruckArrivalHandler {
    public static final Logger logger = Logger
            .getLogger(TruckArrivalHandler.class.getName());

    private final RabbitConfigProperties rabbitConfigProperties;
    private final StockPortionDeliveryService stockPortionDeliveryService;

    public TruckArrivalHandler(RabbitConfigProperties rabbitConfigProperties, StockPortionDeliveryService stockPortionDeliveryService) {
        this.rabbitConfigProperties = rabbitConfigProperties;
        this.stockPortionDeliveryService = stockPortionDeliveryService;
    }


    @RabbitListener(queues = "#{@rabbitConfigProperties.truckDepartureFromWeighingBridgeQueue}")
    public void truckArrival(TruckArrivalAtWarehouseDto dto) {
        try {
            logger.info(String.format(
                    "Truck arrival event received: Vendor ID: %s, Resource ID: %s, unloading-request-id: %s, Weight: %s",
                    dto.vendorId(), dto.resourceId(), dto.unloadingRequestId(), dto.weight()));
            stockPortionDeliveryService.handleStockPortionAtDelivery(
                    dto.vendorId(), dto.resourceId(), dto.weight(), dto.unloadingRequestId(), dto.endWeightTime());
        } catch (Exception e) {
            logger.severe(String.format("Something went wrong while handling stock-portion at delivery: %s"
                    , e.getMessage()));
        }
    }

}

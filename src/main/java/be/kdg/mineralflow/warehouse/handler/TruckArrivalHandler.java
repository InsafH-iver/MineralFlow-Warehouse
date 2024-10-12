package be.kdg.mineralflow.warehouse.handler;

import be.kdg.mineralflow.warehouse.config.messaging.RabbitConfigProperties;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.TruckArrivalAtWarehouseDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TruckArrivalHandler {

    private final RabbitConfigProperties rabbitConfigProperties;

    public TruckArrivalHandler(RabbitConfigProperties rabbitConfigProperties) {
        this.rabbitConfigProperties = rabbitConfigProperties;
    }

    @RabbitListener(queues = "#{@rabbitConfigProperties.truckArrivalAtWarehouseQueue}")
    public void truckArrival(TruckArrivalAtWarehouseDto dto) {
        System.out.println("HELLO QUEUE ------------" + dto.toString()+"---------------------");
    }

}

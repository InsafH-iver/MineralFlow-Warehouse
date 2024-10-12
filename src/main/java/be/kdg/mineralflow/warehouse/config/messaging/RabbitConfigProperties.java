package be.kdg.mineralflow.warehouse.config.messaging;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "messaging")
public class RabbitConfigProperties {
    private String exchangeName;
    private String truckArrivalAtWarehouseRoutingKey;
    private String truckArrivalAtWarehouseQueue;

    public String getTruckArrivalAtWarehouseQueue() {
        return truckArrivalAtWarehouseQueue;
    }

    public String getTruckArrivalAtWarehouseRoutingKey() {
        return truckArrivalAtWarehouseRoutingKey;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public void setTruckArrivalAtWarehouseRoutingKey(String truckArrivalAtWarehouseRoutingKey) {
        this.truckArrivalAtWarehouseRoutingKey = truckArrivalAtWarehouseRoutingKey;
    }

    public void setTruckArrivalAtWarehouseQueue(String truckArrivalAtWarehouseQueue) {
        this.truckArrivalAtWarehouseQueue = truckArrivalAtWarehouseQueue;
    }
}

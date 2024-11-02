package be.kdg.mineralflow.warehouse.config.messaging;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "messaging")
public class RabbitConfigProperties {
    private String exchangeName;
    private String truckDepartureFromWeighingBridgeRoutingKey;
    private String truckDepartureFromWeighingBridgeQueue;
    private String addPurchaseOrderRoutingKey;
    private String addPurchaseOrderQueue;

    public String getTruckDepartureFromWeighingBridgeQueue() {
        return truckDepartureFromWeighingBridgeQueue;
    }

    public String getTruckDepartureFromWeighingBridgeRoutingKey() {
        return truckDepartureFromWeighingBridgeRoutingKey;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public void setTruckDepartureFromWeighingBridgeRoutingKey(String truckDepartureFromWeighingBridgeRoutingKey) {
        this.truckDepartureFromWeighingBridgeRoutingKey = truckDepartureFromWeighingBridgeRoutingKey;
    }

    public void setTruckDepartureFromWeighingBridgeQueue(String truckDepartureFromWeighingBridgeQueue) {
        this.truckDepartureFromWeighingBridgeQueue = truckDepartureFromWeighingBridgeQueue;
    }

    public String getAddPurchaseOrderRoutingKey() {
        return addPurchaseOrderRoutingKey;
    }

    public void setAddPurchaseOrderRoutingKey(String addPurchaseOrderRoutingKey) {
        this.addPurchaseOrderRoutingKey = addPurchaseOrderRoutingKey;
    }

    public String getAddPurchaseOrderQueue() {
        return addPurchaseOrderQueue;
    }

    public void setAddPurchaseOrderQueue(String addPurchaseOrderQueue) {
        this.addPurchaseOrderQueue = addPurchaseOrderQueue;
    }
}

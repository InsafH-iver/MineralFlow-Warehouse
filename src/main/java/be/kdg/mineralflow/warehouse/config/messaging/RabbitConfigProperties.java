package be.kdg.mineralflow.warehouse.config.messaging;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "messaging")
public class RabbitConfigProperties {
    private String warehouseExchangeName;
    private String waterExchangeName;
    // publishers routing keys
    private String publisherEndOfPurchaseOrderPickUpRoutingKey;
    // queues and keys
    private String purchaseOrderPickUpQueue;
    private String purchaseOrderPickUpRoutingKey;

    private String addPurchaseOrderQueue;
    private String addPurchaseOrderRoutingKey;

    private String truckDepartureFromWeighingBridgeQueue;
    private String truckDepartureFromWeighingBridgeRoutingKey;

    public String getPublisherEndOfPurchaseOrderPickUpRoutingKey() {
        return publisherEndOfPurchaseOrderPickUpRoutingKey;
    }

    public String getAddPurchaseOrderQueue() {
        return addPurchaseOrderQueue;
    }

    public String getAddPurchaseOrderRoutingKey() {
        return addPurchaseOrderRoutingKey;
    }

    public String getPurchaseOrderPickUpQueue() {
        return purchaseOrderPickUpQueue;
    }

    public String getPurchaseOrderPickUpRoutingKey() {
        return purchaseOrderPickUpRoutingKey;
    }

    public String getTruckDepartureFromWeighingBridgeQueue() {
        return truckDepartureFromWeighingBridgeQueue;
    }

    public String getTruckDepartureFromWeighingBridgeRoutingKey() {
        return truckDepartureFromWeighingBridgeRoutingKey;
    }

    public String getWarehouseExchangeName() {
        return warehouseExchangeName;
    }

    public String getWaterExchangeName() {
        return waterExchangeName;
    }

    public void setPublisherEndOfPurchaseOrderPickUpRoutingKey(String publisherEndOfPurchaseOrderPickUpRoutingKey) {
        this.publisherEndOfPurchaseOrderPickUpRoutingKey = publisherEndOfPurchaseOrderPickUpRoutingKey;
    }

    public void setAddPurchaseOrderQueue(String addPurchaseOrderQueue) {
        this.addPurchaseOrderQueue = addPurchaseOrderQueue;
    }

    public void setAddPurchaseOrderRoutingKey(String addPurchaseOrderRoutingKey) {
        this.addPurchaseOrderRoutingKey = addPurchaseOrderRoutingKey;
    }

    public void setPurchaseOrderPickUpQueue(String purchaseOrderPickUpQueue) {
        this.purchaseOrderPickUpQueue = purchaseOrderPickUpQueue;
    }

    public void setPurchaseOrderPickUpRoutingKey(String purchaseOrderPickUpRoutingKey) {
        this.purchaseOrderPickUpRoutingKey = purchaseOrderPickUpRoutingKey;
    }

    public void setTruckDepartureFromWeighingBridgeQueue(String truckDepartureFromWeighingBridgeQueue) {
        this.truckDepartureFromWeighingBridgeQueue = truckDepartureFromWeighingBridgeQueue;
    }

    public void setTruckDepartureFromWeighingBridgeRoutingKey(String truckDepartureFromWeighingBridgeRoutingKey) {
        this.truckDepartureFromWeighingBridgeRoutingKey = truckDepartureFromWeighingBridgeRoutingKey;
    }

    public void setWarehouseExchangeName(String warehouseExchangeName) {
        this.warehouseExchangeName = warehouseExchangeName;
    }

    public void setWaterExchangeName(String waterExchangeName) {
        this.waterExchangeName = waterExchangeName;
    }
}

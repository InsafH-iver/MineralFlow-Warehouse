package be.kdg.mineralflow.warehouse.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "company")
public class ConfigProperties {
    private double warehouseMaxCapacityInTon;
    private double warehouseMaxCapacityThreshold;
    private double commissionPart;

    public double getWarehouseMaxCapacityInTon() {
        return warehouseMaxCapacityInTon;
    }

    public void setWarehouseMaxCapacityInTon(double warehouseMaxCapacityInTon) {
        this.warehouseMaxCapacityInTon = warehouseMaxCapacityInTon;
    }

    public double getWarehouseMaxCapacityThreshold() {
        return warehouseMaxCapacityThreshold;
    }

    public void setWarehouseMaxCapacityThreshold(double warehouseMaxCapacityThreshold) {
        this.warehouseMaxCapacityThreshold = warehouseMaxCapacityThreshold;
    }

    public double getCommissionPart() {
        return commissionPart;
    }

    public void setCommissionPart(double commissionPart) {
        this.commissionPart = commissionPart;
    }
}

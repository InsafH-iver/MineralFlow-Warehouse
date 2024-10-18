package be.kdg.mineralflow.warehouse.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "company")
public class ConfigProperties {
    public double warehouseMaxCapacityInTon;

    public double getWarehouseMaxCapacityInTon() {
        return warehouseMaxCapacityInTon;
    }

    public void setWarehouseMaxCapacityInTon(double warehouseMaxCapacityInTon) {
        this.warehouseMaxCapacityInTon = warehouseMaxCapacityInTon;
    }
}

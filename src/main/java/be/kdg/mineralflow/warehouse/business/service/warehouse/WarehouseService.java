package be.kdg.mineralflow.warehouse.business.service.warehouse;

import be.kdg.mineralflow.warehouse.business.domain.Warehouse;
import be.kdg.mineralflow.warehouse.business.util.ExceptionHandlingHelper;
import be.kdg.mineralflow.warehouse.persistence.WarehouseRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.logging.Logger;

@Service
public class WarehouseService {
    public static final Logger logger = Logger
            .getLogger(WarehouseService.class.getName());
    private final WarehouseRepository warehouseRepository;

    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public int getWarehouseNumberByVendorAndResourceId(UUID vendorId, UUID resourceId) {
        logger.info(String.format("Getting warehouse number by vendor id %s and resource id %s", vendorId, resourceId));
        Warehouse warehouse = getWarehouse(vendorId, resourceId);
        int warehouseNumber = warehouse.getWarehouseNumber();
        logger.info(String.format("Returning warehouse number %d successfully", warehouseNumber));
        return warehouseNumber;
    }

    private Warehouse getWarehouse(UUID vendorId, UUID resourceId) {
        return warehouseRepository.findFirstByVendorIdAndResourceId(vendorId, resourceId)
                .orElseThrow(() -> ExceptionHandlingHelper.logAndThrowNotFound(
                        "No warehouse found for vendor ID %s with resource ID %s",
                        vendorId, resourceId
                ));
    }
}

package be.kdg.mineralflow.warehouse.business.service.warehouse;

import be.kdg.mineralflow.warehouse.business.domain.Resource;
import be.kdg.mineralflow.warehouse.business.domain.Vendor;
import be.kdg.mineralflow.warehouse.business.domain.Warehouse;
import be.kdg.mineralflow.warehouse.business.util.ExceptionHandlingHelper;
import be.kdg.mineralflow.warehouse.config.ConfigProperties;
import be.kdg.mineralflow.warehouse.persistence.ResourceRepository;
import be.kdg.mineralflow.warehouse.persistence.VendorRepository;
import be.kdg.mineralflow.warehouse.persistence.WarehouseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class WarehouseCapacityService {
    public static final Logger logger = Logger
            .getLogger(WarehouseCapacityService.class.getName());
    private final WarehouseRepository warehouseRepository;
    private final VendorRepository vendorRepository;
    private final ResourceRepository resourceRepository;
    private final ConfigProperties configProperties;

    public WarehouseCapacityService(WarehouseRepository warehouseRepository, VendorRepository vendorRepository, ResourceRepository resourceRepository, ConfigProperties configProperties) {
        this.warehouseRepository = warehouseRepository;
        this.vendorRepository = vendorRepository;
        this.resourceRepository = resourceRepository;
        this.configProperties = configProperties;
    }

    public boolean isWarehouseFull(UUID vendorId, UUID resourceId) {
        logger.info(String.format("Checking if warehouse %s is full", vendorId));
        Vendor vendor = getVendor(vendorId);

        Resource resource = getResource(resourceId);

        List<Warehouse> warehouse = warehouseRepository.findAllByVendorIdAndResourceId(vendor.getId(), resource.getId());
        for (Warehouse w : warehouse) {
            if (!w.isFull(configProperties.getWarehouseMaxCapacityThreshold())) return false;
        }
        return true;
    }

    private Vendor getVendor(UUID vendorId) {
        return vendorRepository.findById(vendorId)
                .orElseThrow(() -> ExceptionHandlingHelper.logAndThrowNotFound(
                        "Vendor with id %s, was not found",
                        vendorId
                ));
    }

    private Resource getResource(UUID resourceId) {
        return resourceRepository.findById(resourceId)
                .orElseThrow(() -> ExceptionHandlingHelper.logAndThrowNotFound(
                        "Resource with id %s, was not found",
                        resourceId
                ));
    }
}

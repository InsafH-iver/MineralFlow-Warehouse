package be.kdg.mineralflow.warehouse.business.service;

import be.kdg.mineralflow.warehouse.business.domain.Resource;
import be.kdg.mineralflow.warehouse.business.domain.Vendor;
import be.kdg.mineralflow.warehouse.business.domain.Warehouse;
import be.kdg.mineralflow.warehouse.config.ConfigProperties;
import be.kdg.mineralflow.warehouse.exception.NoItemFoundException;
import be.kdg.mineralflow.warehouse.persistence.ResourceRepository;
import be.kdg.mineralflow.warehouse.persistence.VendorRepository;
import be.kdg.mineralflow.warehouse.persistence.WarehouseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        Optional<Vendor> optionalVendor = vendorRepository.findById(vendorId);
        if (optionalVendor.isEmpty()) {
            String messageException = String.format("Vendor with id %s, was not found", vendorId);
            logger.severe(messageException);
            throw new NoItemFoundException(messageException);
        }

        Optional<Resource> optionalResource = resourceRepository.findById(resourceId);
        if (optionalResource.isEmpty()) {
            String messageException = String.format("Resource with id %s, was not found", resourceId);
            logger.severe(messageException);
            throw new NoItemFoundException(messageException);
        }
        List<Warehouse> warehouse = warehouseRepository.findAllByVendorIdAndResourceId(optionalVendor.get().getId(),optionalResource.get().getId());
        for (Warehouse w : warehouse) {
            if (!w.isFull(configProperties.getWarehouseMaxCapacityThreshold())) return false;
        }
        return true;
    }
}

package be.kdg.mineralflow.warehouse.business.service;

import be.kdg.mineralflow.warehouse.business.domain.Resource;
import be.kdg.mineralflow.warehouse.business.domain.Vendor;
import be.kdg.mineralflow.warehouse.business.domain.Warehouse;
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
public class WarehouseService {
    public static final Logger logger = Logger
            .getLogger(WarehouseService.class.getName());
    private final WarehouseRepository warehouseRepository;
    private final VendorRepository vendorRepository;
    private final ResourceRepository resourceRepository;

    public WarehouseService(WarehouseRepository warehouseRepository, VendorRepository vendorRepository, ResourceRepository resourceRepository) {
        this.warehouseRepository = warehouseRepository;
        this.vendorRepository = vendorRepository;
        this.resourceRepository = resourceRepository;
    }

    public int getWarehouseNumberByVendorAndResourceId(UUID vendorId, UUID resourceId) {
        logger.info(String.format("Getting warehouse number by vendor id %s and resource id %s", vendorId, resourceId));
        Optional<Warehouse> optionalWarehouse = warehouseRepository.findFirstByVendorIdAndResourceId(vendorId, resourceId);
        if (optionalWarehouse.isEmpty()) {
            String messageException = String.format("The warehouse of vendor with id %s and for resource with id %s, was not found", vendorId, resourceId);
            logger.severe(messageException);
            throw new NoItemFoundException(messageException);
        }
        int warehouseNumber = optionalWarehouse.get().getWarehouseNumber();
        logger.info(String.format("Returning warehouse number %d successfully", warehouseNumber));
        return warehouseNumber;
    }

    public boolean isWarehouseFull(String vendorName, String resourceName) {
        logger.info(String.format("Checking if warehouse %s is full", vendorName));
        Vendor vendor = vendorRepository.findByName(vendorName);
        Resource resource = resourceRepository.findByName(resourceName);
        List<Warehouse> warehouse = warehouseRepository.findAllByVendorIdAndResourceId(vendor.getId(),resource.getId());
        for (Warehouse w : warehouse) {
            if (!w.isFull()) return false;
        }
        return true;
    }
}

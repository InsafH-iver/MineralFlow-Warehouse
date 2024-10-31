package be.kdg.mineralflow.warehouse.business.service;

import be.kdg.mineralflow.warehouse.business.domain.DeliveryTicket;
import be.kdg.mineralflow.warehouse.business.domain.Resource;
import be.kdg.mineralflow.warehouse.business.domain.Warehouse;
import be.kdg.mineralflow.warehouse.exception.NoItemFoundException;
import be.kdg.mineralflow.warehouse.persistence.DeliveryTicketRepository;
import be.kdg.mineralflow.warehouse.persistence.ResourceRepository;
import be.kdg.mineralflow.warehouse.persistence.WarehouseRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class StockPortionDeliveryService {
    public static final Logger logger = Logger
            .getLogger(StockPortionDeliveryService.class.getName());
    private final WarehouseRepository warehouseRepository;
    private final ResourceRepository resourceRepository;
    private final DeliveryTicketRepository deliveryTicketRepository;

    public StockPortionDeliveryService(WarehouseRepository warehouseRepository, ResourceRepository resourceRepository, DeliveryTicketRepository deliveryTicketRepository) {
        this.warehouseRepository = warehouseRepository;
        this.resourceRepository = resourceRepository;
        this.deliveryTicketRepository = deliveryTicketRepository;
    }

    @Transactional
    public void handleStockPortionAtDelivery(UUID vendorId, UUID resourceId,
                                             double weight, UUID unloadingRequestId,
                                             ZonedDateTime endWeightTime) {
        logger.info(String.format("Start of handling StockPortion with weight %f and of vendor %s and resource %s", weight, vendorId, resourceId));
        Resource resource = getResourceById(resourceId);
        Warehouse warehouse = getWarehouseByResourceIdAndVendorId(resourceId, vendorId);
        ZonedDateTime deliveryTime = getDeliveryTime(unloadingRequestId, endWeightTime);

        addStockPortionToWarehouse(warehouse, resource, weight, deliveryTime);
        logger.info("Successfully added stock portion to warehouse");
    }

    private Warehouse getWarehouseByResourceIdAndVendorId(UUID resourceId, UUID vendorId) {
        Optional<Warehouse> optionalWarehouse = warehouseRepository.findFirstByVendorIdAndResourceId(vendorId, resourceId);

        if (optionalWarehouse.isEmpty()) {
            String messageException = String.format("The warehouse of vendor with id %s and for resource with id %s, was not found", vendorId, resourceId);
            logger.severe(messageException);
            throw new NoItemFoundException(messageException);
        }
        return optionalWarehouse.get();
    }

    private Resource getResourceById(UUID resourceId) {
        Optional<Resource> optionalResource = resourceRepository.findById(resourceId);

        if (optionalResource.isEmpty()) {
            String messageException = String.format("Resource with id %s was not found", resourceId);
            logger.severe(messageException);
            throw new NoItemFoundException(messageException);
        }

        return optionalResource.get();
    }

    private ZonedDateTime getDeliveryTime(UUID unloadingRequestId, ZonedDateTime endWeightTime) {
        Optional<DeliveryTicket> optionalDeliveryTicket = deliveryTicketRepository.findDeliveryTicketByUnloadingRequestId(unloadingRequestId);
        if (optionalDeliveryTicket.isEmpty()) {
            logger.info(String.format("The delivery ticket for unloading " +
                    "request id %s does not exist, time of end weight will be used", unloadingRequestId));

            return endWeightTime;
        }
        return optionalDeliveryTicket.get().getDeliveryTime();
    }

    private void addStockPortionToWarehouse(Warehouse warehouse, Resource resource,
                                            double weight, ZonedDateTime deliveryTime) {
        double storageCostPerTonPerDayOfResource = resource.getStoragePricePerTonPerDay();
        warehouse.addStockPortion(weight, deliveryTime, storageCostPerTonPerDayOfResource);
        warehouseRepository.save(warehouse);
    }
}

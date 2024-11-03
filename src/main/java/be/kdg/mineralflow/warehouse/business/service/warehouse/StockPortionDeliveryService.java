package be.kdg.mineralflow.warehouse.business.service.warehouse;

import be.kdg.mineralflow.warehouse.business.domain.DeliveryTicket;
import be.kdg.mineralflow.warehouse.business.domain.Resource;
import be.kdg.mineralflow.warehouse.business.domain.Warehouse;
import be.kdg.mineralflow.warehouse.business.service.purchase.order.UnfulfilledOrderLineService;
import be.kdg.mineralflow.warehouse.business.util.ExceptionHandlingHelper;
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
    private final UnfulfilledOrderLineService unfulfilledOrderLineService;

    public StockPortionDeliveryService(WarehouseRepository warehouseRepository,
                                       ResourceRepository resourceRepository,
                                       DeliveryTicketRepository deliveryTicketRepository,
                                       UnfulfilledOrderLineService unfulfilledOrderLineService) {
        this.warehouseRepository = warehouseRepository;
        this.resourceRepository = resourceRepository;
        this.deliveryTicketRepository = deliveryTicketRepository;
        this.unfulfilledOrderLineService = unfulfilledOrderLineService;
    }

    @Transactional
    public void handleStockPortionAtDelivery(UUID vendorId, UUID resourceId,
                                             double weight, UUID unloadingRequestId,
                                             ZonedDateTime endWeightTime) {
        logger.info(String.format("Start of handling StockPortion with weight %f and of vendor %s and resource %s", weight, vendorId, resourceId));
        Resource resource = getResource(resourceId);
        Warehouse warehouse = getWarehouse(resourceId, vendorId);
        ZonedDateTime deliveryTime = getDeliveryTime(unloadingRequestId, endWeightTime);

        addStockPortionToWarehouse(warehouse, resource, weight, deliveryTime);
        unfulfilledOrderLineService.checkForUnfulfilledOrders(resourceId, warehouse);
        logger.info("Successfully added stock portion to warehouse");
    }

    private void addStockPortionToWarehouse(Warehouse warehouse, Resource resource,
                                            double weight, ZonedDateTime deliveryTime) {
        double storageCostPerTonPerDayOfResource = resource.getStoragePricePerTonPerDay();
        warehouse.addStockPortion(weight, deliveryTime, storageCostPerTonPerDayOfResource);
        warehouseRepository.save(warehouse);
    }


    private Warehouse getWarehouse(UUID resourceId, UUID vendorId) {
        return warehouseRepository.findFirstByVendorIdAndResourceId(vendorId, resourceId)
                .orElseThrow(() -> ExceptionHandlingHelper.logAndThrowNotFound(
                        "No warehouse found for vendor ID %s with resource ID %s",
                        vendorId, resourceId
                ));
    }

    private Resource getResource(UUID resourceId) {
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


}

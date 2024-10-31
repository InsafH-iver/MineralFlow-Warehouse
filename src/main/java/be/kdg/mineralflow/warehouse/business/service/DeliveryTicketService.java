package be.kdg.mineralflow.warehouse.business.service;

import be.kdg.mineralflow.warehouse.business.domain.DeliveryTicket;
import be.kdg.mineralflow.warehouse.business.domain.Warehouse;
import be.kdg.mineralflow.warehouse.exception.IncorrectDomainException;
import be.kdg.mineralflow.warehouse.exception.NoItemFoundException;
import be.kdg.mineralflow.warehouse.persistence.DeliveryTicketRepository;
import be.kdg.mineralflow.warehouse.persistence.WarehouseRepository;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.DeliveryDataDto;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.DeliveryTicketDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import static java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME;

@Service
public class DeliveryTicketService {
    public static final Logger logger = Logger
            .getLogger(DeliveryTicketService.class.getName());
    private final WarehouseRepository warehouseRepository;
    private final DeliveryTicketGeneratingService deliveryTicketGeneratingService;
    private final DeliveryTicketRepository deliveryTicketRepository;

    public DeliveryTicketService(WarehouseRepository warehouseRepository, DeliveryTicketGeneratingService deliveryTicketGeneratingService, DeliveryTicketRepository deliveryTicketRepository) {
        this.warehouseRepository = warehouseRepository;
        this.deliveryTicketGeneratingService = deliveryTicketGeneratingService;
        this.deliveryTicketRepository = deliveryTicketRepository;
    }

    @Transactional
    public DeliveryTicketDto addDeliveryTicket(DeliveryDataDto deliveryDataDto) {
        logger.info(String.format("Process to add delivery-ticket with %s delivery-time has started",
                deliveryDataDto.deliveryTime().format(ISO_ZONED_DATE_TIME)));
        Warehouse warehouse = getWarehouseByResourceIdAndVendorId(deliveryDataDto.resourceId(), deliveryDataDto.vendorId());
        DeliveryTicketDto deliveryTicketDto = createDeliveryTicket(warehouse,
                deliveryDataDto.deliveryTime(), deliveryDataDto.unloadingRequestId());
        deliveryTicketGeneratingService.generateDeliveryTicketPdf(deliveryTicketDto);

        logger.info(String.format("Successfully completed process to add delivery-ticket" +
                        " with %s delivery-time",
                deliveryDataDto.deliveryTime().format(ISO_ZONED_DATE_TIME)));
        return deliveryTicketDto;
    }

    private Warehouse getWarehouseByResourceIdAndVendorId(UUID resourceId, UUID vendorId) {
        Optional<Warehouse> optionalWarehouse = warehouseRepository.findFirstByVendorIdAndResourceId(vendorId, resourceId);

        if (optionalWarehouse.isEmpty()) {
            String messageException = String.format("The warehouse of vendor with id %s and resource with id %s, was not found", vendorId, resourceId);
            logger.severe(messageException);
            throw new NoItemFoundException(messageException);
        }
        return optionalWarehouse.get();
    }

    private DeliveryTicketDto createDeliveryTicket(Warehouse warehouse, ZonedDateTime deliveryTime, UUID unloadingRequestId) {
        checkIfDeliveryTicketAlreadyExistForUnloadingRequest(unloadingRequestId);
        warehouse.addDeliveryTicket(deliveryTime, unloadingRequestId);
        warehouseRepository.save(warehouse);
        return draftDeliveryTicketDto(warehouse, deliveryTime);
    }

    private void checkIfDeliveryTicketAlreadyExistForUnloadingRequest(UUID unloadingRequestId) {
        Optional<DeliveryTicket> optionalDeliveryTicket = deliveryTicketRepository.findDeliveryTicketByUnloadingRequestId(unloadingRequestId);
        if (optionalDeliveryTicket.isPresent()) {
            String messageException = String.format("The delivery ticket for unloading " +
                    "request id %s could not be made because it already exists", unloadingRequestId);
            logger.severe(messageException);
            throw new IncorrectDomainException(messageException);
        }
    }

    private DeliveryTicketDto draftDeliveryTicketDto(Warehouse warehouse, ZonedDateTime deliveryTime) {
        String resourceName = warehouse.getResource().getName();
        return new DeliveryTicketDto(resourceName, deliveryTime, warehouse.getWarehouseNumber());
    }
}

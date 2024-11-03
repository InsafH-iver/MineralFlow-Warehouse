package be.kdg.mineralflow.warehouse.presentation.controller.api;

import be.kdg.mineralflow.warehouse.business.service.warehouse.DeliveryTicketService;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.delivery.DeliveryDataDto;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.delivery.DeliveryTicketDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/deliveryTickets")
public class DeliveryTicketRestController {
    public static final Logger logger = Logger
            .getLogger(DeliveryTicketRestController.class.getName());
    private final DeliveryTicketService deliveryTicketService;

    public DeliveryTicketRestController(DeliveryTicketService deliveryTicketService) {
        this.deliveryTicketService = deliveryTicketService;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public DeliveryTicketDto addDeliveryTicket(
            @RequestBody DeliveryDataDto deliveryDataDto) {
        logger.info(String.format("Call was made to delivery ticket at %s for unloading-request" +
                        " with id %s",
                deliveryDataDto.deliveryTime().format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
                deliveryDataDto.unloadingRequestId()));

        DeliveryTicketDto deliveryTicketDto = deliveryTicketService.addDeliveryTicket(deliveryDataDto);

        logger.info(String.format("Delivery ticket has been made for %s for unloading-request" +
                        " with id %s",
                deliveryDataDto.deliveryTime().format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
                deliveryDataDto.unloadingRequestId()));
        return deliveryTicketDto;
    }
}

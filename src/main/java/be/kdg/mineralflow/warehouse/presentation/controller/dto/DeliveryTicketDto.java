package be.kdg.mineralflow.warehouse.presentation.controller.dto;

import java.time.ZonedDateTime;

public record DeliveryTicketDto(String resourceName, ZonedDateTime deliveryTime,
                                int warehouseNumber) {
}

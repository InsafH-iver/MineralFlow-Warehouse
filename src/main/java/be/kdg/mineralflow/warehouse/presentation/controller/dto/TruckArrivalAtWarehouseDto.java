package be.kdg.mineralflow.warehouse.presentation.controller.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

public record TruckArrivalAtWarehouseDto(UUID vendorId, UUID resourceId,
                                         double weight ,ZonedDateTime deliveryTime) {
}

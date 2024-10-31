package be.kdg.mineralflow.warehouse.presentation.controller.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

public record DeliveryDataDto(UUID vendorId, UUID resourceId,
                              ZonedDateTime deliveryTime, UUID unloadingRequestId) {
}

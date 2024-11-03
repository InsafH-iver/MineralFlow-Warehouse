package be.kdg.mineralflow.warehouse.presentation.controller.dto.purchase.order;

import java.time.ZonedDateTime;

public record EndOfPurchaseOrderPickupDto(String  purchaseOrderNumber, ZonedDateTime endTime) {
}

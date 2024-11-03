package be.kdg.mineralflow.warehouse.presentation.controller.dto.purchase.order;

import java.util.UUID;

public record PurchaseOrderPickupDto(UUID vendorId, String poNumber) {
}


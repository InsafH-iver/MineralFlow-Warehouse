package be.kdg.mineralflow.warehouse.presentation.controller.dto.purchase.order;

import be.kdg.mineralflow.warehouse.business.domain.Status;

public record PurchaseOrderStatusDto(String purchaseOrderNumber,
                                     String vesselNumber,
                                     Status status) {
}

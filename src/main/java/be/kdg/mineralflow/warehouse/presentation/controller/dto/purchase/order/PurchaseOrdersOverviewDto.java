package be.kdg.mineralflow.warehouse.presentation.controller.dto.purchase.order;

import java.util.List;

public record PurchaseOrdersOverviewDto(
        List<PurchaseOrderStatusDto> openPurchaseOrders,
        List<PurchaseOrderStatusDto> completedPurchaseOrders) {
}

package be.kdg.mineralflow.warehouse.presentation.controller.dto;

import java.util.List;

public record PurchaseOrderDto(
        String poNumber,
        String referenceId,
        CustomerPartyDto customerParty,
        SellerPartyDto sellerParty,
        String vesselNumber,
        List<OrderLineDto> orderLines
) {
}

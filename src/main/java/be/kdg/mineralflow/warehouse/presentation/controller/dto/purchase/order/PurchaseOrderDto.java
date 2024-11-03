package be.kdg.mineralflow.warehouse.presentation.controller.dto.purchase.order;

import be.kdg.mineralflow.warehouse.presentation.controller.dto.PartyDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;


public record PurchaseOrderDto(
        @NotBlank
        String poNumber,
        String referenceUUID,
        @NotNull
        PartyDto customerParty,
        @NotNull
        PartyDto sellerParty,
        @NotBlank
        String vesselNumber,
        @NotNull
        @NotEmpty
        List<OrderLineDto> orderLines
) {
}

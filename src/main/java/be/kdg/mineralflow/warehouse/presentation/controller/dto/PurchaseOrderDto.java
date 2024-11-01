package be.kdg.mineralflow.warehouse.presentation.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;


public record PurchaseOrderDto(
        @NotBlank
        String poNumber,
        @NotBlank
        String referenceId,
        @NotNull
        CustomerPartyDto customerParty,
        @NotNull
        SellerPartyDto sellerParty,
        @NotBlank
        String vesselNumber,
        @NotNull
        @NotEmpty
        List<OrderLineDto> orderLines
) {
}

package be.kdg.mineralflow.warehouse.presentation.controller.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.UUID;

public record SellerPartyDto(
        @NotBlank
        @UUID
        String uuid,
        String name,
        String address
) {
}

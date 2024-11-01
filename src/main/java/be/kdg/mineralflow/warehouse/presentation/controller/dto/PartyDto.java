package be.kdg.mineralflow.warehouse.presentation.controller.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.UUID;

public record PartyDto(
        @NotBlank
        @UUID
        String UUID,
        String name,
        String address
) {
}
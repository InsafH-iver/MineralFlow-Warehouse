package be.kdg.mineralflow.warehouse.presentation.controller.dto.purchase.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public record OrderLineDto(
        String lineNumber,
        @NotEmpty
        String materialType,
        @NotEmpty
        String description,
        @Min(0)
        int quantity,
        @NotEmpty
        String uom
) {
}

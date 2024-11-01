package be.kdg.mineralflow.warehouse.presentation.controller.dto;

public record OrderLineDto(
        String lineNumber,
        String materialType,
        String description,
        int quantity,
        String uom
) {
}

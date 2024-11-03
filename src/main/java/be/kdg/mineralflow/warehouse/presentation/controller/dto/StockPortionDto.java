package be.kdg.mineralflow.warehouse.presentation.controller.dto;

import java.time.ZonedDateTime;

public record StockPortionDto(ZonedDateTime arrivalTime,
                              double amountInTon,
                              double amountLeftInTon,
                              double storageCostPerTonPerDay) {
}

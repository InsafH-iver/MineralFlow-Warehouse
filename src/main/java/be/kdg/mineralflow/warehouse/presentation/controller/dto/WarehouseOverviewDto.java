package be.kdg.mineralflow.warehouse.presentation.controller.dto;

import java.util.List;

public record WarehouseOverviewDto(double totalAmountOfResources,
                                   List<StockPortionDto> ListOfStockPortion) {

}

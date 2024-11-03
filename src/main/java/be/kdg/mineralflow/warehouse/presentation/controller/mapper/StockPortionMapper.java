package be.kdg.mineralflow.warehouse.presentation.controller.mapper;

import be.kdg.mineralflow.warehouse.business.domain.StockPortion;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.StockPortionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StockPortionMapper {
    StockPortionMapper INSTANCE = Mappers.getMapper(StockPortionMapper.class);

    @Mapping(source = "arrivalTime", target = "arrivalTime")
    @Mapping(source = "amountInTon", target = "amountInTon")
    @Mapping(source = "amountLeftInTon", target = "amountLeftInTon")
    @Mapping(source = "storageCostPerTonPerDay", target = "storageCostPerTonPerDay")
    StockPortionDto mapStockPortionToStockPortionDto(StockPortion stockPortion);

    List<StockPortionDto> mapStockPortions(List<StockPortion> inspectionOperations);
}
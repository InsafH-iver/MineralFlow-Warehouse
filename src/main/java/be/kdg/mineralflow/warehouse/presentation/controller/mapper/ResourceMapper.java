package be.kdg.mineralflow.warehouse.presentation.controller.mapper;

import be.kdg.mineralflow.warehouse.business.domain.Resource;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.ResourceDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,componentModel = "spring")
public interface ResourceMapper {
    @Mapping(target = "name", source = "name")
    @Mapping(target = "storagePricePerTonPerDay", source = "storagePricePerTonPerDay")
    @Mapping(target = "sellingPricePerTon", source = "sellingPricePerTon")
    ResourceDto toDto(Resource resource);
}

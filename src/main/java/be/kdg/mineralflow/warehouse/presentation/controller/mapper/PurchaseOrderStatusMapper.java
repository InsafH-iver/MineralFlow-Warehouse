package be.kdg.mineralflow.warehouse.presentation.controller.mapper;


import be.kdg.mineralflow.warehouse.business.domain.PurchaseOrder;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.purchase.order.PurchaseOrderStatusDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PurchaseOrderStatusMapper {
    PurchaseOrderStatusMapper INSTANCE = Mappers.getMapper(PurchaseOrderStatusMapper.class);

    @Mapping(source = "inspectionNumber", target = "inspectionNumber")
    @Mapping(source = "startingTime", target = "startingTime")
    @Mapping(source = "endingTime", target = "endingTime")
    @Mapping(source = "hasBeenApproved", target = "hasBeenApproved")
    PurchaseOrderStatusDto mapPurchaseOrderToPurchaseOrderStatusDto(PurchaseOrder purchaseOrder);

    List<PurchaseOrderStatusDto> mapPurchaseOrders(List<PurchaseOrder> inspectionOperations);
}

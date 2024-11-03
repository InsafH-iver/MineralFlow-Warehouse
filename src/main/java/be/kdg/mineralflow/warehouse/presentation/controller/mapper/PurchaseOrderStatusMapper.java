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

    @Mapping(source = "purchaseOrderNumber", target = "purchaseOrderNumber")
    @Mapping(source = "vesselNumber", target = "vesselNumber")
    @Mapping(source = "status", target = "status")
    PurchaseOrderStatusDto mapPurchaseOrderToPurchaseOrderStatusDto(PurchaseOrder purchaseOrder);

    List<PurchaseOrderStatusDto> mapPurchaseOrders(List<PurchaseOrder> inspectionOperations);
}

package be.kdg.mineralflow.warehouse.persistence.purchase.order;

import be.kdg.mineralflow.warehouse.business.domain.PurchaseOrder;
import be.kdg.mineralflow.warehouse.business.domain.Status;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, UUID> {

    Optional<PurchaseOrder> findByPurchaseOrderNumberAndVendorIdAndStatus(String purchaseOrderNumber, UUID vendorId, Status status);
    Optional<PurchaseOrder> findByOrderLines_id(UUID orderLineId);
    Optional<PurchaseOrder> findPurchaseOrderByPurchaseOrderNumber(String purchaseOrderNumber);
    @EntityGraph(attributePaths = {"orderLines","orderLines.resource","vendor","buyer"})
    List<PurchaseOrder> findAll();
    List<PurchaseOrder> findAllByStatusNot(Status status);
    List<PurchaseOrder> findAllByStatus(Status status);
}

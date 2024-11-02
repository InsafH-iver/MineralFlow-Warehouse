package be.kdg.mineralflow.warehouse.persistence;

import be.kdg.mineralflow.warehouse.business.domain.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, UUID> {
}

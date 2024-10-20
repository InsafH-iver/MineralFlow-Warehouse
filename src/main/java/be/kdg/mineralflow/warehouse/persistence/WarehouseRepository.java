package be.kdg.mineralflow.warehouse.persistence;

import be.kdg.mineralflow.warehouse.business.domain.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WarehouseRepository extends JpaRepository<Warehouse, UUID> {

    Optional<Warehouse> findFirstByVendorIdAndResourceId(UUID vendorId, UUID resourceId);
    List<Warehouse> findAllByVendorIdAndResourceId(UUID vendorId,UUID resourceId);
}

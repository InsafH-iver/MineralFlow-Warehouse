package be.kdg.mineralflow.warehouse.persistence.purchase.order;

import be.kdg.mineralflow.warehouse.business.domain.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderLineRepository extends JpaRepository<OrderLine, UUID> {

    List<OrderLine> findAllByResourceId(UUID resourceId);
}

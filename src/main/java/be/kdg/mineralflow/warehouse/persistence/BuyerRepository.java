package be.kdg.mineralflow.warehouse.persistence;

import be.kdg.mineralflow.warehouse.business.domain.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BuyerRepository extends JpaRepository<Buyer, UUID> {
}

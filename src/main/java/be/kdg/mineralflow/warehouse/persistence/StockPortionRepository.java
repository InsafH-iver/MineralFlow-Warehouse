package be.kdg.mineralflow.warehouse.persistence;

import be.kdg.mineralflow.warehouse.business.domain.StockPortion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StockPortionRepository extends JpaRepository<StockPortion, UUID> {
}

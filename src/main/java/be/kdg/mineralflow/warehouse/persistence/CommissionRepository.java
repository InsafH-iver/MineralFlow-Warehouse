package be.kdg.mineralflow.warehouse.persistence;

import be.kdg.mineralflow.warehouse.business.domain.Commission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CommissionRepository extends JpaRepository<Commission, UUID> {
    @Query("SELECT c FROM Commission c WHERE DATE(c.creationDate) = :date AND c.invoice IS NULL")
    List<Commission> findAllCommissionsByCreationDateAndInvoiceIsNull(@Param("date") LocalDate date);
}

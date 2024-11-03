package be.kdg.mineralflow.warehouse.persistence;

import be.kdg.mineralflow.warehouse.business.domain.Commission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CommissionRepository extends JpaRepository<Commission, UUID> {

    List<Commission> findCommissionsByCreationDateAndPurchaseOrder_Vendor_Id(LocalDate date, UUID vendorId);
}

package be.kdg.mineralflow.warehouse.persistence;

import be.kdg.mineralflow.warehouse.business.domain.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    @Query("SELECT i FROM Invoice i WHERE i.vendor.id = :vendorId AND DATE(i.creationDate) = :localDate")
    Invoice getInvoiceByVendorIdAndCreationDate(@Param("vendorId") UUID vendorId, @Param("localDate") LocalDate localDate);
}

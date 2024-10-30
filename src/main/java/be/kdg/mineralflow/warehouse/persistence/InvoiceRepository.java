package be.kdg.mineralflow.warehouse.persistence;

import be.kdg.mineralflow.warehouse.business.domain.Invoice;
import be.kdg.mineralflow.warehouse.business.domain.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    @Query(
            "select i from Invoice i " +
                "Where Date(i.creationDate) = :date " +
                    "AND i.vendor = :vendor "
    )
    Optional<Invoice> getInvoiceByVendorAndDate(Vendor vendor, LocalDate date);
}

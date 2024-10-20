package be.kdg.mineralflow.warehouse.persistence;

import be.kdg.mineralflow.warehouse.business.domain.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
}

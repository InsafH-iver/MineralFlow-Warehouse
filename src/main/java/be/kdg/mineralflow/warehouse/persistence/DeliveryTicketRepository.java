package be.kdg.mineralflow.warehouse.persistence;

import be.kdg.mineralflow.warehouse.business.domain.DeliveryTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryTicketRepository extends JpaRepository<DeliveryTicket, UUID> {

    Optional<DeliveryTicket> findDeliveryTicketByUnloadingRequestId(UUID unloadingRequestId);
}

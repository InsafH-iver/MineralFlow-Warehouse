package be.kdg.mineralflow.warehouse.business.domain;

import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
public class DeliveryTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private ZonedDateTime deliveryTime;
    @Column(unique = true)
    private UUID unloadingRequestId;


    protected DeliveryTicket() {
    }

    public DeliveryTicket(ZonedDateTime deliveryTime, UUID UnloadingRequestId) {
        this.deliveryTime = deliveryTime;
        this.unloadingRequestId = UnloadingRequestId;
    }

    public ZonedDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public UUID getUnloadingRequestId() {
        return unloadingRequestId;
    }
}

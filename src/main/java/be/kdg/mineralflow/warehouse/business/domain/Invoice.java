package be.kdg.mineralflow.warehouse.business.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private ZonedDateTime created;
    @ManyToMany
    private List<Warehouse> warehouses;

    public Invoice(ZonedDateTime created, List<Warehouse> warehouses) {
        this.created = created;
        this.warehouses = warehouses;
    }

    protected Invoice() {

    }

    public double getTotalStorageCost(){
        return warehouses.stream().mapToDouble(w -> w.getStorageCost(created)).sum();
    }

    public Map<Resource, Double> getStorageCostPerResource(){
        return warehouses.stream().collect(
                Collectors.groupingBy(
                        Warehouse::getResource,
                        Collectors.summingDouble(w -> w.getStorageCost(created)))
        );
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}

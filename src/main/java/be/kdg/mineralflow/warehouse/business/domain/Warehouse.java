package be.kdg.mineralflow.warehouse.business.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private int warehouseNumber;
    private double usedCapacityInTon;

    @ManyToOne
    private Resource resource;
    @ManyToOne
    private Vendor vendor;

    protected Warehouse() {
    }

    public Warehouse(UUID id, int warehouseNumber, double usedCapacityInTon) {
        this.id = id;
        this.warehouseNumber = warehouseNumber;
        this.usedCapacityInTon = usedCapacityInTon;
    }

    public int getWarehouseNumber() {
        return warehouseNumber;
    }
}

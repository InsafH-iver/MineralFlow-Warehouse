package be.kdg.mineralflow.warehouse.business.domain;

import be.kdg.mineralflow.warehouse.exception.IncorrectDomainException;
import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Entity
public class Warehouse {
    public static final Logger logger = Logger
            .getLogger(Warehouse.class.getName());
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private int warehouseNumber;
    private double usedCapacityInTon;
    private double maxCapacityInTon;

    @ManyToOne
    private Resource resource;
    @ManyToOne
    private Vendor vendor;
    @OneToMany(cascade = CascadeType.ALL)
    private List<StockPortion> stockPortions;

    protected Warehouse() {
    }

    public Warehouse(UUID id, int warehouseNumber, double usedCapacityInTon, double maxCapacityInTon) {
        this.id = id;
        this.warehouseNumber = warehouseNumber;
        this.usedCapacityInTon = usedCapacityInTon;
        stockPortions = new ArrayList<>();
        this.maxCapacityInTon = maxCapacityInTon;
    }

    public int getWarehouseNumber() {
        return warehouseNumber;
    }
    public boolean isFull(){
        return usedCapacityInTon >= maxCapacityInTon * 0.8;
    }

    public void addStockPortion(double amountInTon, ZonedDateTime deliveryTime, double storageCostPerTonPerDay) {
        if (amountInTon < 0) {
            String text = String.format("The provided amount in ton (%f) is invalid. The value must be a positive number.", amountInTon);
            logger.severe(text);
            throw new IncorrectDomainException(text);
        }
        usedCapacityInTon += amountInTon;
        StockPortion stockPortion = new StockPortion(amountInTon, deliveryTime, storageCostPerTonPerDay);
        stockPortions.add(stockPortion);
    }

    public List<StockPortion> getStockPortions() {
        return stockPortions;
    }
}

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
    @OneToMany(cascade = CascadeType.ALL)
    private List<DeliveryTicket> deliveryTickets;


    protected Warehouse() {
    }

    public Warehouse(UUID warehouseId,int warehouseNumber, double usedCapacityInTon, double maxCapacityInTon) {
        this.id = warehouseId;
        this.warehouseNumber = warehouseNumber;
        this.usedCapacityInTon = usedCapacityInTon;
        stockPortions = new ArrayList<>();
        deliveryTickets = new ArrayList<>();
        this.maxCapacityInTon = maxCapacityInTon;
    }

    public int getWarehouseNumber() {
        return warehouseNumber;
    }
    public boolean isFull(double maxCapacityThreshold){
        return usedCapacityInTon >= maxCapacityInTon * maxCapacityThreshold;
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

    public void addDeliveryTicket(ZonedDateTime deliveryTime, UUID unloadingRequest) {
        DeliveryTicket deliveryTicket = new DeliveryTicket(deliveryTime, unloadingRequest);
        deliveryTickets.add(deliveryTicket);
    }

    public Resource getResource() {
        return resource;
    }

    public List<StockPortion> getStockPortions() {
        return stockPortions;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public double getUsedCapacityInTon() {
        return usedCapacityInTon;
    }

    public List<DeliveryTicket> getDeliveryTickets() {
        return deliveryTickets;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public void setStockPortions(List<StockPortion> stockPortions) {
        this.stockPortions = stockPortions;
    }

    public void reduceStock(double amountInTonTakenOut) {
        if (amountInTonTakenOut > this.usedCapacityInTon || amountInTonTakenOut < 0) {
            String messageException = String.format("The provided amount taken out of warehouse in ton (%f) is invalid," +
                    " the value must be a positive number and can not be more than the used capacity %s"
                    , amountInTonTakenOut, this.usedCapacityInTon);
            logger.severe(messageException);
            throw new IncorrectDomainException(messageException);
        }
        usedCapacityInTon -= amountInTonTakenOut;
    }
}

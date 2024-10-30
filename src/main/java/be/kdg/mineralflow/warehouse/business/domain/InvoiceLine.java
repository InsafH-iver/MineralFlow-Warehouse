package be.kdg.mineralflow.warehouse.business.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class InvoiceLine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @ManyToOne
    private Resource resource;
    @ManyToOne
    private StockPortion stockPortion;

    protected InvoiceLine() {
    }

    public InvoiceLine(Resource resource, StockPortion stockPortion) {
        this.resource = resource;
        this.stockPortion = stockPortion;
    }

    public double getStorageCost(LocalDateTime date){
        return stockPortion.getStorageCost(date);
    }

    public Resource getResource() {
        return resource;
    }

    public StockPortion getStockPortion() {
        return stockPortion;
    }
}

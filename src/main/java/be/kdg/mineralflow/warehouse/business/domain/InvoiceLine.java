package be.kdg.mineralflow.warehouse.business.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

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
    public long getDaysInStorage(LocalDateTime date){
        return stockPortion.getDaysBetween(date);
    }
    public Resource getResource() {
        return resource;
    }

    public StockPortion getStockPortion() {
        return stockPortion;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}

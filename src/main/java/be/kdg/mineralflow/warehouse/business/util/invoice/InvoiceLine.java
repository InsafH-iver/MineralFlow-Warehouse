package be.kdg.mineralflow.warehouse.business.util.invoice;

import be.kdg.mineralflow.warehouse.business.domain.Resource;
import be.kdg.mineralflow.warehouse.business.domain.StockPortion;

import java.time.LocalDateTime;

public class InvoiceLine {
    private Resource resource;
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
}

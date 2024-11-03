package be.kdg.mineralflow.warehouse.business.util.invoice;

import be.kdg.mineralflow.warehouse.business.domain.InvoiceLine;
import be.kdg.mineralflow.warehouse.business.domain.Resource;
import be.kdg.mineralflow.warehouse.business.domain.StockPortion;
import be.kdg.mineralflow.warehouse.business.domain.Warehouse;
import be.kdg.mineralflow.warehouse.business.util.storageCost.StorageCostCalculator;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.invoice.InvoiceLineDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InvoiceLineFactory {
    private final StorageCostCalculator storageCostCalculator;

    public InvoiceLineFactory(StorageCostCalculator storageCostCalculator) {
        this.storageCostCalculator = storageCostCalculator;
    }

    public List<InvoiceLine> createInvoiceLinesFromWarehouses(List<Warehouse> warehouses) {
        return warehouses.stream()
                .flatMap(warehouse -> warehouse.getStockPortions().stream()
                        .map(stockPortion -> createInvoiceLine(warehouse.getResource(), stockPortion)))
                .collect(Collectors.toList());
    }

    public InvoiceLine createInvoiceLine(Resource resource, StockPortion stockPortion) {
        return new InvoiceLine(
                resource,
                stockPortion.getArrivalTime(),
                stockPortion.getAmountLeftInTon(),
                stockPortion.getStorageCostPerTonPerDay());
    }
}

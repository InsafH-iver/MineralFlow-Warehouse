package be.kdg.mineralflow.warehouse.presentation.controller.mapper;

import be.kdg.mineralflow.warehouse.business.domain.Commission;
import be.kdg.mineralflow.warehouse.business.domain.StockPortion;
import be.kdg.mineralflow.warehouse.business.domain.Vendor;
import be.kdg.mineralflow.warehouse.business.domain.Invoice;
import be.kdg.mineralflow.warehouse.business.util.storageCost.StorageCostCalculator;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.invoice.InvoiceDto;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.invoice.InvoiceLineDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InvoiceMapper {
    private final StorageCostCalculator storageCostCalculator;

    public InvoiceMapper(StorageCostCalculator storageCostCalculator) {
        this.storageCostCalculator = storageCostCalculator;
    }
    public InvoiceDto mapInvoiceToInvoiceDto(Invoice invoice) {
        List<InvoiceLineDto> invoiceLines = createInvoiceLineDtos(invoice);
        double totalStorageCost = invoiceLines.stream().mapToDouble(InvoiceLineDto::getStorageCost).sum();
        double commissionCost = (invoice.getCommissions() == null)? 0 :
                invoice.getCommissions().stream().mapToDouble(Commission::getCommisionPrice).sum();

        return new InvoiceDto(
                invoice.getVendor().getName(),
                invoiceLines,
                invoice.getCreationDate(),
                totalStorageCost,
                commissionCost
        );
    }
    private List<InvoiceLineDto> createInvoiceLineDtos(Invoice invoice) {
        return invoice.getInvoiceLines().stream().map(
                il -> {
                    StockPortion stockPortion = il.getStockPortion();
                    return new InvoiceLineDto(
                            stockPortion.getArrivalTime().toLocalDateTime(),
                            il.getResource().getName(),
                            stockPortion.getAmountLeftInTon(),
                            stockPortion.getStorageCostPerTonPerDay(),
                            il.getDaysInStorage(invoice.getCreationDate()),
                            storageCostCalculator.calculateStorageCost(stockPortion,invoice.getCreationDate())
                    );
                }
        ).toList();
    }
}

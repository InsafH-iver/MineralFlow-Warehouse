package be.kdg.mineralflow.warehouse.presentation.controller.mapper;

import be.kdg.mineralflow.warehouse.business.domain.Commission;
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
    public InvoiceDto mapInvoiceToInvoiceDto(Invoice invoice, List<Commission> commissions) {
        List<InvoiceLineDto> invoiceLines = createInvoiceLineDtos(invoice);
        double totalStorageCost = invoiceLines.stream().mapToDouble(InvoiceLineDto::getStorageCost).sum();
        double commissionCost = (commissions.isEmpty())? 0 :
                commissions.stream().mapToDouble(Commission::getCommisionPrice).sum();

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
                    return new InvoiceLineDto(
                            il.getArrivalTime().toLocalDateTime(),
                            il.getResource().getName(),
                            il.getAmountInTon(),
                            il.getStorageCostPerTonPerDay(),
                            il.getDaysInStorage(invoice.getCreationDate().toLocalDate()),
                            storageCostCalculator.calculateStorageCost(il,invoice.getCreationDate().toLocalDate())
                    );
                }
        ).toList();
    }
}

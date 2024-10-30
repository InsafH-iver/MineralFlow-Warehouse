package be.kdg.mineralflow.warehouse.business.util.mapper;


import be.kdg.mineralflow.warehouse.business.domain.Invoice;
import be.kdg.mineralflow.warehouse.business.domain.InvoiceLine;
import be.kdg.mineralflow.warehouse.business.util.dto.InvoiceDto;
import be.kdg.mineralflow.warehouse.business.util.dto.InvoiceLineDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InvoiceMapper {
    InvoiceMapper INSTANCE = Mappers.getMapper(InvoiceMapper.class);

    @Mapping(source = "vendor.name",target = "vendorName")
    @Mapping(source = "invoiceLines",target = "invoiceLines")
    @Mapping(source = "creationDate",target = "creationDate")
    @Mapping(source = "totalStorageCost", target = "totalStorageCost")
    InvoiceDto invoiceToInvoiceDto(Invoice invoice);

    @Mapping(target = "storageCost", ignore = true)
    @Mapping(source = "stockPortion.storageCostPerTonPerDay",target = "storageCostPerDayPerTon")
    @Mapping(source = "stockPortion.amountInTon",target = "weightInTon")
    @Mapping(source = "stockPortion.arrivalTime",target = "arrivalTime")
    @Mapping(source = "resource.name",target = "resource")
    InvoiceLineDto invoiceLineToInvoiceLineDto(InvoiceLine invoiceLine);

    @AfterMapping
    default void calculateStorageCosts(Invoice invoice, @MappingTarget InvoiceDto invoiceDTO) {
        invoiceDTO.getInvoiceLines().forEach(lineDto -> {
            double storageCost = invoice.getInvoiceLines()
                    .get(invoiceDTO.getInvoiceLines().indexOf(lineDto))
                    .getStorageCost(invoice.getCreationDate());
            lineDto.setStorageCost(storageCost);
        });
    }
}

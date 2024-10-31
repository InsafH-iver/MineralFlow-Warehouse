package be.kdg.mineralflow.warehouse.presentation.controller.mapper;


import be.kdg.mineralflow.warehouse.business.util.Invoice;
import be.kdg.mineralflow.warehouse.business.util.InvoiceLine;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.InvoiceDto;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.InvoiceLineDto;
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
    @Mapping(target = "daysInStorage",ignore = true)
    InvoiceLineDto invoiceLineToInvoiceLineDto(InvoiceLine invoiceLine);

    @AfterMapping
    default void calculateStorageCosts(Invoice invoice, @MappingTarget InvoiceDto invoiceDTO) {
        invoiceDTO.getInvoiceLines().forEach(lineDto -> {
            double storageCost = invoice.getInvoiceLines()
                    .get(invoiceDTO.getInvoiceLines().indexOf(lineDto))
                    .getStorageCost(invoice.getCreationDate());
            lineDto.setStorageCost(storageCost);
            long daysInStorage = invoice.getInvoiceLines()
                    .get(invoiceDTO.getInvoiceLines().indexOf(lineDto))
                    .getDaysInStorage(invoice.getCreationDate());
            lineDto.setDaysInStorage(daysInStorage);

        });
    }
}

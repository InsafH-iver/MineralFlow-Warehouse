package be.kdg.mineralflow.warehouse.presentation.controller.api;

import be.kdg.mineralflow.warehouse.business.domain.Invoice;
import be.kdg.mineralflow.warehouse.business.service.InvoiceService;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.InvoiceDto;
import be.kdg.mineralflow.warehouse.presentation.controller.mapper.InvoiceMapper;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController("/invoice")
public class InvoiceRestController {

    private final InvoiceService invoiceService;
    private final InvoiceMapper invoiceMapper;

    public InvoiceRestController(InvoiceService invoiceService, InvoiceMapper invoiceMapper) {
        this.invoiceService = invoiceService;
        this.invoiceMapper = invoiceMapper;
    }

    @GetMapping
    public ResponseEntity<InvoiceDto> getInvoice(UUID vendorId, LocalDateTime dateTime){
        Invoice invoice = invoiceService.getInvoice(vendorId,dateTime);
        return new ResponseEntity<>(invoiceMapper.mapInvoiceToInvoiceDto(invoice),HttpStatus.OK);
    }
}

package be.kdg.mineralflow.warehouse.presentation.controller.api;

import be.kdg.mineralflow.warehouse.business.service.invoice.InvoiceService;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.invoice.InvoiceDto;
import be.kdg.mineralflow.warehouse.presentation.controller.mapper.InvoiceMapper;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/invoice")
public class InvoiceRestController {
    public static final Logger logger = Logger
            .getLogger(InvoiceRestController.class.getName());
    private final InvoiceService invoiceService;
    private final InvoiceMapper invoiceMapper;

    public InvoiceRestController(InvoiceService invoiceService, InvoiceMapper invoiceMapper) {
        this.invoiceService = invoiceService;
        this.invoiceMapper = invoiceMapper;
    }

    @Transactional
    @GetMapping("/{vendorId}/{dateTime}")
    public ResponseEntity<InvoiceDto> getInvoice(@PathVariable UUID vendorId, @PathVariable LocalDateTime dateTime){
        logger.info(String.format("getInvoice was called with vendorId %s and dateTime %s",vendorId,dateTime));
        InvoiceDto invoiceDto = invoiceService.getInvoiceAndCommission(vendorId,dateTime.toLocalDate());
        if (invoiceDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return new ResponseEntity<>(invoiceDto,HttpStatus.OK);
    }
}

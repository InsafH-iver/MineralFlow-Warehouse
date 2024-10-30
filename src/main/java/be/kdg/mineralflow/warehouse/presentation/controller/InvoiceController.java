package be.kdg.mineralflow.warehouse.presentation.controller;

import be.kdg.mineralflow.warehouse.business.domain.Invoice;
import be.kdg.mineralflow.warehouse.business.service.InvoiceService;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.InvoiceDto;
import be.kdg.mineralflow.warehouse.presentation.controller.mapper.InvoiceMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.logging.Logger;

@Controller
public class InvoiceController {
    public static final Logger logger = Logger
            .getLogger(InvoiceController.class.getName());
    private final InvoiceService invoiceService;

    private InvoiceMapper invoiceMapper = InvoiceMapper.INSTANCE;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/invoice/{vendorName}/{dateTime}")
    public String getInvoice(@PathVariable String vendorName,
                             @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTime,
                             Model model) {
        Invoice invoice = invoiceService.getInvoice(vendorName,dateTime);
        InvoiceDto invoiceDto = invoiceMapper.invoiceToInvoiceDto(invoice);

        model.addAttribute("invoice", invoiceDto);
        return "invoice"; // Thymeleaf template name
    }

}

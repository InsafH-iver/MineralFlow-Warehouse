package be.kdg.mineralflow.warehouse.presentation.controller;

import be.kdg.mineralflow.warehouse.business.service.InvoiceService;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.InvoiceDto;
import be.kdg.mineralflow.warehouse.presentation.controller.mapper.InvoiceMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.logging.Logger;

@Controller
public class InvoiceController {
    public static final Logger logger = Logger
            .getLogger(InvoiceController.class.getName());
    private final InvoiceService invoiceService;

    private final InvoiceMapper invoiceMapper = InvoiceMapper.INSTANCE;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/invoice")
    public String getInvoice(@RequestParam String vendorName,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                             Model model) {
        InvoiceDto invoiceDto = invoiceService.getInvoice(vendorName, date);
        model.addAttribute("invoice", invoiceDto);
        return "invoice";
    }

}

package be.kdg.mineralflow.warehouse.business.service.pdf;

import be.kdg.mineralflow.warehouse.presentation.controller.dto.invoice.InvoiceDto;
import org.springframework.stereotype.Service;

@Service
public class InvoiceGeneratingService {

    private final PdfGeneratingService pdfGeneratingService;

    public InvoiceGeneratingService(PdfGeneratingService pdfGeneratingService) {
        this.pdfGeneratingService = pdfGeneratingService;
    }

    public void generateInvoicePdf(InvoiceDto invoiceDto) {
        String fileName = String.format("INV-%s.pdf", invoiceDto.hashCode());
        pdfGeneratingService.generatePdf(invoiceDto, "invoice", "invoice", fileName);
    }
}

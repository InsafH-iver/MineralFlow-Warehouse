package be.kdg.mineralflow.warehouse.business.service;

import be.kdg.mineralflow.warehouse.business.util.dto.InvoiceDto;
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

package be.kdg.mineralflow.warehouse.business.service.pdf;

import be.kdg.mineralflow.warehouse.presentation.controller.dto.delivery.DeliveryTicketDto;
import org.springframework.stereotype.Service;

@Service
public class DeliveryTicketGeneratingService {

    private final PdfGeneratingService pdfGeneratingService;

    public DeliveryTicketGeneratingService(PdfGeneratingService pdfGeneratingService) {
        this.pdfGeneratingService = pdfGeneratingService;
    }

    public void generateDeliveryTicketPdf(DeliveryTicketDto deliveryTicketDto) {
        String fileName = String.format("PDT-%s.pdf", deliveryTicketDto.hashCode());
        pdfGeneratingService.generatePdf(deliveryTicketDto, "deliveryTicket",
                "delivery_ticket", fileName);
    }
}

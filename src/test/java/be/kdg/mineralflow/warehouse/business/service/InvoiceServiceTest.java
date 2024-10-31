package be.kdg.mineralflow.warehouse.business.service;

import be.kdg.mineralflow.warehouse.TestContainer;
import be.kdg.mineralflow.warehouse.business.domain.Invoice;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.InvoiceDto;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.InvoiceLineDto;
import be.kdg.mineralflow.warehouse.persistence.InvoiceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class InvoiceServiceTest extends TestContainer {

    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Test
    void createInvoices() {
        //ARRANGE
        LocalDate today = LocalDate.now();
        //ACT
        invoiceService.createInvoices();
        List<Invoice> invoices = invoiceRepository.findAll();
        //ASSERT
        assertThat(invoices).isNotEmpty();
        invoices.forEach(invoice -> assertThat(invoice).isNotNull());
        invoices.forEach(invoice -> assertThat(invoice.getInvoiceLines()).isNotEmpty());
        invoices.forEach(invoice -> assertThat(invoice.getVendor()).isNotNull());
        invoices.forEach(invoice -> assertThat(invoice.getCreationDate()).isNotNull());
        invoices.forEach(invoice -> assertThat(LocalDate.from(invoice.getCreationDate())).isEqualTo(today));
        invoices.forEach(
                invoice ->
                        assertThat(invoice.getTotalStorageCost())
                                .isEqualTo(invoice.getInvoiceLines().stream().mapToDouble(
                                        invoiceLine ->
                                                invoiceLine.getStorageCost(invoice.getCreationDate())).sum()));
    }

    @Test
    void getInvoice_happyPath() {
        //ARRANGE
        LocalDate today = LocalDate.now();
        invoiceService.createInvoices();
        String vendorName = "Acme Supplies";
        //ACT
        InvoiceDto invoiceDto = invoiceService.getInvoice(vendorName, today);
        //ASSERT
        assertThat(invoiceDto).isNotNull();
        assertThat(invoiceDto.getVendorName()).isEqualTo(vendorName);
        assertThat(LocalDate.from(invoiceDto.getCreationDate())).isEqualTo(today);
        assertThat(invoiceDto.getInvoiceLines()).isNotEmpty();
        assertThat(invoiceDto.getTotalStorageCost())
                .isEqualTo(invoiceDto.getInvoiceLines().stream().mapToDouble(
                        InvoiceLineDto::getStorageCost).sum());
    }
}
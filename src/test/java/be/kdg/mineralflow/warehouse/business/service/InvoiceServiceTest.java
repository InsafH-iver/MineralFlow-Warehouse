package be.kdg.mineralflow.warehouse.business.service;

import be.kdg.mineralflow.warehouse.TestContainer;
import be.kdg.mineralflow.warehouse.business.domain.Invoice;
import be.kdg.mineralflow.warehouse.persistence.InvoiceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

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
        String regex = "INV.*\\.pdf";
        Pattern pattern = Pattern.compile(regex);
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

        File outputDir = new File(System.getProperty("user.home"));
        File[] matchingFiles = outputDir.listFiles((dir, name) -> pattern.matcher(name).matches());
        assertThat(matchingFiles).isNotNull();
        assertThat(matchingFiles.length).isGreaterThan(0);
        assertThat(matchingFiles[0].length()).isGreaterThan(0);
    }
}
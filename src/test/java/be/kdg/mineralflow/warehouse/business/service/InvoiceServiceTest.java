package be.kdg.mineralflow.warehouse.business.service;

import be.kdg.mineralflow.warehouse.TestContainer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;


class InvoiceServiceTest extends TestContainer {

    @Autowired
    private InvoiceService invoiceService;

    @Test
    void createInvoices() {
        //ARRANGE
        String regex = "INV.*\\.pdf";
        Pattern pattern = Pattern.compile(regex);
        //ACT
        invoiceService.createInvoices();
        //ASSERT
        File outputDir = new File(System.getProperty("user.home"));
        File[] matchingFiles = outputDir.listFiles((dir, name) -> pattern.matcher(name).matches());
        assertThat(matchingFiles).isNotNull();
        assertThat(matchingFiles.length).isGreaterThan(0);
        assertThat(matchingFiles[0].length()).isGreaterThan(0);
        Arrays.stream(matchingFiles).forEach(file -> {
            try (PDDocument document = PDDocument.load(file)) {
                PDFTextStripper pdfStripper = new PDFTextStripper();
                String pdfText = pdfStripper.getText(document);
                if (pdfText.toLowerCase().contains("acme supplies")) {
                    assertThat(pdfText).contains("2024-10-01T08:30");
                    assertThat(pdfText).contains("2024-10-02T09:45");
                    assertThat(pdfText).contains("2024-10-05T16:00");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }
}
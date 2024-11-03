package be.kdg.mineralflow.warehouse.business.service.pdf;

import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

@Service
public class PdfGeneratingService {
    public static final Logger logger = Logger
            .getLogger(PdfGeneratingService.class.getName());

    private final SpringTemplateEngine templateEngine;

    public PdfGeneratingService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateEngine.setTemplateResolver(templateResolver);

    }

    public <T> void generatePdf(T dto, String variableName, String templateName, String fileName) {

        String htmlOfDto = parseDtoTemplate(dto, variableName, templateName);
        generatePdfFromHtml(htmlOfDto, fileName);
    }

    private void generatePdfFromHtml(String html, String fileName) {
        String filePath = String.format("%s%s%s", System.getProperty("user.home"), File.separator, fileName);
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private <T> String parseDtoTemplate(T dto, String variableName, String templateName) {
        Context context = new Context();
        context.setVariable(variableName, dto);
        return templateEngine.process(templateName, context);
    }

}

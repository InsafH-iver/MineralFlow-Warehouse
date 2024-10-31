package be.kdg.mineralflow.warehouse.business.domain;

import be.kdg.mineralflow.warehouse.TestContainer;
import be.kdg.mineralflow.warehouse.business.util.Invoice;
import be.kdg.mineralflow.warehouse.business.util.InvoiceLine;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InvoiceTest extends TestContainer {
    @Test
    void getTotalStorageCost() {
        //ARRANGE
        ZonedDateTime invoiceDate =ZonedDateTime.of(2024,10,30,15,58,0,0, ZoneId.of("UTC"));
        UUID resourceId1 = UUID.randomUUID();
        UUID resourceId2 = UUID.randomUUID();
        Invoice invoice = generateFullInvoice(invoiceDate, resourceId1, resourceId2);
        //ACT
        double totalStorageCost = invoice.getTotalStorageCost();
        //ASSERT
        assertEquals(totalStorageCost,357,0.001);
    }

    private static @NotNull Invoice generateFullInvoice(ZonedDateTime invoiceDate, UUID resourceId1, UUID resourceId2) {

        Resource resource1 = new Resource(resourceId1,"A resource used in testing","TestResource",5,3);
        StockPortion stockPortion1 = new StockPortion(20, invoiceDate.minusDays(12),21);
        InvoiceLine invoiceLine1 = new InvoiceLine(resource1,stockPortion1);

        Resource resource2 = new Resource(resourceId2,"A resource used in testing too","SomeOtherTestResource",200,18);
        StockPortion stockPortion2 = new StockPortion(20, invoiceDate.minusDays(5),21);
        InvoiceLine invoiceLine2 = new InvoiceLine(resource2,stockPortion2);
        Vendor vendor = new Vendor("TestVendor","teststreet 4, 2000 antwerpen");

        return new Invoice(invoiceDate.toLocalDateTime(),
                vendor,
                List.of(invoiceLine1,invoiceLine2)
        );
    }
    @Test
    void getTotalStorageCost_should_return_zero_when_there_are_no_stockPortions() {
        //ARRANGE
        ZonedDateTime invoiceDate =ZonedDateTime.of(2024,10,30,15,58,0,0, ZoneId.of("UTC"));
        UUID resourceId1 = UUID.randomUUID();
        UUID resourceId2 = UUID.randomUUID();
        Invoice invoice = generateInvoiceWithoutStock(invoiceDate, resourceId1, resourceId2);
        //ACT
        double totalStorageCost = invoice.getTotalStorageCost();
        //ASSERT
        assertEquals(totalStorageCost,0,0.001);
    }
    private static @NotNull Invoice generateInvoiceWithoutStock(ZonedDateTime invoiceDate, UUID resourceId1, UUID resourceId2) {

        Resource resource1 = new Resource(resourceId1,"A resource used in testing","TestResource",5,3);
        InvoiceLine invoiceLine1 = new InvoiceLine(resource1,null);

        Resource resource2 = new Resource(resourceId2,"A resource used in testing too","SomeOtherTestResource",200,18);
        InvoiceLine invoiceLine2 = new InvoiceLine(resource2,null);
        Vendor vendor = new Vendor("TestVendor","teststreet 4, 2000 antwerpen");

        return new Invoice(invoiceDate.toLocalDateTime(),
                vendor,
                List.of(invoiceLine1,invoiceLine2)
        );
    }
    @Test
    void getTotalStorageCost_should_return_zero_when_there_are_no_invoiceLines() {
        //ARRANGE
        ZonedDateTime invoiceDate =ZonedDateTime.of(2024,10,30,15,58,0,0, ZoneId.of("UTC"));
        UUID resourceId1 = UUID.randomUUID();
        UUID resourceId2 = UUID.randomUUID();
        Invoice invoice = generateEmptyInvoice(invoiceDate, resourceId1, resourceId2);
        //ACT
        double totalStorageCost = invoice.getTotalStorageCost();
        //ASSERT
        assertEquals(totalStorageCost,0,0.001);
    }
    private static @NotNull Invoice generateEmptyInvoice(ZonedDateTime invoiceDate, UUID resourceId1, UUID resourceId2) {

        Vendor vendor = new Vendor("TestVendor","teststreet 4, 2000 antwerpen");

        return new Invoice(invoiceDate.toLocalDateTime(),
                vendor,
                List.of()
        );
    }
}
package be.kdg.mineralflow.warehouse.presentation.controller.api;

import be.kdg.mineralflow.warehouse.TestContainer;
import be.kdg.mineralflow.warehouse.business.domain.*;
import be.kdg.mineralflow.warehouse.business.util.invoice.InvoiceLineFactory;
import be.kdg.mineralflow.warehouse.persistence.*;
import be.kdg.mineralflow.warehouse.persistence.purchase.order.PurchaseOrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
class InvoiceRestControllerTest extends TestContainer {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;
    @Autowired
    private StockPortionRepository stockPortionRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    private BuyerRepository buyerRepository;
    @Autowired
    private InvoiceLineFactory invoiceLineFactory;
    @Autowired
    private CommissionRepository commissionRepository;

    @Test
    void getInvoice_should_return_invoice() throws Exception {
        //ARRANGE
        LocalDateTime dateTime = LocalDateTime.now();
        Vendor vendor = seedDataForHappyPath(dateTime);
        UUID vendorId = vendor.getId();

        //ACT
        // ASSERT
        mockMvc.perform(
                        get("/api/invoice/{vendorId}/{dateTime}"
                                , vendorId, dateTime)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendorName", is("berend janssens")))
                .andExpect(jsonPath("$.totalStorageCost", is(300.0)))
                .andExpect(jsonPath("$.commissionCost", is(150.0)))
                .andExpect(jsonPath("$.invoiceLines[0].resource", is("hout")))
                .andExpect(jsonPath("$.invoiceLines[0].weightInTon", is(15.0)))
                .andExpect(jsonPath("$.invoiceLines[0].storageCostPerDayPerTon", is(5.0)))
                .andExpect(jsonPath("$.invoiceLines[0].daysInStorage", is(4)))
                .andExpect(jsonPath("$.invoiceLines[0].storageCost", is(300.0)));

    }
    @Test
    void getInvoice_should_return_not_found_when_no_invoice_was_found() throws Exception {
        //ARRANGE
        LocalDateTime dateTime = LocalDateTime.now();
        UUID vendorId = UUID.randomUUID();

        //ACT
        // ASSERT
        mockMvc.perform(
                        get("/api/invoice/{vendorId}/{dateTime}"
                                , vendorId, dateTime)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound());
    }

    private Vendor seedDataForHappyPath(LocalDateTime dateTime) {
        Buyer buyer = new Buyer("somewhere over the rainbow", "freddy janssens");
        Vendor vendor = new Vendor("berend janssens","somewhere before the rainbow");
        Resource resource = new Resource("Hout","hout",10,5);
        PurchaseOrder po = new PurchaseOrder(
                "PO12121212",
                "VES12121212",
                Status.COMPLETED, List.of(new OrderLine(15,10, resource)),
                vendor,
                buyer
        );
        Commission commission = new Commission(po,150);
        commission.setCreationDate(dateTime.toLocalDate().minusDays(1));
        resourceRepository.save(resource);
        vendorRepository.save(vendor);
        StockPortion stockPortion = new StockPortion(15,ZonedDateTime.of(dateTime, ZoneId.of("UTC")).minusDays(4),4);
        Invoice invoice = new Invoice(dateTime,
                vendor,
                List.of(invoiceLineFactory.createInvoiceLine(resource,stockPortion)));
        stockPortionRepository.save(stockPortion);
        buyerRepository.save(buyer);
        purchaseOrderRepository.save(po);
        commissionRepository.save(commission);
        invoiceRepository.save(invoice);
        return vendor;
    }
}
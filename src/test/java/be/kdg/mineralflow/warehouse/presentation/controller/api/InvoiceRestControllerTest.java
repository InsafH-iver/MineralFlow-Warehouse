package be.kdg.mineralflow.warehouse.presentation.controller.api;

import be.kdg.mineralflow.warehouse.TestContainer;
import be.kdg.mineralflow.warehouse.business.domain.*;
import be.kdg.mineralflow.warehouse.business.service.InvoiceService;
import be.kdg.mineralflow.warehouse.business.util.Status;
import be.kdg.mineralflow.warehouse.config.ConfigProperties;
import be.kdg.mineralflow.warehouse.persistence.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
class InvoiceRestControllerTest extends TestContainer {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;
    @Autowired
    private CommissionRepository commissionRepository;
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    private BuyerRepository buyerRepository;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private ConfigProperties configProperties;
    @Autowired
    private WarehouseRepository warehouseRepository;

    @Test
    void getInvoice_should_return_invoice() throws Exception {
        //ARRANGE
        LocalDateTime dateTime = LocalDateTime.now();
        Vendor vendor = seedDataForHappyPath(dateTime);
        UUID vendorId = vendor.getId();
        invoiceService.createInvoices();

        //ACT
        // ASSERT
        MvcResult result = mockMvc.perform(
                        get("/api/invoice/{vendorId}/{dateTime}"
                                , vendorId, dateTime)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();
        String invoice = result.getResponse().getContentAsString();
        assertThat(invoice).containsIgnoringCase(vendor.getName());
        assertThat(invoice).containsIgnoringCase("commissionCost\":150");
        assertThat(invoice).containsIgnoringCase("totalStorageCost\":16");
        assertThat(invoice).containsIgnoringCase("weightInTon\":412");
        assertThat(invoice).containsIgnoringCase("vendorName\":\"berend janssens\"");

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
                Status.FULFILLED, List.of(new OrderLine(15,10, resource)),
                vendor,
                buyer
        );
        Commission commission = new Commission(po,150);
        commission.setCreationDate(dateTime);
        resourceRepository.saveAndFlush(resource);
        Vendor resultVendor = vendorRepository.save(vendor);
        Warehouse warehouse = new Warehouse(2,
                123,configProperties.getWarehouseMaxCapacityInTon());
        warehouse.setVendor(resultVendor);
        warehouse.setResource(resource);
        warehouse.setStockPortions(List.of(new StockPortion(412, ZonedDateTime.now().minusDays(4),4)));
        warehouseRepository.save(warehouse);
        buyerRepository.save(buyer);
        purchaseOrderRepository.save(po);
        commissionRepository.save(commission);
        return vendor;
    }
}
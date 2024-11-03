package be.kdg.mineralflow.warehouse.presentation.controller.api;

import be.kdg.mineralflow.warehouse.TestContainer;
import be.kdg.mineralflow.warehouse.business.domain.*;
import be.kdg.mineralflow.warehouse.business.service.InvoiceService;
import be.kdg.mineralflow.warehouse.business.util.Status;
import be.kdg.mineralflow.warehouse.config.ConfigProperties;
import be.kdg.mineralflow.warehouse.persistence.*;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.InvoiceDto;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.OrderLineDto;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.PartyDto;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.PurchaseOrderDto;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.InstanceOf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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
    @Autowired
    private InvoiceRestController invoiceRestController;

    @Test
    void getInvoice_should_return_invoice() throws Exception {
        //ARRANGE
        Vendor vendor = seedDataForHappyPath();
        UUID vendorId = vendor.getId();
        LocalDateTime dateTime = LocalDateTime.now();
        invoiceService.createInvoices();
        //ACT
        MvcResult some = mockMvc.perform(
                        get("/api/invoice/{vendorId}/{dateTime}"
                                , vendorId, dateTime)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(result -> assertInstanceOf(InvoiceDto.class,result)).andReturn();
        // ASSERT

    }

    private Vendor seedDataForHappyPath() {
        Buyer buyer = new Buyer("somewhere over the rainbow", "freddy janssens");
        Vendor vendor = new Vendor("somewhere over the rainbow", "freddy janssens");
        Resource resource = new Resource("Hout","hout",10,5);
        PurchaseOrder po = new PurchaseOrder(
                "PO12121212",
                "VES12121212",
                Status.FULFILLED, List.of(new OrderLine(15,10, resource)),
                vendor,
                buyer
        );
        Commission commission = new Commission(po,150);
        resourceRepository.saveAndFlush(resource);
        Vendor resultVendor = vendorRepository.save(vendor);
        Warehouse warehouse = new Warehouse(2,
                123,configProperties.getWarehouseMaxCapacityInTon());
        warehouse.setVendor(resultVendor);
        warehouseRepository.save(warehouse);
        buyerRepository.save(buyer);
        purchaseOrderRepository.save(po);
        commissionRepository.save(commission);
        return vendor;
    }
}
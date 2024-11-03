package be.kdg.mineralflow.warehouse.presentation.controller.api;

import be.kdg.mineralflow.warehouse.TestContainer;
import be.kdg.mineralflow.warehouse.business.domain.PurchaseOrder;
import be.kdg.mineralflow.warehouse.business.domain.Status;
import be.kdg.mineralflow.warehouse.persistence.purchase.order.PurchaseOrderRepository;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.purchase.order.PurchaseOrderStatusDto;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.purchase.order.PurchaseOrdersOverviewDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class PurchaseOrdersOverviewRestControllerTest extends TestContainer {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Test
    void getPurchaseOrdersOverview_Should_Return_No_Completed_Po_And_One_Open_Po() throws Exception {
        //ARRANGE
        PurchaseOrderStatusDto purchaseOrderStatusDto =
                new PurchaseOrderStatusDto(
                        "PO2345",
                        "VE123456",
                        Status.WAITING
                );
        PurchaseOrdersOverviewDto dto =
                new PurchaseOrdersOverviewDto(
                        List.of(purchaseOrderStatusDto),
                        List.of()
                );

        String overviewAsJson = objectMapper.writeValueAsString(dto);
        //ACT
        // ASSERT
        mockMvc.perform(
                        get("/api/purchase-orders-overview")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(header().string(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON.toString()))
                .andExpect(content().string(overviewAsJson));
    }

    @Test
    void getPurchaseOrdersOverview_Should_Return_No_Completed_Po_And_No_Open_Po() throws Exception {
        //ARRANGE
        deleteAllPurchaseOrders();
        PurchaseOrdersOverviewDto dto =
                new PurchaseOrdersOverviewDto(
                        List.of(),
                        List.of()
                );

        String overviewAsJson = objectMapper.writeValueAsString(dto);
        //ACT
        // ASSERT
        mockMvc.perform(
                        get("/api/purchase-orders-overview")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(header().string(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON.toString()))
                .andExpect(content().string(overviewAsJson));
    }

    @Test
    void getPurchaseOrdersOverview_Should_Return_One_Completed_Po_And_No_Open_Po() throws Exception {
        //ARRANGE
        deleteAllPurchaseOrders();
        String poNumber = "PO23452";
        String veNumber = "VE123456";
        Status status = Status.COMPLETED;

        PurchaseOrder purchaseOrder = new PurchaseOrder(
                UUID.randomUUID(),
                null,
                poNumber,
                status,
                null,
                veNumber
        );
        addPurchaseOrder(purchaseOrder);

        PurchaseOrderStatusDto purchaseOrderStatusDto =
                new PurchaseOrderStatusDto(
                        poNumber,
                        veNumber,
                        status
                );

        PurchaseOrdersOverviewDto dto =
                new PurchaseOrdersOverviewDto(
                        List.of(),
                        List.of(purchaseOrderStatusDto)
                );

        String overviewAsJson = objectMapper.writeValueAsString(dto);
        //ACT
        // ASSERT
        mockMvc.perform(
                        get("/api/purchase-orders-overview")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(header().string(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON.toString()))
                .andExpect(content().string(overviewAsJson));
    }

    private void deleteAllPurchaseOrders() {
        purchaseOrderRepository.deleteAll();
    }

    private void addPurchaseOrder(PurchaseOrder purchaseOrder) {
        purchaseOrderRepository.save(purchaseOrder);
    }
}
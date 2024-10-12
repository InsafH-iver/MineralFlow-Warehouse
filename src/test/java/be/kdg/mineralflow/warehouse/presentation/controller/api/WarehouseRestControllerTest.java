package be.kdg.mineralflow.warehouse.presentation.controller.api;

import be.kdg.mineralflow.warehouse.TestContainer;
import be.kdg.mineralflow.warehouse.exception.NoItemFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class WarehouseRestControllerTest extends TestContainer {
    @Autowired
    private MockMvc mockMvc;


    @Test
    void getWarehouseNumberByVendorAndResourceId_When_Warehouse_Exists() throws Exception {
        //ARRANGE
        String warehouseNumber = "2";
        UUID vendorId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID resourceId = UUID.fromString("11111111-1111-1111-1111-111111111112");

        //ACT
        // ASSERT
        mockMvc.perform(
                        get("/api/warehouses/{vendorId}/{resourceId}"
                                , vendorId, resourceId)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(header().string(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON.toString()))
                .andExpect(content().string(warehouseNumber));
    }

    @Test
    void getWarehouseNumberByVendorAndResourceId_When_Warehouse_Does_Not_ExistNoItemFoundException_Thrown() throws Exception {
        //ARRANGE
        UUID vendorId = UUID.fromString("11111111-1111-1111-1111-111111111120");
        UUID resourceId = UUID.fromString("11111111-1111-1111-1111-111111111121");

        //ACT
        // ASSERT
        mockMvc.perform(
                        get("/api/warehouses/{vendorId}/{resourceId}"
                                , vendorId, resourceId)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(NoItemFoundException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals(String.format("The warehouse of vendor with id %s and for resource with id %s, was not found",vendorId, resourceId), Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }
}
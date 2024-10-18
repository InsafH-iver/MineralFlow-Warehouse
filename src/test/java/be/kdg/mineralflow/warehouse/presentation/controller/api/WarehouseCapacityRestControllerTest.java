package be.kdg.mineralflow.warehouse.presentation.controller.api;

import be.kdg.mineralflow.warehouse.TestContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class WarehouseCapacityRestControllerTest extends TestContainer {
    @Autowired
    MockMvc mockMvc;

    @Test
    void isWarehouseFull_should_return_false_if_warehouse_is_not_full() throws Exception {
        //ARRANGE
        String resourceName = "Gips";
        String vendorName = "Acme Supplies";

        //ACT
        var some = mockMvc.perform(
                        get("/api/warehouseCapacity/{vendorName}/{resourceName}"
                                , vendorName, resourceName)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(header().string(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON.toString())).andReturn();
        // ASSERT
        Assertions.assertFalse(Boolean.parseBoolean(some.getResponse().getContentAsString()));
    }
    @Test
    void isWarehouseFull_should_return_true_if_warehouse_is_full() throws Exception {
        //ARRANGE
        String resourceName = "Beton";
        String vendorName = "Atlaan";

        //ACT
        var some = mockMvc.perform(
                        get("/api/warehouseCapacity/{vendorName}/{resourceName}"
                                , vendorName, resourceName)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(header().string(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON.toString())).andReturn();
        // ASSERT
        Assertions.assertTrue(Boolean.parseBoolean(some.getResponse().getContentAsString()));
    }
}
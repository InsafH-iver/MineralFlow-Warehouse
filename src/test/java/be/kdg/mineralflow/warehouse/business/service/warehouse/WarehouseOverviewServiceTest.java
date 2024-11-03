package be.kdg.mineralflow.warehouse.business.service.warehouse;

import be.kdg.mineralflow.warehouse.TestContainer;
import be.kdg.mineralflow.warehouse.exception.NoItemFoundException;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.StockPortionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class WarehouseOverviewServiceTest extends TestContainer {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getWarehouseOverview_SHould_Give_Overview_When_Warehouse_Exists_And_Has_StockPortions() throws Exception {
        //ARRANGE
        UUID warehouseId = UUID.fromString("11111111-1111-1111-1111-111111111116");
        String usedCapacity = "450";

        StockPortionDto stockPortionDto1 = new StockPortionDto(
                ZonedDateTime.parse("2024-10-04T11:15:00+00:00"),
                8.7,
                8.7,
                28.75
        );
        StockPortionDto stockPortionDto2 = new StockPortionDto(
                ZonedDateTime.parse("2024-10-03T14:00:00+00:00"),
                20.0,
                20.0,
                22.50
        );

        String dto1AsJson = objectMapper.writeValueAsString(stockPortionDto1);
        String dto2AsJson = objectMapper.writeValueAsString(stockPortionDto2);

        //ACT
        var warehouseOverview = mockMvc.perform(
                        get("/api/warehouses-overview/{warehouseId}", warehouseId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(header().string(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON.toString())).andReturn();
        //ASSERT
        String content = warehouseOverview.getResponse().getContentAsString();
        assertThat(content).contains(dto1AsJson);
        assertThat(content).contains(dto2AsJson);
        assertThat(content).contains(usedCapacity);
    }

    @Test
    void getWarehouseOverview_Should_Throw_exception_When_Warehouse_Does_Not_Exists() throws Exception {
        //ARRANGE
        UUID warehouseId = UUID.fromString("00000000-1111-1111-1111-111111111116");

        //ACT
        // ASSERT
        mockMvc.perform(
                        get("/api/warehouses-overview/{warehouseId}", warehouseId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(NoItemFoundException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals(String.format("No warehouse found with id %s",
                        warehouseId), Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void getWarehouseOverview_SHould_Give_Overview_With_No_StockPortions_When_Warehouse_Exists_And_Has_No_StockPortions() throws Exception {
        //ARRANGE
        UUID warehouseId = UUID.fromString("33333333-1111-1111-1111-111111111202");
        String usedCapacity = "0";
        String emptyStockPortionDtos = "\"ListOfStockPortion\":[]";

        //ACT
        var warehouseOverview = mockMvc.perform(
                        get("/api/warehouses-overview/{warehouseId}", warehouseId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(header().string(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON.toString())).andReturn();
        //ASSERT
        String content = warehouseOverview.getResponse().getContentAsString();
        assertThat(content).contains(usedCapacity);
        assertThat(content).contains(emptyStockPortionDtos);
    }
}
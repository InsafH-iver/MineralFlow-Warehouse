package be.kdg.mineralflow.warehouse.presentation.controller.api;

import be.kdg.mineralflow.warehouse.TestContainer;
import be.kdg.mineralflow.warehouse.business.domain.DeliveryTicket;
import be.kdg.mineralflow.warehouse.business.domain.Warehouse;
import be.kdg.mineralflow.warehouse.exception.IncorrectDomainException;
import be.kdg.mineralflow.warehouse.exception.NoItemFoundException;
import be.kdg.mineralflow.warehouse.persistence.WarehouseRepository;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.delivery.DeliveryDataDto;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.delivery.DeliveryTicketDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class DeliveryTicketRestControllerTest extends TestContainer {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WarehouseRepository warehouseRepository;

    @Test
    void addDeliveryTicket_Should_Succeed_When_Warehouse_Exists_And_UnloadingRequestId_Is_Unique() throws Exception {
        //ARRANGE
        UUID warehouseId = UUID.fromString("11111111-1111-1111-1111-111111111116");
        UUID resourceId = UUID.fromString("11111111-1111-1111-1111-111111111114");
        ZonedDateTime deliveryTime = ZonedDateTime.of(2024, 2, 2, 2, 2, 2, 0, ZoneOffset.UTC);
        UUID vendorId = UUID.fromString("11111111-1111-1111-1111-111111111115");
        UUID unloadingRequestId = UUID.fromString("34011111-1111-1111-1111-111111111116");
        DeliveryDataDto deliveryDataDto = new DeliveryDataDto(vendorId, resourceId, deliveryTime, unloadingRequestId);
        DeliveryTicketDto deliveryTicketDto = new DeliveryTicketDto(
                "Beton", deliveryTime, 3);

        String jsonDeliveryDataDto = objectMapper.writeValueAsString(deliveryDataDto);
        String jsonDeliveryTicketDto = objectMapper.writeValueAsString(deliveryTicketDto);

        //ACT
        // ASSERT
        mockMvc.perform(
                        post("/api/deliveryTickets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonDeliveryDataDto)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(header().string(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON.toString()))
                .andExpect(content().string(jsonDeliveryTicketDto));

        Warehouse warehouse = warehouseRepository.findWarehouseWithTicketsById(warehouseId).get();
        List<DeliveryTicket> deliveryTickets = warehouse.getDeliveryTickets();
        assertThat(deliveryTickets.size()).isEqualTo(1);
        DeliveryTicket actualDeliveryTicket = deliveryTickets.getFirst();
        assertThat(actualDeliveryTicket.getDeliveryTime()).isEqualTo(deliveryTime);
        assertThat(actualDeliveryTicket.getUnloadingRequestId()).isEqualTo(unloadingRequestId);
    }

    @Test
    void addDeliveryTicket_Should_Throw_Exception_When_Warehouse_Does_Not_Exist() throws Exception {
        //ARRANGE
        UUID resourceId = UUID.fromString("20411111-1111-1111-1111-111111111114");
        ZonedDateTime deliveryTime = ZonedDateTime.of(2024, 2, 2, 2, 2, 2, 0, ZoneOffset.UTC);
        UUID vendorId = UUID.fromString("11111111-1111-1111-1111-111111111115");
        UUID unloadingRequestId = UUID.fromString("34011111-1111-1111-1111-111111111116");
        DeliveryDataDto deliveryDataDto = new DeliveryDataDto(vendorId, resourceId, deliveryTime, unloadingRequestId);

        String jsonDeliveryDataDto = objectMapper.writeValueAsString(deliveryDataDto);

        //ACT
        // ASSERT
        mockMvc.perform(
                        post("/api/deliveryTickets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonDeliveryDataDto)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(NoItemFoundException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals(String.format("No warehouse found for vendor ID %s with resource ID %s", vendorId, resourceId), Objects.requireNonNull(result.getResolvedException()).getMessage()));

    }

    @Test
    void addDeliveryTicket_Should_Throw_Exception_When_Warehouse_Exists_But_UnloadingRequestId_Is_Not_unique() throws Exception {
        //ARRANGE
        UUID resourceId = UUID.fromString("11111111-1111-1111-1111-111111111114");
        ZonedDateTime deliveryTime = ZonedDateTime.of(2024, 2, 2, 2, 2, 2, 0, ZoneOffset.UTC);
        UUID vendorId = UUID.fromString("11111111-1111-1111-1111-111111111115");
        UUID unloadingRequestId = UUID.fromString("11111111-1111-1111-1111-111111111131");
        DeliveryDataDto deliveryDataDto = new DeliveryDataDto(vendorId, resourceId, deliveryTime, unloadingRequestId);

        String jsonDeliveryDataDto = objectMapper.writeValueAsString(deliveryDataDto);

        //ACT
        // ASSERT
        mockMvc.perform(
                        post("/api/deliveryTickets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonDeliveryDataDto)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(IncorrectDomainException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals(String.format("The delivery ticket for unloading " +
                                "request id %s could not be made because it already exists", unloadingRequestId),
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));

    }
}
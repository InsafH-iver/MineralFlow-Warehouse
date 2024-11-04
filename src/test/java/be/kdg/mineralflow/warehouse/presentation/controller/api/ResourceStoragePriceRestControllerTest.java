package be.kdg.mineralflow.warehouse.presentation.controller.api;

import be.kdg.mineralflow.warehouse.TestContainer;
import be.kdg.mineralflow.warehouse.business.domain.Resource;
import be.kdg.mineralflow.warehouse.persistence.ResourceRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class ResourceStoragePriceRestControllerTest extends TestContainer {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResourceRepository resourceRepository;

    @Test
    void putResourceStoragePrice_should_return_created_and_resource_dto() throws Exception {
        //ARRANGE
        double newStoragePrice = 100.50;
        Resource resource = new Resource("cement","cement",10,5);
        UUID resourceId = UUID.randomUUID();

        Mockito.when(resourceRepository.getResourceById(resourceId))
                .thenReturn(Optional.of(resource));
        Mockito.when(resourceRepository.save(Mockito.any(Resource.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // ACT
        // ASSERT
        mockMvc.perform(put("/api/resourceStoragePrice/{resourceId}", resourceId)
                        .param("newStoragePrice", String.valueOf(newStoragePrice))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(resource.getName())))
                .andExpect(jsonPath("$.sellingPricePerTon", is(resource.getSellingPricePerTon())))
                .andExpect(jsonPath("$.storagePricePerTonPerDay", is(newStoragePrice)));
    }
    @Test
    void putResourceStoragePrice_should_return_badRequest_when_storagePrice_is_negative() throws Exception {
        //ARRANGE
        UUID resourceId = UUID.randomUUID();
        double invalidStoragePrice = -50.0;

        // ACT
        // ASSERT
        mockMvc.perform(put("/api/resourceStoragePrice/{resourceId}", resourceId)
                        .param("newStoragePrice", String.valueOf(invalidStoragePrice))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void putResourceStoragePrice_should_return_notFound_when_resource_does_not_exist() throws Exception {
        //ARRANGE
        UUID nonExistentResourceId = UUID.randomUUID();
        double newStoragePrice = 100.0;

        Mockito.when(resourceRepository.getResourceById(nonExistentResourceId))
                .thenReturn(Optional.empty());

        // ACT
        // ASSERT
        mockMvc.perform(put("/api/resourceStoragePrice/{resourceId}", nonExistentResourceId)
                        .param("newStoragePrice", String.valueOf(newStoragePrice))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    void putResourceSellingPrice_should_return_badRequest_when_storagePrice_missing() throws Exception {
        // ARRANGE
        UUID resourceId = UUID.randomUUID();

        // ACT & ASSERT
        mockMvc.perform(put("/api/resourceSellingPrice/{resourceId}", resourceId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    void putResourceStoragePrice_should_return_badRequest_when_storagePrice_is_zero() throws Exception {
        //ARRANGE
        UUID resourceId = UUID.randomUUID();
        double zeroStoragePrice = 0.0;

        // ACT
        // ASSERT
        mockMvc.perform(put("/api/resourceStoragePrice/{resourceId}", resourceId)
                        .param("newStoragePrice", String.valueOf(zeroStoragePrice))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
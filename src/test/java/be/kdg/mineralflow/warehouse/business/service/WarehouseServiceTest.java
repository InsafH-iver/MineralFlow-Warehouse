package be.kdg.mineralflow.warehouse.business.service;

import be.kdg.mineralflow.warehouse.TestContainer;
import be.kdg.mineralflow.warehouse.business.domain.Warehouse;
import be.kdg.mineralflow.warehouse.exception.NoItemFoundException;
import be.kdg.mineralflow.warehouse.persistence.WarehouseRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class WarehouseServiceTest extends TestContainer {

    @Autowired
    private WarehouseService warehouseService;
    @MockBean
    private WarehouseRepository warehouseRepository;


    @Test
    void getWarehouseNumberByVendorAndResourceId_When_Warehouse_Exists() {
        //ARRANGE
        int warehouseNumber = 2;
        Warehouse warehouse = new Warehouse(UUID.randomUUID(), warehouseNumber,
                123);
        UUID vendorId = UUID.randomUUID();
        UUID resourceId = UUID.randomUUID();

        Mockito.when(warehouseRepository.
                        findFirstByVendorIdAndResourceId(vendorId, resourceId))
                .thenReturn(Optional.of(warehouse));
        //ACT
        int actualWarehouseNumber = warehouseService.getWarehouseNumberByVendorAndResourceId(vendorId, resourceId);

        //ASSERT
        assertThat(actualWarehouseNumber).isEqualTo(warehouseNumber);
        Mockito.verify(warehouseRepository, Mockito.times(1)).findFirstByVendorIdAndResourceId(vendorId, resourceId);
    }

    @Test
    void getWarehouseNumberByVendorAndResourceId_When_Warehouse_Does_Not_Exist_NoItemFoundException_Thrown() {
        //ARRANGE
        UUID vendorId = UUID.randomUUID();
        UUID resourceId = UUID.randomUUID();

        Mockito.when(warehouseRepository.
                        findFirstByVendorIdAndResourceId(vendorId, resourceId))
                .thenReturn(Optional.empty());

        //ACT
        // ASSERT
        assertThrows(NoItemFoundException.class, () -> warehouseService.getWarehouseNumberByVendorAndResourceId(vendorId, resourceId));
    }
}
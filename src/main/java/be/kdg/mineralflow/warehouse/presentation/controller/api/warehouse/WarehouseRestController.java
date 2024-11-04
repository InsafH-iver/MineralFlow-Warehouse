package be.kdg.mineralflow.warehouse.presentation.controller.api.warehouse;

import be.kdg.mineralflow.warehouse.business.service.warehouse.WarehouseService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseRestController {
    public static final Logger logger = Logger
            .getLogger(WarehouseRestController.class.getName());
    private final WarehouseService warehouseService;

    public WarehouseRestController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping("/{vendorId}/{resourceId}")
    @ResponseStatus(HttpStatus.OK)
    public int getWarehouseNumberByVendorAndResourceId(
            @PathVariable UUID vendorId, @PathVariable UUID resourceId) {
        logger.info(String.format("Call was made to get warehouse number by vendor id %s and resource id %s", vendorId, resourceId));
        return warehouseService.getWarehouseNumberByVendorAndResourceId(vendorId, resourceId);
    }
}

package be.kdg.mineralflow.warehouse.presentation.controller.api;

import be.kdg.mineralflow.warehouse.business.service.WarehouseCapacityService;
import be.kdg.mineralflow.warehouse.business.service.WarehouseService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/warehouseCapacity")
public class WarehouseCapacityRestController {
    public static final Logger logger = Logger
            .getLogger(WarehouseCapacityRestController.class.getName());
    private final WarehouseCapacityService warehouseCapacityService;

    public WarehouseCapacityRestController(WarehouseCapacityService warehouseCapacityService) {
        this.warehouseCapacityService = warehouseCapacityService;
    }

    @GetMapping("/{vendorId}/{resourceId}")
    public boolean isWarehouseFull(@PathVariable("vendorId") UUID vendorId, @PathVariable("resourceId") UUID resourceId) {
        return warehouseCapacityService.isWarehouseFull(vendorId,resourceId);
    }
}

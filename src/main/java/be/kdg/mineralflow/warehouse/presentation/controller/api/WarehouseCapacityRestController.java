package be.kdg.mineralflow.warehouse.presentation.controller.api;

import be.kdg.mineralflow.warehouse.business.service.WarehouseService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/warehouseCapacity")
public class WarehouseCapacityRestController {
    public static final Logger logger = Logger
            .getLogger(WarehouseCapacityRestController.class.getName());
    private final WarehouseService warehouseService;

    public WarehouseCapacityRestController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping("/{vendorId}/{resourceId}")
    public boolean isWarehouseFull(@PathVariable("vendorId") UUID vendorId, @PathVariable("resourceId") UUID resourceId) {
        return warehouseService.isWarehouseFull(vendorId,resourceId);
    }
}

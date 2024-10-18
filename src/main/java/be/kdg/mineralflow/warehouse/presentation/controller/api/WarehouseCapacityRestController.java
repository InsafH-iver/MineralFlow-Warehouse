package be.kdg.mineralflow.warehouse.presentation.controller.api;

import be.kdg.mineralflow.warehouse.business.service.WarehouseService;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{vendorName}/{resourceName}")
    public boolean isWarehouseFull(@PathVariable("vendorName") String vendorName, @PathVariable("resourceName") String resourceName) {
        return warehouseService.isWarehouseFull(vendorName,resourceName);
    }
}

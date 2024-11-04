package be.kdg.mineralflow.warehouse.presentation.controller.api.warehouse;

import be.kdg.mineralflow.warehouse.business.service.warehouse.WarehouseCapacityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        boolean warehouseFull = warehouseCapacityService.isWarehouseFull(vendorId, resourceId);
        logger.info(String.format("WarehouseCapacityRestController: isWarehouseFull was called and returned %s",warehouseFull));
        return warehouseFull;
    }
}

package be.kdg.mineralflow.warehouse.presentation.controller.api;

import be.kdg.mineralflow.warehouse.business.service.warehouse.WarehouseOverviewService;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.WarehouseOverviewDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/warehouses-overview")
public class WarehouseOverviewRestController {
    public static final Logger logger = Logger
            .getLogger(WarehouseOverviewRestController.class.getName());

    private final WarehouseOverviewService warehouseOverviewService;

    public WarehouseOverviewRestController(WarehouseOverviewService warehouseOverviewService) {
        this.warehouseOverviewService = warehouseOverviewService;
    }

    @GetMapping("/{warehouseId}")
    @ResponseStatus(HttpStatus.OK)
    public WarehouseOverviewDto getWarehouseOverview(@PathVariable("warehouseId") UUID warehouseId) {
        logger.info(String.format("Call was made to fetch warehouse information" +
                " of warehouse with id %s", warehouseId));
        return warehouseOverviewService.getWarehouseOverview(warehouseId);
    }
}

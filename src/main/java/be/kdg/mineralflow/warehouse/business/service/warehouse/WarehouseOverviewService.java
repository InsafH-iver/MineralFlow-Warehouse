package be.kdg.mineralflow.warehouse.business.service.warehouse;

import be.kdg.mineralflow.warehouse.business.domain.Warehouse;
import be.kdg.mineralflow.warehouse.business.util.ExceptionHandlingHelper;
import be.kdg.mineralflow.warehouse.persistence.WarehouseRepository;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.StockPortionDto;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.WarehouseOverviewDto;
import be.kdg.mineralflow.warehouse.presentation.controller.mapper.StockPortionMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class WarehouseOverviewService {
    public static final Logger logger = Logger
            .getLogger(WarehouseOverviewService.class.getName());
    private final WarehouseRepository warehouseRepository;
    private final StockPortionMapper stockPortionMapper = StockPortionMapper.INSTANCE;

    public WarehouseOverviewService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Transactional
    public WarehouseOverviewDto getWarehouseOverview(UUID warehouseId) {
        logger.info(String.format("Starting process to fetch warehouse information" +
                " of warehouse with id %s", warehouseId));

        Warehouse warehouse = getWarehouse(warehouseId);
        List<StockPortionDto> mappedStockPortions =
                stockPortionMapper.mapStockPortions(warehouse.getStockPortions());
        WarehouseOverviewDto warehouseOverviewDto =
                new WarehouseOverviewDto(warehouse.getUsedCapacityInTon(), mappedStockPortions);

        logger.info(String.format("Successfully fetched warehouse information" +
                " of warehouse with id %s", warehouseId));
        return warehouseOverviewDto;
    }

    private Warehouse getWarehouse(UUID warehouseId) {
        return warehouseRepository.findById(warehouseId)
                .orElseThrow(() ->
                        ExceptionHandlingHelper.logAndThrowNotFound(
                                "No warehouse found with id %s",
                                warehouseId
                        ));
    }
}

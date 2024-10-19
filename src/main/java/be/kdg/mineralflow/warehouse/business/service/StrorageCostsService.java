package be.kdg.mineralflow.warehouse.business.service;

import be.kdg.mineralflow.warehouse.business.domain.Vendor;
import be.kdg.mineralflow.warehouse.business.domain.Warehouse;
import be.kdg.mineralflow.warehouse.persistence.WarehouseRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StrorageCostsService {

    private final WarehouseRepository warehouseRepository;

    public StrorageCostsService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void createInvoices(){
        ZonedDateTime now = ZonedDateTime.now();
        List<Warehouse> warehouses = warehouseRepository.findAll();
        Map<Vendor, Double> storageCostPerVendor = warehouses.stream()
                .collect(Collectors.groupingBy(
                        Warehouse::getVendor,
                        Collectors.summingDouble(w -> w.getStorageCost(now))
                ));
    }
}

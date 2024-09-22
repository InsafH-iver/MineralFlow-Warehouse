package be.kdg.mineralflow.warehouse.business.domain;

import java.util.List;

public class Warehouse {
    private int warehouseNumber;
    private double usedCapacityInTon;

    private Vendor vendor;
    private List<StockPortion> stockPortions;
    private Resource resource;
}

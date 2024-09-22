package be.kdg.mineralflow.warehouse.business.domain;

import java.util.List;

public class Resource {
    private String name;
    private double storagePricePerTonPerDay;
    private double sellingPricePerTon;
    private String description;

    private List<OrderLine> orderLines;
    private List<Warehouse> warehouses;
}

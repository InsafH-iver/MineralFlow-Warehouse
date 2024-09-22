package be.kdg.mineralflow.warehouse.business.domain;

import java.util.List;

public class Vendor {
    private int vendorId;
    private String name;
    private String address;

    private List<PurchaseOrder> purchaseOrders;
    private List<Warehouse> warehouses;
}

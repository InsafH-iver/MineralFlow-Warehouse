package be.kdg.mineralflow.warehouse.business.domain;

import java.util.List;

public class Buyer {
    private int buyerId;
    private String name;
    private String address;
    private List<PurchaseOrder> purchaseOrders;
}

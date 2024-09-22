package be.kdg.mineralflow.warehouse.business.domain;

import java.util.Date;

public class PurchaseOrder {
    private int purchaseOrderNumber;
    private String name;
    private Date date;
    private int vesselNumber;
    private boolean hasBeenFulfilled;

    private OrderLine orderLine;
    private Buyer buyer;
    private Vendor vendor;
}

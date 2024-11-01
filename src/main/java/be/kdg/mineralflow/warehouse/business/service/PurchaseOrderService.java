package be.kdg.mineralflow.warehouse.business.service;

import be.kdg.mineralflow.warehouse.business.domain.*;
import be.kdg.mineralflow.warehouse.business.util.UnitConverter;
import be.kdg.mineralflow.warehouse.persistence.PurchaseOrderRepository;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.OrderLineDto;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.PurchaseOrderDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.logging.Logger;

@Service
public class PurchaseOrderService {
    public static final Logger logger = Logger
            .getLogger(PurchaseOrderService.class.getName());
    private final UnitConverter unitConverter;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final VendorService vendorService;
    private final BuyerService buyerService;
    private final ResourceService resourceService;

    public PurchaseOrderService(UnitConverter unitConverter, PurchaseOrderRepository purchaseOrderRepository, VendorService vendorService, BuyerService buyerService, ResourceService resourceService) {
        this.unitConverter = unitConverter;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.vendorService = vendorService;
        this.buyerService = buyerService;
        this.resourceService = resourceService;
    }

    public void addPurchaseOrder(@Valid PurchaseOrderDto purchaseOrderDto){
        logger.info(String.format("addPurchaseOrder was called with purchaseOrderDto %s",purchaseOrderDto));
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        /*
        purchaseOrder.setOrderLines(
                purchaseOrderDto.orderLines().stream()
                        .map(this::createOrderLine).toList());

        UUID vendorId = UUID.fromString(purchaseOrderDto.sellerParty().uuid());
        Vendor vendor = vendorService.getVendorById(vendorId);
        purchaseOrder.setVendor(vendor);

        UUID buyerId = UUID.fromString(purchaseOrderDto.customerParty().uuid());
        Buyer buyer = buyerService.getBuyerById(buyerId);
        purchaseOrder.setBuyer(buyer);

        purchaseOrderRepository.save(purchaseOrder);

         */
    }
    private OrderLine createOrderLine(OrderLineDto orderLineDto){
        Resource resource = resourceService.getResourceByName(orderLineDto.description());
        OrderLine orderLine = new OrderLine();
        orderLine.setResource(resource);
        double amountInTon =unitConverter.convertToTonnes(orderLineDto.quantity(),orderLineDto.uom());
        orderLine.setAmountInTon(amountInTon);
        orderLine.setSellingPricePerTon(resource.getSellingPricePerTon());
        return orderLine;
    }
}

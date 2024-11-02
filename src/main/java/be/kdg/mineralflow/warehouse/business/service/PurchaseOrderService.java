package be.kdg.mineralflow.warehouse.business.service;

import be.kdg.mineralflow.warehouse.business.domain.*;
import be.kdg.mineralflow.warehouse.business.util.ExceptionHandlingHelper;
import be.kdg.mineralflow.warehouse.business.util.Status;
import be.kdg.mineralflow.warehouse.business.util.UnitConverter;
import be.kdg.mineralflow.warehouse.persistence.BuyerRepository;
import be.kdg.mineralflow.warehouse.persistence.PurchaseOrderRepository;
import be.kdg.mineralflow.warehouse.persistence.ResourceRepository;
import be.kdg.mineralflow.warehouse.persistence.VendorRepository;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.OrderLineDto;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.PurchaseOrderDto;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.logging.Logger;

@Service
public class PurchaseOrderService {
    public static final Logger logger = Logger
            .getLogger(PurchaseOrderService.class.getName());
    private final UnitConverter unitConverter;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderValidationService purchaseOrderValidationService;
    private final VendorRepository vendorRepository;
    private final BuyerRepository buyerRepository;
    private final ResourceRepository resourceRepository;

    public PurchaseOrderService(UnitConverter unitConverter, PurchaseOrderRepository purchaseOrderRepository, PurchaseOrderValidationService purchaseOrderValidationService, VendorRepository vendorRepository, BuyerRepository buyerRepository, ResourceRepository resourceRepository) {
        this.unitConverter = unitConverter;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.purchaseOrderValidationService = purchaseOrderValidationService;
        this.vendorRepository = vendorRepository;
        this.buyerRepository = buyerRepository;
        this.resourceRepository = resourceRepository;
    }


    public void addPurchaseOrder(PurchaseOrderDto purchaseOrderDto){
        purchaseOrderValidationService.validate(purchaseOrderDto);

        logger.info(String.format("addPurchaseOrder was called with purchaseOrderDto %s",purchaseOrderDto));
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setOrderLines(
                purchaseOrderDto.orderLines().stream()
                        .map(this::createOrderLine).toList());

        UUID vendorId = UUID.fromString(purchaseOrderDto.sellerParty().UUID());
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> ExceptionHandlingHelper.logAndThrowNotFound(String.format("The Vendor '%s', was not found", vendorId)));
        purchaseOrder.setVendor(vendor);

        UUID buyerId = UUID.fromString(purchaseOrderDto.customerParty().UUID());
        Buyer buyer = buyerRepository.findById(buyerId)
                .orElseThrow(() -> ExceptionHandlingHelper.logAndThrowNotFound(String.format("The Buyer '%s', was not found", buyerId)));

        purchaseOrder.setBuyer(buyer);
        purchaseOrder.setPoNumber(purchaseOrderDto.poNumber());
        purchaseOrder.setVesselNumber(purchaseOrderDto.vesselNumber());
        purchaseOrder.setStatus(Status.OPEN);

        purchaseOrderRepository.save(purchaseOrder);
    }
    private OrderLine createOrderLine(OrderLineDto orderLineDto){
        String resourceName = orderLineDto.description();
        Resource resource = resourceRepository.getResourceByNameIgnoreCase(resourceName)
                .orElseThrow(() -> ExceptionHandlingHelper.logAndThrowNotFound(String.format("The Resource '%s', was not found", resourceName)));
        OrderLine orderLine = new OrderLine();
        orderLine.setResource(resource);
        double amountInTon =unitConverter.convertToTonnes(orderLineDto.quantity(),orderLineDto.uom());
        orderLine.setAmountInTon(amountInTon);
        orderLine.setSellingPricePerTon(resource.getSellingPricePerTon());
        return orderLine;
    }
}

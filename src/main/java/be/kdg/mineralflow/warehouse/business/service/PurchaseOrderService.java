package be.kdg.mineralflow.warehouse.business.service;

import be.kdg.mineralflow.warehouse.business.domain.*;
import be.kdg.mineralflow.warehouse.business.util.UnitConverter;
import be.kdg.mineralflow.warehouse.exception.NoItemFoundException;
import be.kdg.mineralflow.warehouse.persistence.BuyerRepository;
import be.kdg.mineralflow.warehouse.persistence.PurchaseOrderRepository;
import be.kdg.mineralflow.warehouse.persistence.ResourceRepository;
import be.kdg.mineralflow.warehouse.persistence.VendorRepository;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.OrderLineDto;
import be.kdg.mineralflow.warehouse.presentation.controller.dto.PurchaseOrderDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class PurchaseOrderService {
    public static final Logger logger = Logger
            .getLogger(PurchaseOrderService.class.getName());
    private final ResourceRepository resourceRepository;
    private final UnitConverter unitConverter;
    private final VendorRepository vendorRepository;
    private final BuyerRepository buyerRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    public PurchaseOrderService(ResourceRepository resourceRepository, UnitConverter unitConverter, VendorRepository vendorRepository, BuyerRepository buyerRepository, PurchaseOrderRepository purchaseOrderRepository) {
        this.resourceRepository = resourceRepository;
        this.unitConverter = unitConverter;
        this.vendorRepository = vendorRepository;
        this.buyerRepository = buyerRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    @RabbitListener(queues = "purchase_order_queue")
    public void receivePurchaseOrder(PurchaseOrderDto purchaseOrderDto){
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setOrderLines(purchaseOrderDto.orderLines().stream().map(this::createOrderLine).toList());
        UUID vendorId = UUID.fromString(purchaseOrderDto.sellerParty().uuid());
        Optional<Vendor> optionalVendor = vendorRepository.findById(vendorId);
        if (optionalVendor.isEmpty()) {
            String messageException = String.format("The Vendor '%s', was not found", vendorId);
            logger.severe(messageException);
            throw new NoItemFoundException(messageException);
        }
        purchaseOrder.setVendor(optionalVendor.get());
        UUID buyerId = UUID.fromString(purchaseOrderDto.customerParty().uuid());
        Optional<Buyer> optionalBuyer = buyerRepository.findById(vendorId);
        if (optionalBuyer.isEmpty()) {
            String messageException = String.format("The Buyer '%s', was not found", buyerId);
            logger.severe(messageException);
            throw new NoItemFoundException(messageException);
        }
        purchaseOrder.setBuyer(optionalBuyer.get());

        purchaseOrderRepository.save(purchaseOrder);
    }
    private OrderLine createOrderLine(OrderLineDto orderLineDto){
        Optional<Resource> optionalResource = resourceRepository.getResourceByName(orderLineDto.description());
        if (optionalResource.isEmpty()) {
            String messageException = String.format("The Resource '%s', was not found", orderLineDto.description());
            logger.severe(messageException);
            throw new NoItemFoundException(messageException);
        }
        Resource resource = optionalResource.get();
        OrderLine orderLine = new OrderLine();
        orderLine.setResource(resource);
        double amountInTon =calculateAmountInTon(orderLineDto.quantity(),orderLineDto.uom());
        orderLine.setAmountInTon(amountInTon);
        orderLine.setSellingPricePerTon(resource.getSellingPricePerTon());
        return orderLine;
    }

    private double calculateAmountInTon(int quantity, String uom) {
        return unitConverter.convertToTonnes(quantity,uom);
    }
}

package be.kdg.mineralflow.warehouse.business.service;

import be.kdg.mineralflow.warehouse.business.domain.Commission;
import be.kdg.mineralflow.warehouse.business.domain.Invoice;
import be.kdg.mineralflow.warehouse.business.domain.PurchaseOrder;
import be.kdg.mineralflow.warehouse.business.domain.Vendor;
import be.kdg.mineralflow.warehouse.business.util.commission.CommissionCostCalculator;
import be.kdg.mineralflow.warehouse.persistence.CommissionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class CommissionService {
    private final CommissionCostCalculator commissionCostCalculator;
    private final CommissionRepository commissionRepository;

    public CommissionService(CommissionCostCalculator commissionCostCalculator, CommissionRepository commissionRepository) {
        this.commissionCostCalculator = commissionCostCalculator;
        this.commissionRepository = commissionRepository;
    }


    public Invoice addCommissionsToInvoice(Invoice invoice){
        List<Commission> commissions = getCommissions(invoice.getCreationDate().toLocalDate(),invoice.getVendor());
        if (commissions.isEmpty()) return invoice;
        commissions.forEach(invoice::addCommission);
        return invoice;
    }
    public List<Commission> getCommissions(LocalDate date, Vendor vendor){
        return commissionRepository.findAllCommissionsByCreationDateAndInvoiceIsNullAndVendor(date,vendor);
    }
    public PurchaseOrder createCommissionForPurchaseOrder(PurchaseOrder purchaseOrder){
        Commission commission = new Commission();
        commission.setCreationDate(LocalDateTime.now());
        commission.setPurchaseOrder(purchaseOrder);
        double commissionPrice = commissionCostCalculator.calculateCommissionCost(purchaseOrder.getOrderLines());
        commission.setCommisionPrice(commissionPrice);
        commissionRepository.save(commission);
        return purchaseOrder;
    }
}

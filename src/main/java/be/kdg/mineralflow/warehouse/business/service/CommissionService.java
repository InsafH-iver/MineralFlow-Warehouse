package be.kdg.mineralflow.warehouse.business.service;

import be.kdg.mineralflow.warehouse.business.domain.Commission;
import be.kdg.mineralflow.warehouse.business.domain.Invoice;
import be.kdg.mineralflow.warehouse.business.domain.PurchaseOrder;
import be.kdg.mineralflow.warehouse.business.domain.Vendor;
import be.kdg.mineralflow.warehouse.business.util.commission.CommissionCostCalculator;
import be.kdg.mineralflow.warehouse.persistence.CommissionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class CommissionService {
    private final CommissionCostCalculator commissionCostCalculator;
    private final CommissionRepository commissionRepository;

    public CommissionService(CommissionCostCalculator commissionCostCalculator, CommissionRepository commissionRepository) {
        this.commissionCostCalculator = commissionCostCalculator;
        this.commissionRepository = commissionRepository;
    }

    public void createAndSaveCommissionForPurchaseOrder(PurchaseOrder purchaseOrder){
        Commission commission = new Commission();
        commission.setCreationDate(LocalDate.now());
        commission.setPurchaseOrder(purchaseOrder);
        double commissionPrice = commissionCostCalculator.calculateCommissionCost(purchaseOrder.getOrderLines());
        commission.setCommisionPrice(commissionPrice);
        commissionRepository.save(commission);
    }
}

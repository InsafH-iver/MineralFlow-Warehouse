package be.kdg.mineralflow.warehouse.business.util.commission;

import be.kdg.mineralflow.warehouse.business.domain.OrderLine;

import java.util.List;

public interface CommissionCostCalculator {
    double calculateCommissionCost(List<OrderLine> orderLines);
}

package be.kdg.mineralflow.warehouse.business.util.commission;

import be.kdg.mineralflow.warehouse.business.domain.OrderLine;
import be.kdg.mineralflow.warehouse.config.ConfigProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConditionalOnProperty(name = "commission-cost.strategy", havingValue = "x_percent_of_selling_price")
public class XPercentOfSellingPriceCommissionCalculator implements CommissionCostCalculator {

    private final ConfigProperties configProperties;

    public XPercentOfSellingPriceCommissionCalculator(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    @Override
    public double calculateCommissionCost(List<OrderLine> orderLines) {
        if (orderLines == null) return 0;
        double commission = orderLines.stream()
                .mapToDouble(orderLine->
                        orderLine.getAmountInTon() * orderLine.getSellingPricePerTon()).sum()
                * configProperties.getCommissionPart();
        return commission;
    }
}

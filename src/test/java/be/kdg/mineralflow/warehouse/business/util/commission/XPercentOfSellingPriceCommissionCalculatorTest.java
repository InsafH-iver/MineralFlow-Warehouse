package be.kdg.mineralflow.warehouse.business.util.commission;

import be.kdg.mineralflow.warehouse.TestContainer;
import be.kdg.mineralflow.warehouse.business.domain.OrderLine;
import be.kdg.mineralflow.warehouse.business.domain.Resource;
import be.kdg.mineralflow.warehouse.config.ConfigProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class XPercentOfSellingPriceCommissionCalculatorTest extends TestContainer {
    @Autowired
    private XPercentOfSellingPriceCommissionCalculator calculator;
    @Autowired
    private ConfigProperties configProperties;
    @Test
    void calculateCommissionCost() {
        //ARRANGE
        OrderLine orderLine1 = new OrderLine();
        orderLine1.setResource(new Resource(UUID.randomUUID(),"Hout","hout",10,5));
        orderLine1.setSellingPricePerTon(orderLine1.getResource().getSellingPricePerTon());
        orderLine1.setAmountInTon(5);
        OrderLine orderLine2 = new OrderLine();
        orderLine2.setResource(new Resource(UUID.randomUUID(),"turf","turf",1,1));
        orderLine2.setSellingPricePerTon(orderLine1.getResource().getSellingPricePerTon());
        orderLine2.setAmountInTon(10);
        List<OrderLine> orderLines = List.of(orderLine1,orderLine2);

        //ACT
        double commissionCost = calculator.calculateCommissionCost(orderLines);
        //ASSERT
        assertThat(commissionCost)
                .isEqualTo(
                        (orderLine1.getSellingPricePerTon()
                                * orderLine1.getAmountInTon()
                                + orderLine2.getAmountInTon()
                                * orderLine2.getSellingPricePerTon()) * configProperties.getCommissionPart());
    }
    @Test
    void calculateCommissionCost_should_return_zero_when_orderLines_are_empty() {
        //ARRANGE
        List<OrderLine> orderLines = List.of();

        //ACT
        double commissionCost = calculator.calculateCommissionCost(orderLines);
        //ASSERT
        assertThat(commissionCost).isEqualTo(0);
    }
    @Test
    void calculateCommissionCost_should_return_zero_when_orderLines_are_null() {
        //ARRANGE
        List<OrderLine> orderLines = null;

        //ACT
        double commissionCost = calculator.calculateCommissionCost(orderLines);
        //ASSERT
        assertThat(commissionCost).isEqualTo(0);
    }
}
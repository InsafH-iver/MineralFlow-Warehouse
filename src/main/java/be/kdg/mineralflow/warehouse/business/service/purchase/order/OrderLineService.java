package be.kdg.mineralflow.warehouse.business.service.purchase.order;

import be.kdg.mineralflow.warehouse.business.domain.OrderLine;
import be.kdg.mineralflow.warehouse.business.domain.StockPortion;
import be.kdg.mineralflow.warehouse.business.domain.Warehouse;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

@Service
public class OrderLineService {
    public static final Logger logger = Logger
            .getLogger(OrderLineService.class.getName());

    public boolean processOrderLine(OrderLine orderLine, Warehouse warehouse) {
        logger.info(String.format("Processing order line  with id %s"
                , orderLine.getId()));
        if (orderLine.getAmountInTon() > warehouse.getUsedCapacityInTon()) {
            logger.info("Order line wasn't fulfilled");
            return false;
        }
        releaseStockForOrderLine(orderLine, warehouse);
        orderLine.markAsCompleted();
        logger.info("Order line marked as completed");
        return true;
    }

    private void releaseStockForOrderLine(OrderLine orderLine, Warehouse warehouse) {
        double remainingAmount = orderLine.getAmountInTon();

        List<StockPortion> stockPortions = getNonEmptyStockPortionsSortedByArrival(warehouse);

        for (StockPortion stockPortion : stockPortions) {
            remainingAmount -= stockPortion.takeAmountInTon(remainingAmount);
            if (remainingAmount <= 0) break;
        }

        warehouse.reduceStock(orderLine.getAmountInTon());
    }

    private List<StockPortion> getNonEmptyStockPortionsSortedByArrival(Warehouse warehouse) {
        return warehouse.getStockPortions().stream()
                .filter(Predicate.not(StockPortion::isPortionEmpty))
                .sorted(Comparator.comparing(StockPortion::getArrivalTime))
                .toList();
    }
}

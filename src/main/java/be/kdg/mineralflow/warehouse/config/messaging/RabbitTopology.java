package be.kdg.mineralflow.warehouse.config.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitTopology {

    private final RabbitConfigProperties rabbitConfigProperties;

    public RabbitTopology(RabbitConfigProperties rabbitConfigProperties) {
        this.rabbitConfigProperties = rabbitConfigProperties;
    }

    //EXCHANGES
    @Bean
    TopicExchange waterTopicExchange() {
        return new TopicExchange(rabbitConfigProperties.getWaterExchangeName());
    }

    @Bean
    TopicExchange warehouseTopicExchange() {
        return new TopicExchange(rabbitConfigProperties.getWarehouseExchangeName());
    }

    //PURCHASE_ORDER_PICK_up
    @Bean
    public Queue purchaseOrderPickUpQueue() {
        return new Queue(rabbitConfigProperties.getPurchaseOrderPickUpQueue(), false);
    }

    @Bean
    public Binding purchaseOrderPickUpBinding(@Qualifier("warehouseTopicExchange") TopicExchange warehouseTopicExchange
            , @Qualifier("purchaseOrderPickUpQueue") Queue purchaseOrderPickUpQueue) {

        return BindingBuilder.bind(purchaseOrderPickUpQueue).to(warehouseTopicExchange)
                .with(rabbitConfigProperties.getPurchaseOrderPickUpRoutingKey());
    }

    //ADD_PURCHASE_ORDER
    @Bean
    public Queue addPurchaseOrderQueue() {
        return new Queue(rabbitConfigProperties.getAddPurchaseOrderQueue(), false);
    }

    @Bean
    public Binding addPurchaseOrderBinding(@Qualifier("warehouseTopicExchange") TopicExchange warehouseTopicExchange
            , @Qualifier("addPurchaseOrderQueue") Queue addPurchaseOrderQueue) {

        return BindingBuilder.bind(addPurchaseOrderQueue).to(warehouseTopicExchange)
                .with(rabbitConfigProperties.getAddPurchaseOrderRoutingKey());
    }

    //TRUCK_DEPARTURE_FROM_WEIGHING_BRIDGE
    @Bean
    public Queue truckDepartureFromWeighingBridgeQueue() {
        return new Queue(rabbitConfigProperties.getTruckDepartureFromWeighingBridgeQueue(), false);
    }

    @Bean
    public Binding truckDepartureFromWeighingBridgeBinding(@Qualifier("warehouseTopicExchange") TopicExchange warehouseTopicExchange
            , @Qualifier("truckDepartureFromWeighingBridgeQueue") Queue truckDepartureFromWeighingBridgeQueue) {

        return BindingBuilder.bind(truckDepartureFromWeighingBridgeQueue).to(warehouseTopicExchange)
                .with(rabbitConfigProperties.getTruckDepartureFromWeighingBridgeRoutingKey());
    }
}

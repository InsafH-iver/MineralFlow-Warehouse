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

    @Bean
    TopicExchange landTopicExchange(){
        return new TopicExchange(rabbitConfigProperties.getExchangeName());
    }

    @Bean
    public Queue arrivalAtWareHouseQueue() {
        return new Queue(rabbitConfigProperties.getTruckDepartureFromWeighingBridgeQueue(), false);
    }

    @Bean
    public Binding arrivalAtWareHouseBinding(TopicExchange topicExchange, @Qualifier("arrivalAtWareHouseQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(topicExchange).with(rabbitConfigProperties.getTruckDepartureFromWeighingBridgeRoutingKey());
    }

    @Bean
    public Queue addPurchaseOrderQueue(){
        return new Queue(rabbitConfigProperties.getAddPurchaseOrderQueue(),false);
    }
    @Bean
    public Binding addPurchaseOrderBinding(TopicExchange topicExchange, @Qualifier("addPurchaseOrderQueue") Queue queue){
        return BindingBuilder.bind(queue).to(topicExchange).with(rabbitConfigProperties.getAddPurchaseOrderRoutingKey());
    }
}

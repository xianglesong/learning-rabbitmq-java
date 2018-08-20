package com.xianglesong.rabbitmq.configs;

import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

  @Value("${mq.order.info}")
  String orderSycnQueue;

//  @Bean
//  public ConnectionFactory connectionFactory() {
//    CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//    connectionFactory.setAddresses("127.0.0.1:5672");
//    connectionFactory.setUsername("guest");
//    connectionFactory.setPassword("guest");
//    connectionFactory.setVirtualHost("/");
//    connectionFactory.setPublisherConfirms(true); //必须要设置
//    return connectionFactory;
//  }

  @Bean
  public Queue orderSycnQueue() {
    Map<String, Object> arguments = new HashMap<>();
    arguments.put("x-dead-letter-exchange", "");
    arguments.put("x-dead-letter-routing-key", "");
    arguments.put("x-max-priority", 16);
    arguments.put("x-message-ttl", 10000);

//        return new Queue(orderSycnQueue);
    return new Queue(orderSycnQueue, true, false, false, arguments);
  }

  // spring boot
  final static String queueName = "spring-boot";

  @Bean
  Queue queue() {
    return new Queue(queueName, false);
  }

  @Bean
  TopicExchange exchange() {
    return new TopicExchange("spring-boot-exchange");
  }

  @Bean
  Binding binding(Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(queueName);
  }
}

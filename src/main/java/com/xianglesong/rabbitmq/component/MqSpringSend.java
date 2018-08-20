package com.xianglesong.rabbitmq.component;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MqSpringSend implements ConfirmCallback {

  private static final Logger logger = LoggerFactory.getLogger(MqSpringSend.class);

  @Autowired
  RabbitTemplate rabbitTemplate;

  @Value("${mq.order.info}")
  String queues;

  public void sendMessgage(String msg) {
    int priority = 5;
    System.out.println("====优先级===" + priority);
    MessageProperties messageProperties = new MessageProperties();
    messageProperties.setContentType("text");
    messageProperties.setPriority(priority);
    byte[] body = msg.getBytes();

    // 一次性发送10条消息
    for (int i = 0; i < 3; i++) {
      CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
      System.out.println("correlationId: " + correlationId);
      rabbitTemplate.send("", queues, new Message(body, messageProperties), correlationId);
      rabbitTemplate.setConfirmCallback(this);
    }

  }

  @Override
  public void confirm(CorrelationData correlationData, boolean ack, String cause) {
    System.out.println(" 回调id:" + correlationData);
    if (ack) {
      System.out.println("消息成功消费");
    } else {
      System.out.println("消息消费失败:" + cause);
    }
  }
}


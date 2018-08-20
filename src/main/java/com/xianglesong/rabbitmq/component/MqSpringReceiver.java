package com.xianglesong.rabbitmq.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MqSpringReceiver {

  private static final Logger logger = LoggerFactory.getLogger(MqSpringReceiver.class);

  @RabbitListener(queues = "${mq.order.info}")
  public void receive(Message message) {
    try {
      if (message != null && message.getBody() != null) {
        String jsonString = new String(message.getBody(), "UTF-8");
        logger.info("receive : " + jsonString);
      }
    } catch (Exception ex) {
      logger.error("mq process excepion", ex);
      throw new RuntimeException("mq process excepion");
    }
  }

}

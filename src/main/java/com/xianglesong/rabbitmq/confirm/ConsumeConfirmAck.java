/**
 * @CopyRight all rights reserved
 */

package com.xianglesong.rabbitmq.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class ConsumeConfirmAck {

  final static String QUEUE_NAME = "confirm-test";
  static ConnectionFactory connectionFactory;

  public static void main(String[] args) {
    try {
      connectionFactory = new ConnectionFactory();
      Connection conn = connectionFactory.newConnection();
      Channel ch = conn.createChannel();
      ch.queueDeclare(QUEUE_NAME, true, false, false, null);
      // Consume
      QueueingConsumer qc = new QueueingConsumer(ch);
      boolean autoAck = false;
      ch.basicConsume(QUEUE_NAME, autoAck, qc);
      for (int i = 0; i < 2; ++i) {
        Delivery delivery = qc.nextDelivery();
        String msg = new String(delivery.getBody());
        ch.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        //      ch.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
        System.out.println("consume: " + msg);
      }
      // Cleanup
      ch.close();
      conn.close();
    } catch (Throwable e) {
      System.out.println("Whoosh!");
    }
  }
}

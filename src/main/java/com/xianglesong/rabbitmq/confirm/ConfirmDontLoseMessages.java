/**
 * @CopyRight all rights reserved
 */

package com.xianglesong.rabbitmq.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import java.io.IOException;

public class ConfirmDontLoseMessages {

  static int msgCount = 3;
  final static String QUEUE_NAME = "confirm-test";
  static ConnectionFactory connectionFactory;

  public static void main(String[] args)
      throws IOException, InterruptedException {
    connectionFactory = new ConnectionFactory();

    // Consume msgCount messages.
    // (new Thread(new Consumer())).start();
    // Publish msgCount messages and wait for confirms.
    (new Thread(new Publisher())).start();

  }

  @SuppressWarnings("ThrowablePrintedToSystemOut")
  static class Publisher implements Runnable {

    public void run() {
      try {
        long startTime = System.currentTimeMillis();

        // Setup
        Connection conn = connectionFactory.newConnection();
        Channel ch = conn.createChannel();
        ch.queueDeclare(QUEUE_NAME, true, false, false, null);
        ch.confirmSelect();

        ch.addConfirmListener(new ConfirmListener() {
          @Override
          public void handleAck(long deliveryTag, boolean multiple) throws IOException {
            System.out.println(
                "+++++++++++++handleAck   deliveryTag: " + deliveryTag + ", multiple: " + multiple);
            if (multiple) {
              // sortedSet.headSet(deliveryTag + 1).clear();
            } else {
              // sortedSet.remove(deliveryTag);
            }
          }

          @Override
          public void handleNack(long deliveryTag, boolean multiple) throws IOException {
            System.out.println(
                "------------handleNack   deliveryTag: " + deliveryTag + ", multiple: " + multiple);
            if (multiple) {
              // sortedSet.headSet(deliveryTag + 1) toDo    进行重新发送，或记录下来，后续再统一发送;
            } else {
              // sortedSet.first();  进行重新发送，或记录下来，后续再统一发送;
            }
          }
        });

        // Publish
        for (long i = 0; i < msgCount; ++i) {
          String xx = "nop" + String.valueOf(i);
          ch.basicPublish("", QUEUE_NAME,
              MessageProperties.PERSISTENT_BASIC,
              xx.getBytes());
//          if (ch.waitForConfirms()) {
//            System.out.println("消息发送成功");
//          }
        }

        ch.waitForConfirmsOrDie();

        // Cleanup
        // ch.queueDelete(QUEUE_NAME);
        ch.close();
        conn.close();

        long endTime = System.currentTimeMillis();
        System.out.printf("Test took %.3fs\n",
            (float) (endTime - startTime) / 1000);
      } catch (Throwable e) {
        System.out.println("foobar :(");
        System.out.print(e);
      }
    }
  }

  static class Consumer implements Runnable {

    public void run() {
      try {
        // Setup
        Connection conn = connectionFactory.newConnection();
        Channel ch = conn.createChannel();
        ch.queueDeclare(QUEUE_NAME, true, false, false, null);
        // Consume
        QueueingConsumer qc = new QueueingConsumer(ch);
        boolean autoAck = false;
        ch.basicConsume(QUEUE_NAME, autoAck, qc);
        for (int i = 0; i < msgCount; ++i) {
          Delivery delivery = qc.nextDelivery();
          String msg = new String(delivery.getBody());
          ch.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
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

}
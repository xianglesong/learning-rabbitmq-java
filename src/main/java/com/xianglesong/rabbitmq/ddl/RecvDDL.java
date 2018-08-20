package com.xianglesong.rabbitmq.ddl;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;

public class RecvDDL {

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("127.0.0.1");
    Connection connection = factory.newConnection();

    Channel channel = connection.createChannel();

    channel.basicConsume("test-dlx2", true, "consumer", new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope,
          AMQP.BasicProperties properties, byte[] body) throws IOException {

        long deliveryTag = envelope.getDeliveryTag();

        //do some work async
        System.out.println(body[0]);
      }
    });
  }
}
package com.xianglesong.rabbitmq.ddl;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SendDDL {

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("127.0.0.1");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    byte i = 10;
    while (i-- > 0) {
      channel.basicPublish("", "test-dlx1",
          new AMQP.BasicProperties.Builder().expiration(String.valueOf(i * 1000)).build(),
          new byte[]{i});
    }

    channel.close();
    connection.close();
  }
}

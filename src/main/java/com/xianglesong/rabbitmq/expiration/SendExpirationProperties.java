package com.xianglesong.rabbitmq.expiration;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.util.HashMap;
import java.util.Map;

public class SendExpirationProperties {

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("127.0.0.1");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    channel.queueDeclare("hello_expiration", true, false, false, null);
    String message = "Hello World!";
    Map<String, Object> headers = new HashMap<String, Object>();
    headers.put("latitude", 51.5252949);
    headers.put("longitude", -0.0905493);
    AMQP.BasicProperties basicProperties = new AMQP.BasicProperties.Builder()
        .contentType("text/plain")
        .deliveryMode(2)
        .priority(1)
        .userId("guest")
        .headers(headers)
        .expiration("10000")
        .build();
    channel.basicPublish("", "hello_expiration", basicProperties, message.getBytes("UTF-8"));
    System.out.println(" [x] Sent '" + message + "'");
    channel.close();
    connection.close();
  }
}

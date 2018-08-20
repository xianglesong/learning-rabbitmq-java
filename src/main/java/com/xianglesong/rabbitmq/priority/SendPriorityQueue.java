package com.xianglesong.rabbitmq.priority;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections.map.HashedMap;

public class SendPriorityQueue {

  private final static String QUEUE_NAME = "hello_priority";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("127.0.0.1");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    Map<String, Object> map = new HashedMap();
    map.put("x-max-priority", 16);
    channel.queueDeclare(QUEUE_NAME, true, false, false, map);
    String message = "Hello World: " + Calendar.getInstance().getTime();
    Map<String, Object> headers = new HashMap<String, Object>();
    headers.put("latitude", 51.5252949);
    headers.put("longitude", -0.0905493);
    AMQP.BasicProperties basicProperties = new AMQP.BasicProperties.Builder()
        .contentType("text/plain")
        .deliveryMode(2)
        .priority(16)
        .userId("guest")
        .headers(headers)
        // .expiration("100000")
        .build();
    channel.basicPublish("", QUEUE_NAME, basicProperties, message.getBytes("UTF-8"));
    System.out.println(" [x] Sent '" + message + "'");
    channel.close();
    connection.close();
  }
}

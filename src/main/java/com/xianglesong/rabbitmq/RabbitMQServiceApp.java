package com.xianglesong.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import com.xianglesong.rabbitmq.component.MqSpringSend;

@SpringBootApplication
@EnableAsync
public class RabbitMQServiceApp {

  private static final Logger logger = LoggerFactory.getLogger(RabbitMQServiceApp.class);

  public static void main(String[] args) {
    logger.info("start.");
    ConfigurableApplicationContext context =
        new SpringApplicationBuilder(RabbitMQServiceApp.class).run(args);
    context.addApplicationListener(new ApplicationPidFileWriter());

    MqSpringSend mqService = (MqSpringSend) context.getBean("mqSpringSend");

    mqService.sendMessgage("this is 啊 test");

    logger.info("end");
  }
}

package com.niuchaoqun.hello;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeoutException;

/**
 * 简单队列，发送端
 * <p>
 * 使用默认的 exchange
 *
 * @author niuchaoqun
 */
public class Send {
    public static final String QUEUE_NAME = "hello";

    public static void run(String[] args) throws IOException, TimeoutException {
        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 创建连接
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.101.170");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setVirtualHost("/");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 声明队列是幂等的，队列不会被重复创建
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 消息体
        String message = "Hello World! " + datetime;

        // 消息不会直接发送到队列，而是需要通过 exchange，空字符串表示默认 exchange，通过 routing_key 指定队列名称
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("[x] Send <" + message + ">");

        channel.close();
        connection.close();
    }
}

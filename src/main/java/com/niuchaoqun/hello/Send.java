package com.niuchaoqun.hello;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send {
    public static final String QUEUE_NAME = "hello";

    public static void run(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.101.170");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 声明队列是幂等的，
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        String message = "Hello World";

        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("[x] Send <" + message + ">");

        channel.close();
        connection.close();
    }
}

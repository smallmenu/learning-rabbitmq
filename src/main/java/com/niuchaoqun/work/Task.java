package com.niuchaoqun.work;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeoutException;

/**
 * Work 模式发送端
 *
 * @author niuchaoqun
 */
public class Task {
    public static final String QUEUE_NAME = "task_queue";

    public static void run(String[] args) throws IOException, TimeoutException {
        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.101.170");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 持久化消息，通过 RabbitMQ 的持久化机制来进行
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);

        // 可以从命令行接收参数列表，拼接成消息体
        String message = getMessage(args) + " " + datetime;

        // 持久化消息，需要将消息标记为持久性 PERSISTENT_TEXT_PLAIN
        channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
        System.out.println(" [x] Send <" + message + ">");

        channel.close();
        connection.close();
    }

    private static String getMessage(String[] strings) {
        if (strings.length < 1) {
            return "Hello World!";
        }

        return joinStrings(strings, " ");
    }

    private static String joinStrings(String[] strings, String delimiter) {
        int length = strings.length;
        if (length == 0) {
            return "";
        }
        StringBuilder words = new StringBuilder(strings[0]);
        for (int i = 1; i < length; i++) {
            words.append(delimiter).append(strings[i]);
        }
        return words.toString();
    }
}

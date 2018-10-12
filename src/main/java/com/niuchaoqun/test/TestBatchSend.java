package com.niuchaoqun.test;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeoutException;

/**
 * 批量发布消息
 */
public class TestBatchSend {
    public static final String QUEUE_NAME = "batch";

    public static final long TOTAL = 10000;

    public static void run(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.101.170");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        long start = System.currentTimeMillis();
        for (int i = 0; i < TOTAL; i++) {
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern(" yyyy-MM-dd HH:mm:ss.SSS"));
            String message = "Batch Message <No." + i + time + ">";
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
        }
        long consume = System.currentTimeMillis() - start;
        double each = consume / TOTAL;
        System.out.println("Batch All " + consume + "ms, Each " + each + "ms");

        channel.close();
        connection.close();
    }

    public static String generate1K() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 512; i++) {
            sb.append(i);
        }
        return sb.toString();
    }

    public static String generate10K() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1024*10; i++) {
            sb.append(i);
        }
        return sb.toString();
    }
}

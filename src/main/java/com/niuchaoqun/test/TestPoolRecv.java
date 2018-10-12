package com.niuchaoqun.test;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 初始化线程池，处理接收到的消息
 */
public class TestPoolRecv {
    public static final String QUEUE_NAME = "batch";

    private static AtomicLong time = new AtomicLong();

    private static AtomicLong counter = new AtomicLong();

    private static int THREAD_NUM = 5;

    public static void run(String[] args) throws IOException, TimeoutException {
        if (args.length >= 1) {
            int threadNum = Integer.parseInt(args[0]);
            if (threadNum >0) {
                THREAD_NUM = threadNum;
            }
        }

        // 初始化线程池
        System.out.println("[*] Init ThreadPool " + THREAD_NUM);
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUM);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.101.170");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        System.out.println("[*] Waiting for messages. To exit press CTRL+C");

        channel.basicQos(1);

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                try {
                    executor.execute(new Message(message, time));
                } finally {
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };

        channel.basicConsume(QUEUE_NAME, false, consumer);
    }
}

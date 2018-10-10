package com.niuchaoqun.work;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Work 模式处理端
 *
 * @author niuchaoqun
 */
public class Worker {
    public static final String QUEUE_NAME = "task_queue";

    public static void run(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.101.170");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);

        System.out.println("[*] Waiting for messages. To exit press CTRL+C");

        // 公平派遣机制
        channel.basicQos(1);

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");

                System.out.println("[x] Recived <" + message + ">");

                try {
                    doWork(message);
                } finally {
                    // 为了确保消息不丢失，RabbitMQ 支持消息确认，如果没有收到确认，消息会重新发送
                    // 错过 basicAck 是个常见的错误，但后果很严重
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };

        // 关闭自动确认，在 consumer 中手动处理
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    }

    private static void doWork(String message) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

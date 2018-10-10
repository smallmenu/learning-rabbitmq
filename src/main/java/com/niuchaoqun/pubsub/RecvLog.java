package com.niuchaoqun.pubsub;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发布订阅模式，接收端。
 *
 * @author niuchaoqun
 */
public class RecvLog {
    public static final String EXCHANGE_NAME = "logs";

    public static void run(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.101.170");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT, true);

        // 临时队列，多个接收端创建一个非持久的，独占的，自动删除的临时队列，从而形成订阅模式
        // 如果这里多个接收端使用相同的队列名称，会退化成 work 队列模式
        //String queueName = channel.queueDeclare("share", true, false, false, null).getQueue();
        String queueName = channel.queueDeclare().getQueue();

        // 将队列和 exchange 绑定在一起，表示队列对此 exchange 感兴趣
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println("[*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("[x] Recived <" + message + ">");
            }
        };

        channel.basicConsume(queueName, true, consumer);
    }
}

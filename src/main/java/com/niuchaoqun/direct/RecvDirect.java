package com.niuchaoqun.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 路由模式，使用 redirect exchange
 *
 * @author niuchaoqun
 */
public class RecvDirect {
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void run(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.101.170");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true);

        // 与 fanout 类似，这里使用自动创建临时队列，多个相同的 routingKey 客户端会接收到相同的消息。可以理解为具有路由功能的发布订阅模式
        // 但是，如果使用相同的队列名绑定，多个不同 routingKey 就会指向同一个队列，退化为普通队列，这样也就失去了路由的意义
        String queueName = channel.queueDeclare().getQueue();

        if (args.length < 1) {
            System.err.println("Usage: direct-recv [info] [warning] [error]");
            System.exit(1);
        }

        // 创建多个基于 routingKey 的绑定，用于接受数据
        for (String bindingKey : args) {
            channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
        }
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("[x] Recived <" + envelope.getRoutingKey() + "> <" + message + ">");
            }
        };

        channel.basicConsume(queueName, true, consumer);
    }
}
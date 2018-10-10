package com.niuchaoqun.hello;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 简单队列，接收端
 *
 * @author SuoSi
 */
public class Recv {
    public static final String QUEUE_NAME = "hello";

    public static void run(String[] args) throws IOException, TimeoutException {
        // 创建连接
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.101.170");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 接受消息的程序依然要声明队列，因为我们不确定 Send 是否会优先执行，重复声明队列是个好习惯
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        System.out.println("[*] Waiting for messages. To exit press CTRL+C");

        // 回调
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("[x] Recived <" + message + ">");
            }
        };

        // 第二个参数，表示自动ack，Recv 程序不会退出，会一直运行接受更多的消息，并可被 Ctrl+C 中断
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}

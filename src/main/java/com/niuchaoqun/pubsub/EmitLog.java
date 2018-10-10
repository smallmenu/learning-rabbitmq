package com.niuchaoqun.pubsub;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeoutException;

/**
 * 发布订阅模式，发送端。使用 fanout exchange
 *
 * @author niuchaoqun
 */
public class EmitLog {
    public static final String EXCHANGE_NAME = "logs";

    public static void run(String[] args) throws IOException, TimeoutException {
        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.101.170");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 声明 exchange，并持久化
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT, true);

        // 可以从命令行接收参数列表，拼接成消息体
        String message = getMessage(args) + " " + datetime;

        // fanout 会自动忽略 routingKey
        channel.basicPublish(EXCHANGE_NAME, "", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
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

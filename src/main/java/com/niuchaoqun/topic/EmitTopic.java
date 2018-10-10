package com.niuchaoqun.topic;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeoutException;

/**
 * 主题模式，发送端。使用 topic exchange
 * <p>
 * 扩展了直接模式，可以使用通配符定义 routingkey
 *
 * @author niuchaoqun
 */
public class EmitTopic {

    public static final String EXCHANGE_NAME = "top_logs";

    public static void run(String[] args) throws IOException, TimeoutException {

        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.101.170");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 声明 exchange，并持久化
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC, true);

        String routingKey = getSeverity(args);
        String message = getMessage(args) + " " + datetime;

        // 根据传入的参数，动态创建具有 routingKey 的发布
        channel.basicPublish(EXCHANGE_NAME, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));

        System.out.println(" [x] Send <"+ routingKey +"> <" + message + ">");

        channel.close();
        connection.close();
    }

    private static String getSeverity(String[] strings) {
        if (strings.length < 1) {
            return "anonymous.info";
        }

        return strings[0];
    }

    private static String getMessage(String[] strings) {
        if (strings.length < 2) {
            return "Hello World!";
        }

        return joinStrings(strings, " ", 1);
    }

    private static String joinStrings(String[] strings, String delimiter, int startIndex) {
        int length = strings.length;
        if (length == 0) {
            return "";
        }
        if (length < startIndex) {
            return "";
        }
        StringBuilder words = new StringBuilder(strings[startIndex]);
        for (int i = startIndex + 1; i < length; i++) {
            words.append(delimiter).append(strings[i]);
        }
        return words.toString();
    }

}

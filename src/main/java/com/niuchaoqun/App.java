package com.niuchaoqun;

import com.niuchaoqun.hello.Send;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RabbitMQ
 *
 * @author niuchaoqun
 */
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        int exitCode = -1;
        Driver pd = new Driver();
        try {
            pd.addClass("hello-send", Send.class, "hello send example");

            exitCode = pd.run(args, 1);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        System.out.println(exitCode);
    }
}

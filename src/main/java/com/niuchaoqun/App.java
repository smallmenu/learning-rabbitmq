package com.niuchaoqun;

import com.niuchaoqun.hello.Recv;
import com.niuchaoqun.hello.Send;
import com.niuchaoqun.pubsub.EmitLog;
import com.niuchaoqun.pubsub.RecvLog;
import com.niuchaoqun.routing.EmitDirect;
import com.niuchaoqun.routing.RecvDirect;
import com.niuchaoqun.work.Task;
import com.niuchaoqun.work.Worker;
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
        if (args.length < 1) {
            System.err.println("Usage: example sub-example");
        }

        int exitCode = -1;
        Driver pd = new Driver();
        try {
            pd.addClass("hello-send", Send.class, "hello send example");
            pd.addClass("hello-recv", Recv.class, "hello recv example");
            pd.addClass("work-task", Task.class, "work task example");
            pd.addClass("work-worker", Worker.class, "work worker example");
            pd.addClass("pubsub-emit", EmitLog.class, "pubsub emit example");
            pd.addClass("pubsub-recv", RecvLog.class, "pubsub recv example");
            pd.addClass("routing-emit", EmitDirect.class, "routing emit example");
            pd.addClass("routing-recv", RecvDirect.class, "routing recv example");

            exitCode = pd.run(args, 0);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        System.out.println(exitCode);
    }
}

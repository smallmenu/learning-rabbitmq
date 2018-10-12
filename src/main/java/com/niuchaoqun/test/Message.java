package com.niuchaoqun.test;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class Message implements Runnable {
    private final String message;

    private final AtomicLong counter;

    private static final Random random = new Random();

    private static final ThreadLocal<Long> TL_TIME = ThreadLocal.withInitial(() -> 0L);

    public Message(String message, AtomicLong counter) {
        this.message = message;
        this.counter = counter;
    }

    @Override
    public void run() {
        Long start = System.currentTimeMillis();

        String threadName = Thread.currentThread().getName();

        // 模拟单个任务耗时
        try {
            Thread.sleep(100 + random.nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Long end = System.currentTimeMillis();
        Long use = end - start;

        // 统计各个线程池总耗时
        long no = counter.incrementAndGet();
        TL_TIME.set(TL_TIME.get() + use);

        System.out.println(threadName + " Deal <Counter." + no  + "> " +  message + ", use：" + use + "ms, total：" + TL_TIME.get() + "ms");
    }
}

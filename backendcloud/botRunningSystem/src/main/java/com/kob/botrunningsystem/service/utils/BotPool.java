package com.kob.botrunningsystem.service.utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BotPool extends Thread {

    /**
     * 锁
     */
    private static final ReentrantLock lock = new ReentrantLock();
    /**
     * 条件变量
     */
    private final Condition condition = lock.newCondition();

    private final Queue<Bot> bots = new LinkedList<>();

    public void addBot(Integer userId, String botCode, String input) {
        //生产者
        lock.lock();
        try {
            bots.add(new Bot(userId, botCode, input));
            //唤醒另外一个线程
            condition.signalAll();
        } finally {
            lock.unlock();

        }
    }

    private void consume(Bot bot) {
        Consumer consumer = new Consumer();
        consumer.startTimeOut(2000, bot);
    }

    @Override
    public void run() {
        //消息队列  消费者
        while (true) {
            lock.lock();
            if (bots.isEmpty()) {
                try {
                    //线程阻塞状态 会自动将锁释放
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            } else {
                Bot bot = bots.remove();
                lock.unlock();
                consume(bot);   //编译执行代码 比较耗时
            }
        }
    }
}

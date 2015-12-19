/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.common.kafka.producer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.*;

import com.lamfire.logger.Logger;
import com.lamfire.utils.StringUtils;

/**
 * @author zxc Dec 17, 2015 4:01:30 PM
 */
public class ThreadUtil {

    private static Logger                   logger     = Logger.getLogger(ThreadUtil.class);

    /** # */
    private final static String             POUND      = "#";
    /** - */
    private final static String             MINUS_SIGN = "-";

    private static ScheduledExecutorService service    = Executors.newScheduledThreadPool(1);

    private static ExecutorService          pool       = Executors.newFixedThreadPool(8);

    public static void submitTask(Runnable runnable) {
        pool.submit(runnable);
    }

    /**
     * 重复开启 threadNum 个线程来执行 runnable
     * 
     * @param runnable 可执行任务
     * @param threadNum 重复开启的线程个数
     * @param sleepTime 启动完所有线程后，休息 ms
     */
    public static void startThread(Runnable runnable, String threadName, int threadNum, long sleepTime) {
        for (int i = 0; i < threadNum; i++) {
            Thread thread = new Thread(runnable, POUND + StringUtils.defaultIfEmpty(threadName, "Thread") + MINUS_SIGN
                                                 + i);
            thread.start();
        }
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
        }
    }

    /**
     * 重复开启 threadNum 个线程来执行 runnable
     * 
     * @param runnable 可执行任务
     * @param threadNum 重复开启的线程个数
     * @param sleepTime 启动完所有线程后，休息 ms
     */
    public static void startThread(Runnable runnable, int threadNum, long sleepTime) {
        ThreadUtil.startThread(runnable, "Thread", threadNum, sleepTime);
    }

    /**
     * 开启 1 个线程来执行 runnable
     * 
     * @param runnable 可执行任务
     */
    public static void startThread(Runnable runnable) {
        startThread(runnable, 1, 0);
    }

    /**
     * 开启 1 个线程来执行 runnable
     * 
     * @param runnable 可执行任务
     */
    public static void startThread(Runnable runnable, String threadName) {
        startThread(runnable, StringUtils.trimToEmpty(threadName), 1, 0);
    }

    /**
     * 重复开启 threadNum 个线程来执行 runnable
     * 
     * @param runnable 可执行任务
     * @param sleepTime 重复开启的线程个数
     */
    public static void startThread(Runnable runnable, long sleepTime) {
        startThread(runnable, 1, sleepTime);
    }

    /**
     * 周期性的执行一个任务
     * 
     * @param task
     * @param delay
     * @param period
     * @throws Exception
     */
    public static void scheduleAtFixed(Runnable runnable, long delay, long period) throws Exception {
        if (runnable == null) throw new Exception("runnable 为空");
        service.scheduleAtFixedRate(runnable, delay, period, TimeUnit.MILLISECONDS);
    }

    /**
     * 定时执行一个任务
     * 
     * @param task 实现TimerTask接口的任务实例
     * @param delay 调用方法后要延时的毫秒数
     * @param period 执行间隔
     * @throws Exception
     */
    public static void scheduleAtFixedRateDelayTimeMillisDelay(TimerTask task, long delay, long period)
                                                                                                       throws Exception {
        if (task == null) throw new Exception("task 为空");
        service.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS);
    }

    /**
     * 定时执行一个任务()
     * 
     * @param time 20:00:00
     * @param runnable
     */
    public static void executeCrontab(String time, Runnable runnable) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        long oneDay = 24 * 60 * 60 * 1000;

        long initDelay = getTimeMillis(time) - System.currentTimeMillis();
        initDelay = initDelay > 0 ? initDelay : oneDay + initDelay;
        executor.scheduleAtFixedRate(runnable, initDelay, oneDay, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取指定时间对应的毫秒数
     *
     * @param time "HH:mm:ss"
     * @return
     */
    private static long getTimeMillis(String time) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
            DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");
            Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " " + time);
            return curDate.getTime();
        } catch (Exception e) {
            logger.error("[CrontabUtils getTimeMillis]", e);
        }
        return 0;
    }

    /**
     * Sleep thread without exception.
     * 
     * @param millis
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}

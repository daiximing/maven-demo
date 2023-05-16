package com.ddup.thread;

import java.util.concurrent.*;

/**
 * @author Dai Ximing
 * @create 2023-05-10 8:55
 * 线程的四种创建方式
 */
public class HelloThread {

    public static void main(String[] args) throws Exception {
        //startThreadByThread();
        //startThreadByRunnable();
        //startThreadByCallable();
        //startThreadByThreadPool();
    }

    /**
     * 继承 Thread 类，代码方式为使用匿名内部类方式
     */
    public static void startThreadByThread(){
        Thread t1 = new Thread(() -> {
            System.out.println("start a thread by Thread.");
        });
        t1.start();
    }

    /**
     * 实现 Runnable 接口
     */
    public static void startThreadByRunnable(){
        Runnable r1 = () -> System.out.println("start a thread by Runnable.");
        r1.run();
    }

    /**
     * 实现 Callable 接口，此种方式可以获取线程内的返回值
     * @throws Exception
     */
    public static void startThreadByCallable() throws Exception {
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            System.out.println("start a thread by Callable.");
            return "Callable";
        });
        Thread thread = new Thread(futureTask);
        thread.start();
        System.out.println(futureTask.get());
    }


    /**
     * 线程池，线程执行时需要一个 Runnable 类型的参数
     * @throws Exception
     */
    public static void startThreadByThreadPool() throws Exception{
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                3,
                5,
                2000,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(100),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());

        threadPool.execute(() -> {
            System.out.println("start a thread by threadPool.");
        });
    }
}

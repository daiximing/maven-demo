package com.ddup.futureTask;

import java.util.concurrent.*;

/**
 * @author Dai Ximing
 * @create 2023-05-16 10:48
 * 使用 FutureTask 的方式
 */
public class HelloFutureTask {

    public static void main(String[] args) {
        //startFutureTaskByThread();

        startFutureTaskByThreadPool();
    }

    /**
     * 使用 ThreadPool 运行 FutureTask
     */
    private static void startFutureTaskByThreadPool() {
        ExecutorService threadPool = Executors.newFixedThreadPool(100);

        try {
            FutureTask<String> futureTask = new FutureTask(() -> {
                System.out.println("start FutureTask by ThreadPool.");
                return "futureTask over";
            });

            threadPool.execute(futureTask);

            String result = futureTask.get();
            System.out.println(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            threadPool.shutdown();
        }
    }

    /**
     * 使用 Thread 运行 FutureTask
     */
    private static void startFutureTaskByThread() {
        FutureTask<String> futureTask = new FutureTask(() -> {
            System.out.println("start FutureTask by Thread.");
            return "futureTask over";
        });

        Thread thread = new Thread(futureTask);
        thread.start();

        try {
            String result = futureTask.get();
            System.out.println(result);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}

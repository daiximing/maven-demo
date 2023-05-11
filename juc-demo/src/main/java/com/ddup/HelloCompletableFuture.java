package com.ddup;

import java.util.concurrent.*;

/**
 * @author Dai Ximing
 * @create 2023-05-10 14:46
 */
public class HelloCompletableFuture {

    /**
     * 简易生成线程池，此种方式不推荐
     */
    public static ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws Exception {
        //runAsync();
        supplyAsync();
    }

    /**
     * 通过 CompletableFuture 执行子线程任务，无返回，没有实际意义。
     */
    public static void runAsync() {
        CompletableFuture completableFuture = CompletableFuture.runAsync(() -> {
            System.out.println("ThreadName: " + Thread.currentThread().getName());
            try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { throw new RuntimeException(e); }
        }, threadPool);
        /* CompletableFuture 对比与 FutureTask 新增了 join() 方法，使用它不需要捕捉异常。 */
        /* 因为 runAsync 没有返回值，所以代码输出 null。 */
        System.out.println(completableFuture.join());
    }

    public static void supplyAsync() {
        try {
            CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
                System.out.println("ThreadName: " + Thread.currentThread().getName());
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                int result = ThreadLocalRandom.current().nextInt(100);
                System.out.println("子线程结果：" + result);
                return result;
            }, threadPool).whenComplete((v, e) -> {
                /* whenComplete 通过链式编程获取子进程的返回值和异常情况，并且可以多次调用 */
                if (e == null) {
                    System.out.println("第一次 whenComplete 获取结果：" + v);
                }
            }).whenComplete((v, e) -> {
                if (e == null) {
                    v++;
                    System.out.println("第二次 whenComplete 将第一次的结果进行 +1：" + v);
                }
            }).exceptionally(e -> {
                /* 使用 exceptionally 捕捉异常*/
                e.printStackTrace();
                System.out.println(e.getCause());
                System.out.println(e.getMessage());
                return null;
            });

            System.out.println("主线程执行-----");
            /* 主线程获取子线程的结果时仍然处于阻塞状态，其结果为 return 返回的结果，与 whenComplete 的操作无关。 */
            System.out.println("主线程获取子线程的结果：" + completableFuture.join());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            threadPool.shutdown();
        }
    }
}

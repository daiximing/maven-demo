package com.ddup.futureTask;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * @author Dai Ximing
 * @create 2023-05-16 10:56
 * FutureTask Demo
 */
public class FutureTaskDemo {

    public static void main(String[] args) throws Exception {
        calculateCostTimeByUseFutureTask();
    }

    /**
     * 计算使用 FutureTask 所需要的时间
     * 结论：主线程会等待每个子线程的`get()`方法成功返回后，才会继续执行下去。如果需要根据子进程的结果再进行计算，那么此时使用多线程和使用单线程的效率是一样的。
     * @throws Exception
     */
    private static void calculateCostTimeByUseFutureTask() throws Exception {
        long startTime = System.currentTimeMillis();

        FutureTask<String> futureTask1 = new FutureTask(() -> {
            try { TimeUnit.MILLISECONDS.sleep(3000); } catch (InterruptedException e) { throw new RuntimeException(e); }
            return "futureTask1 over";
        });
        new Thread(futureTask1).start();
        System.out.println(futureTask1.get());

        FutureTask<String> futureTask2 = new FutureTask(() -> {
            try { TimeUnit.MILLISECONDS.sleep(4000); } catch (InterruptedException e) { throw new RuntimeException(e); }
            return "futureTask2 over";
        });
        new Thread(futureTask2).start();
        System.out.println(futureTask2.get());

        FutureTask<String> futureTask3 = new FutureTask(() -> {
            try { TimeUnit.MILLISECONDS.sleep(5000); } catch (InterruptedException e) { throw new RuntimeException(e); }
            return "futureTask3 over";
        });
        new Thread(futureTask3).start();
        System.out.println(futureTask3.get());

        long endTime = System.currentTimeMillis();

        System.out.println("Time Cost: " +  (endTime - startTime));
    }
}

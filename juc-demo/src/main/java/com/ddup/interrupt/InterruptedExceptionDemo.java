package com.ddup.interrupt;

import java.util.concurrent.TimeUnit;

/**
 * @author Dai Ximing
 * @create 2023-05-15 14:01
 * 测试线程状态设置为 true 后，线程是否会立即停止
 */
public class InterruptedExceptionDemo {

    public static void main(String[] args) throws Exception {
        interruptedDemo1();
        interruptedDemo2();
    }

    /**
     * 如果线程处于正常活动状态，那么会将该线程的中断标志设置为 true，仅此而已。被设置中断标志的线程将继续正常运行，不受影响。
     * 所以， `interrupt()` 并不能真正的中断线程，需要被调用的线程自己进行配合才行。
     *
     * 代码流程：
     * 1. 线程 t1 的初始中断标识为 **false**
     * 2. 当对 t1 线程执行`interrupt()` 方法后，t1 线程的中断标识为 **true**，但线程 t1 仍在继续循环遍历中，没有立刻停止
     * 3. 循环结束后，打印 t1 线程的最终中断标识为 **true**
     */
    private static void interruptedDemo1() {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 300; i++) {
                System.out.println("--------------：" + i);
            }
            System.out.println("线程 t1 的最终中断标识为：" + Thread.currentThread().isInterrupted());
        }, "t1");
        t1.start();

        System.out.println("线程 t1 的初始中断标识为：" + t1.isInterrupted());

        try { TimeUnit.MILLISECONDS.sleep(2); } catch (InterruptedException e) { throw new RuntimeException(e); }
        t1.interrupt();

        System.out.println("执行过 interrupt() 方法后，线程 t1 的中断标识为：" + t1.isInterrupted());
    }

    /**
     * 如果线程处于被阻塞状态（例如处于 **sleep**、**wait**、**join** 等状态），
     * 在别的线程中调用当前线程对象的`interrupt()`方法那么线程将立即退出被阻塞状态，并抛出一个`InterruptedException`异常。
     *
     * 代码流程：
     * 1. 线程 t1 中断标识默认为 **false**
     * 2. 线程 t2 调用`t1.interrupt()`方法后，线程 t1 中断标识为 **true**
     * 3. 当线程 t1 内没有 sleep 等方法时，经过 if 判断后，正常退出
     * 4. 当线程 t1 内有 sleep 等方法时，抛出`InterruptedException`异常，并重置中断标识为 **false**，需在 catch 块内再次调用`t1.interrupt()`，才能正常退出
     */
    private static void interruptedDemo2(){
        Thread t1 = new Thread(() -> {
            while (true){
                if(Thread.currentThread().isInterrupted()){
                    System.out.println("线程 t1 正在停止......");
                    break;
                }

                System.out.println("线程 t1 正在运行。");

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    System.out.println("catch 中的 t1 的中断标识为：" + Thread.currentThread().isInterrupted());
                    /* 使用 sleep 方法后，重点标识被重置了，如果不再次标识中断，将无限循环下去。 */
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }

            }
        }, "t1");
        t1.start();

        try { TimeUnit.MILLISECONDS.sleep(1000); } catch (InterruptedException e) { throw new RuntimeException(e); }

        System.out.println("线程 t1 的中断标识：" + t1.isInterrupted());

        new Thread(() -> {
            t1.interrupt();
        }, "t2").start();
    }
}

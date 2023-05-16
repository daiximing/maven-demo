package com.ddup.lockSupport;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Dai Ximing
 * @create 2023-05-15 22:01
 * 三种唤醒模式对比
 */
public class LockSupportDemo {

    public static void main(String[] args) {
        //wakeUpByObject();
        //wakeUpByCondition();
        wakeUpByLockSupport();
    }

    /**
     * LockSupport 解决了 Object 和 Condition 方式唤醒线程的问题：
     * 1. 阻塞和唤醒不需要在同步代码块中。
     * 2. 即使唤醒线程的代码先执行，因为有”许可证“的原因，执行阻塞代码后也不会一直处于阻塞状态。
     *
     *  注意：
     * 如果在代码中写入了两次`LockSupport.park();`和`LockSupport.unpark(t1);`。
     * 因为每个线程只有一个“许可证”的原因，即使执行了两次`LockSupport.unpark(t1);`，也只有一个“许可证”，但因为执行了两次`LockSupport.park();`，需要两个许可证才能退出阻塞状态。
     * 所以程序这时会处于阻塞状态。
     */
    private static void wakeUpByLockSupport() {
        Thread t1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " come in.");
            LockSupport.park();
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + " come out.");
        }, "t1");
        t1.start();

        try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { throw new RuntimeException(e); }

        new Thread(() -> {
            LockSupport.unpark(t1);
            LockSupport.unpark(t1);
            System.out.println("唤醒 t1 线程。");
        }, "t2").start();
    }

    /**
     * 注意事项：同 Object 方式
     */
    private static void wakeUpByCondition() {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        new Thread(() -> {
            lock.lock();
            try {

                System.out.println(Thread.currentThread().getName() + " come in.");
                condition.await();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
            System.out.println(Thread.currentThread().getName() + " come out.");
        }, "t1").start();

        try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { throw new RuntimeException(e); }

        new Thread(() -> {
            lock.lock();
            try {
                condition.signal();
                System.out.println("唤醒 t1 线程。");
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }, "t2").start();
    }

    /**
     * 注意事项：
     * 1. 阻塞何唤醒的代码必须在`synchronized`代码块内，否则会报`java.lang.IllegalMonitorStateException`异常。
     * 2. 在正常唤醒的代码中，在调用唤醒方法前 sleep 2 秒，保证线程 t1 先阻塞，线程 t2 后唤醒。但如果线程 t2 先唤醒，线程 t1 后阻塞，则线程会一直处于阻塞状态。
     */
    private static void wakeUpByObject() {
        Object object = new Object();

        new Thread(() -> {
            synchronized (object) {
                System.out.println(Thread.currentThread().getName() + " come in.");
                try {
                    object.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(Thread.currentThread().getName() + " come out.");
            }
        }, "t1").start();


        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            synchronized (object) {
                object.notify();
                System.out.println("唤醒 t1 线程。");
            }
        }, "t2").start();
    }
}

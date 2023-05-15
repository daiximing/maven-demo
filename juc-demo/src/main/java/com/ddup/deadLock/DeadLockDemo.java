package com.ddup.deadLock;

import java.util.concurrent.TimeUnit;

/**
 * @author Dai Ximing
 * @create 2023-05-12 16:05
 */
public class DeadLockDemo {

    static Object a = new Object();
    static Object b = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (a){
                System.out.println("线程 t1 进入，锁定 a 对象。");
                try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { throw new RuntimeException(e); }
                synchronized (b){
                    System.out.println("线程 t1 进入，锁定 b 对象。");
                }
            }
        }, "t1").start();
        new Thread(() -> {
            synchronized (b){
                System.out.println("线程 t2 进入，锁定 b 对象。");
                try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { throw new RuntimeException(e); }
                synchronized (a){
                    System.out.println("线程 t2 进入，锁定 a 对象。");
                }
            }
        }, "t2").start();
    }
}

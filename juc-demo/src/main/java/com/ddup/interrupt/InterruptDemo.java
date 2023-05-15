package com.ddup.interrupt;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Dai Ximing
 * @create 2023-05-15 13:31
 * 中断线程的三种方法
 */
public class InterruptDemo {


    public static void main(String[] args) {
        //methodByVolatile();
        //methodByAtomicBoolean();
        methodByThread();
    }

    public static volatile boolean isStop = false;
    public static void methodByVolatile() {
        new Thread(() -> {
            while (true){
                if(isStop){
                    System.out.println(Thread.currentThread().getName() + " 线程停止了。");
                    break;
                }
                System.out.println("------hello volatile-------");
            }
        }, "t1").start();

        try { TimeUnit.MILLISECONDS.sleep(10); } catch (InterruptedException e) { throw new RuntimeException(e); }

        new Thread(() -> {
            isStop = true;
        }, "t2").start();
    }

    public static AtomicBoolean atomicBoolean = new AtomicBoolean(false);

    public static void methodByAtomicBoolean() {
        new Thread(() -> {
            while (true){
                if(atomicBoolean.get()){
                    System.out.println(Thread.currentThread().getName() + " 线程停止了。");
                    break;
                }
                System.out.println("------hello atomicBoolean-------");
            }
        }, "t1").start();

        try { TimeUnit.MILLISECONDS.sleep(10); } catch (InterruptedException e) { throw new RuntimeException(e); }

        new Thread(() -> {
            atomicBoolean.set(true);
        }, "t2").start();
    }


    public static void methodByThread(){
        Thread t1 = new Thread(() -> {
            while (true){
                if(Thread.currentThread().isInterrupted()){
                    System.out.println(Thread.currentThread().getName() + " 线程标志位设置为 true，程序停止。");
                    break;
                }
                System.out.println("------hello thread-------");
            }
        }, "t1");
        t1.start();

        try { TimeUnit.MILLISECONDS.sleep(10); } catch (InterruptedException e) { throw new RuntimeException(e); }

        new Thread(() -> {
            t1.interrupt();
        }, "t2").start();
    }
}

package com.ddup.interrupt;

/**
 * @author Dai Ximing
 * @create 2023-05-15 17:23
 * 关于静态方法`Thread.interrupted()`
 */
public class staticInterruptedDemo {
    public static void main(String[] args) {
        staticInterruptMethodDemo();
    }

    /**
     * 这个方法做了两件事：
     * 1. 返回当前线程的中断状态，测试当前线程是否已被中断。
     * 2. 将当前线程的中断状态清零并重新设为 false，清除线程的中断状态。
     *
     * 代码流程：
     * 1. 默认线程标识为 false
     * 2. 执行第一个`Thread.interrupted()`返回当前中断标识 **false**，并重置为 **false**
     * 3. 执行第二个`Thread.interrupted()`返回当前中断标识 **false**，并重置为 **false**
     * 4. 打印 **before interrupt**
     * 5. 修改线程中断标识为 **true**
     * 6. 打印 **after interrupt**
     * 7. 执行第三个`Thread.interrupted()`返回当前中断标识 **true**，并重置为 **false**
     * 8. 执行第四个`Thread.interrupted()`返回当前中断标识 **false**，并重置为 **false**
     */
    private static void staticInterruptMethodDemo() {
        System.out.println("Thread.interrupted() = " + Thread.interrupted());
        System.out.println("Thread.interrupted() = " + Thread.interrupted());
        System.out.println("before interrupt");
        Thread.currentThread().interrupt();
        System.out.println("after interrupt");
        System.out.println("Thread.interrupted() = " + Thread.interrupted());
        System.out.println("Thread.interrupted() = " + Thread.interrupted());
    }


}

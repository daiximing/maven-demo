package com.ddup.lock;

import java.util.concurrent.TimeUnit;

/**
 * @author Dai Ximing
 * @create 2023-05-12 8:59
 * 8 锁模型
 */
public class LockDemoOf8Lock {
    public static void main(String[] args) {
        //lock1();
        //lock2();
        //lock3();
        //lock4();
        //lock5();
        //lock6();
        //lock7();
        //lock8();
    }

    /**
     * 1. 标准访问，a、b两个线程执行不同的静态方法，请问先打印邮件还是短信？
     *
     * 执行结果：
     * ---------sendEmail---------
     * ---------sendMessage---------
     */
   public static void lock1(){
        Phone1 phone = new Phone1();
        new Thread(() -> {
            phone.sendEmail();
        }, "a").start();
        new Thread(() -> {
            phone.sendMessage();
        }, "b").start();
    }


    /**
     * 2. sendEmail 方法中暂停 3 秒，请问先打印邮件还是短信？
     *
     * 执行结果：
     * ---------sendEmail---------
     * ---------sendMessage---------
     *
     * 结论：
     * 一个对象里面如果有多个`synchronized`方法。
     * 某一个时刻内，只要一个线程去调用其中的一个`synchronized`方法了，其它的线程都只能等待。
     * 换句话说，一个时刻内，只能有唯一的一个线程去访问这些`synchronized`方法。
     * 锁的是当前对象`this`，被锁定后，其它的线程都不能进入到当前对象的其它的`synchronized`方法。
     */
    public static void lock2(){
        Phone2 phone = new Phone2();
        new Thread(() -> {
            phone.sendEmail();
        }, "a").start();
        new Thread(() -> {
            phone.sendMessage();
        }, "b").start();
    }

    /**
     * 3. 添加一个普通的 hello 方法，请问先打印邮件还是 hello ？
     *
     * 执行结果：
     * ---------hello---------
     * ---------sendEmail---------
     *
     * 结论：
     * hello 是普通实例方法，与锁没有竞争关系。
     */
    public static void lock3(){
        Phone3 phone = new Phone3();
        new Thread(() -> {
            phone.sendEmail();
        }, "a").start();
        new Thread(() -> {
            phone.hello();
        }, "b").start();
    }

    /**
     * 4. 有两部手机，分别执行发邮件和短信，请问先发邮件还是短信？
     *
     * 执行结果：
     * ---------sendMessage---------
     * ---------sendEmail---------
     *
     * 结论：
     * 跟案例二相同，因为访问`synchronized`方法时锁的是当前的对象，两个不同的对象互不影响。
     */
    public static void lock4(){
        Phone4 phone1 = new Phone4();
        Phone4 phone2 = new Phone4();
        new Thread(() -> {
            phone1.sendEmail();
        }, "a").start();
        new Thread(() -> {
            phone2.sendMessage();
        }, "b").start();
    }

    /**
     * 5. 一部手机 ，访问两个静态同步方法，请问先发邮件还是短信？
     *
     * 执行结果：
     * ---------sendEmail---------
     * ---------sendMessage---------
     */
    public static void lock5(){
        Phone5 phone = new Phone5();
        new Thread(() -> {
            phone.sendEmail();
        }, "a").start();
        new Thread(() -> {
            phone.sendMessage();
        }, "b").start();
    }

    /**
     * 6. 两部手机，访问两个静态同步方法，请问先发邮件还是短信？
     *
     * 执行结果：
     * ---------sendEmail---------
     * ---------sendMessage---------
     *
     * 结论：
     * 针对案例五、六我们要理解`synchronized`的三种使用情况，锁的对象是不同的。
     * 1. 同步方法：锁的是当前实例对象，也就是`this`，如 **案例一、二**。
     * 2. 静态方法：锁的是当前对象的`Class`对象，在**案例五、六**中也就是`Phone.Class`对象，它是生成`Phone`对象的模板，只有一份。
     * 3. 同步代码块：锁的是`synchronized`括号里的对象。
     * 根据以上的内容，很清楚就明白里**案例七、八**，因为一个锁的是`this`对象，一个是`Class`对象。
     */
    public static void lock6(){
        Phone6 phone1 = new Phone6();
        Phone6 phone2 = new Phone6();
        new Thread(() -> {
            phone1.sendEmail();
        }, "a").start();
        new Thread(() -> {
            phone2.sendMessage();
        }, "b").start();
    }

    /**
     * 7. 一部手机，一个静态同步方法，一个普通同步方法
     */
    public static void lock7(){
        Phone7 phone = new Phone7();
        new Thread(() -> {
            phone.sendEmail();
        }, "a").start();
        new Thread(() -> {
            phone.sendMessage();
        }, "b").start();
    }

    /**
     *8. 两部手机，一个静态同步方法，一个普通同步方法
     */
    public static void lock8(){
        Phone8 phone1 = new Phone8();
        Phone8 phone2 = new Phone8();
        new Thread(() -> {
            phone1.sendEmail();
        }, "a").start();
        new Thread(() -> {
            phone2.sendMessage();
        }, "b").start();
    }
}

class Phone1{
    public  synchronized void sendEmail(){
        System.out.println("---------sendEmail---------");
    }
    public synchronized void sendMessage(){
        System.out.println("---------sendMessage---------");
    }
}

class Phone2{
    public  synchronized void sendEmail(){
        /* 为了使打印效果明显，阻塞 3 秒 */
        try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { throw new RuntimeException(e); }
        System.out.println("---------sendEmail---------");
    }
    public synchronized void sendMessage(){
        System.out.println("---------sendMessage---------");
    }
}

class Phone3{
    public synchronized void sendEmail(){
        /* 为了使打印效果明显，阻塞 3 秒 */
        try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { throw new RuntimeException(e); }
        System.out.println("---------sendEmail---------");
    }
    public  void hello(){
        System.out.println("---------hello---------");
    }
}

class Phone4 {

    public synchronized void sendEmail(){
        /* 为了使打印效果明显，阻塞 3 秒 */
        try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { throw new RuntimeException(e); }
        System.out.println("---------sendEmail---------");
    }

    public synchronized void sendMessage(){
        System.out.println("---------sendMessage---------");
    }
}

class Phone5 {

    public static synchronized void sendEmail(){
        /* 为了使打印效果明显，阻塞 3 秒 */
        try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { throw new RuntimeException(e); }
        System.out.println("---------sendEmail---------");
    }

    public static synchronized void sendMessage(){
        System.out.println("---------sendMessage---------");
    }
}

class Phone6 {

    public static synchronized void sendEmail(){
        /* 为了使打印效果明显，阻塞 3 秒 */
        try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { throw new RuntimeException(e); }
        System.out.println("---------sendEmail---------");
    }

    public static synchronized void sendMessage(){
        System.out.println("---------sendMessage---------");
    }
}

class Phone7 {

    public  synchronized void sendEmail(){
        /* 为了使打印效果明显，阻塞 3 秒 */
        try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { throw new RuntimeException(e); }
        System.out.println("---------sendEmail---------");
    }

    public static synchronized void sendMessage(){

        System.out.println("---------sendMessage---------");
    }
}

class Phone8 {

    public  synchronized void sendEmail(){
        /* 为了使打印效果明显，阻塞 3 秒 */
        try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { throw new RuntimeException(e); }
        System.out.println("---------sendEmail---------");
    }

    public static synchronized void sendMessage(){

        System.out.println("---------sendMessage---------");
    }
}
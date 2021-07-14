package cn.edkso.basethread.threadlocal;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: dingmd
 * @create: 2021-07-14 20:43
 * @description: 验证ThreadLocal内存泄漏
 *
 * 结果：①ThreadLocalMap存放的是Entry键值对，在Entry键值对中，key为Threadlocal，并且key是弱引用；
 *      当线程中指向ThreadLocal的引用断开，Entry中key指向ThreadLocal的弱并没有断开，此时Entry中
 *      ThreadLocal对象作为key对应得value并没有被回收，并且key对应的value也无法被访问到，就造成了内存泄漏。
 *      ②ThreadLocal中的set()、get()方法在被调用时,有几率会触发expungeStaleEntry()方法，
 *      对key为null(手动为null或垃圾回收时，会回收弱引用对象)的Entry对象和value进行垃圾回收。
 *      ③如果key时强引用，垃圾回收时，如果不是手动把ThreadLocal对象==null，则ThreadLocal对象不会被回收，
 *      因此ThreadLocal中的set()、get()方法在被调用时,有不会触发expungeStaleEntry()方法，造成严重内存泄漏事故！
 **/
public class ThreadLocalMemoryLeak {

    private static final int TASK_LOOP_SIZE = 500;

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 5,
            1, TimeUnit.MINUTES, new LinkedBlockingQueue<>());

    static class LocalVariable {
        private byte[] bytes = new byte[1024*1024*5];
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < TASK_LOOP_SIZE; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {

                    /*System.out.println("使用本地变量");
                    new LocalVariable();*/

                    System.out.println("使用ThreadLocal变量");
                    ThreadLocal<LocalVariable> threadLocal = new ThreadLocal<>();
                    threadLocal.set(new LocalVariable());

                    /*System.out.println("使用ThreadLocal变量,并手动remove()");
                    threadLocal.remove();*/
                }

            });
            Thread.sleep(100);
        }
    }
}

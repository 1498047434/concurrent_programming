package cn.edkso.basethread.syn;

/**
 * @author: dingmd
 * @create: 2021-07-10 22:28
 * @description: 类锁的本质
 *
 * 结果：①对象锁，若多个线程不是同一个对象锁（多个线程用的就不是同一把锁），锁无效
 *              若多个线程是同一个对象锁（多个线程用的就是同一把锁），锁生效[本示例不体现]
 *      ②类锁的本质是类的class对象，类的class对象在类加载时生成，一个类仅有有个class对象，
 *             多个线程是同一个对象锁（多个线程用的就是同一把锁），锁生效
 *      ③阻塞类异常捕获后，需要在对异常做处理后（比如资源释放）通知中断（interrupt()）
 **/
public class ClassLock extends Thread{


    public void fun(){
        Object unique = new Object();
        synchronized (unique){  //类锁
//        synchronized (Object.class){  //类锁
//        synchronized (unique.getClass()){ //类锁
            while (!isInterrupted()){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    /*捕获异常后要做的事情*/
                    interrupt(); // 通知中断
                }
                System.out.println(Thread.currentThread().getName() + ": 随便说点什么");
            }

        }
    }

    @Override
    public void run(){
        fun();
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new ClassLock();
        Thread t2 = new ClassLock();
        t1.start();
        t2.start();
        Thread.sleep(5000);
        t1.interrupt();
        t2.interrupt();
    }
}

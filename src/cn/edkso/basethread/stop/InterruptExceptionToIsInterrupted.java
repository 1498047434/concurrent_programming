package cn.edkso.basethread.stop;
/**
 * @author: dingmd
 * @create: 2021-07-09 22:23
 *
 * @description: 阻塞中断异常对中断状态的影响
 *               验证结果：捕获到阻塞中断异常后，会自动把中断标志位置位true，
 *                       线程会最做完最后的工作，之后主动调用 interrupt()把中断标志位置为true，
 **/
public class InterruptExceptionToIsInterrupted extends Thread{

    @Override
    public void run() {
        while (!interrupted()){
            try {
                sleep(100);
            } catch (InterruptedException e) {
                System.out.println("捕获阻塞中断异常、、、");
                /*停止线程前要做的事↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
                  .............最后的奋斗...............
                  停止线程前要做的事↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
                 */
                interrupt();
                System.out.println("循环内中断标志位为：" + isInterrupted());
            }
        }
        System.out.printf("循环外中断标志位为：" + isInterrupted());
    }

    public static void main(String[] args) throws InterruptedException {
        InterruptExceptionToIsInterrupted t = new InterruptExceptionToIsInterrupted();
        t.start();
        Thread.sleep(50);
        t.interrupt();
    }
}

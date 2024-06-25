import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ProducerConsumerDemo {
    private static final int CAPACITY = 1; // 托盘容量
    private final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(CAPACITY);

    class Producer implements Runnable {
        private int produceCount = 1;

        @Override
        public void run() {
            while (produceCount <= 10) {
                try {
                    synchronized (queue) {
                        queue.put(produceCount); // 尝试放入货物
                        System.out.println("Producer put: " + produceCount);
                        queue.notifyAll();
                        queue.wait();
                        //Thread.sleep(new Random().nextInt(100)); // sleep 0-100ms
                    }
                    produceCount++;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Producer interrupted: " + e.getMessage());
                    break;
                }
            }
        }
    }

    class Consumer implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    synchronized (queue) {
                        int goods = queue.take(); // 取出货物
                        System.out.println("Consumer get: " + goods);
                        if (goods == 10) { // 如果取到的是最后一个货物，结束程序
                            System.exit(0);
                        }
                        queue.notifyAll();
                        queue.wait();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Consumer interrupted: " + e.getMessage());
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        ProducerConsumerDemo demo = new ProducerConsumerDemo();

        Thread producer = new Thread(demo.new Producer());
        Thread consumer = new Thread(demo.new Consumer());

        producer.start();
        consumer.start();
    }
}
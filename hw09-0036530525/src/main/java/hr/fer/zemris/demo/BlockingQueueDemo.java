package hr.fer.zemris.demo;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingQueueDemo {

	public static void main(String[] args) {
		
		BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // Create producer thread
        Thread producer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
            	int x = i;
                try {
                    taskQueue.put(() -> {
                        System.out.println("Task " + x + " executed by " + Thread.currentThread().getName());
                        try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        producer.start();

        // Consume tasks from the queue
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                try {
                    Runnable task = taskQueue.take();
                    task.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
    }

}

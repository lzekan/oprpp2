package hr.fer.zemris.demo;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorsDemo {
	
	public static void main(String[] args) {
		ExecutorService executorService = Executors.newFixedThreadPool(1); // Creating a thread pool of 1 thread

        Runnable r = () -> {
        	for(int i = 0; i < 5; i++) {
        		System.out.println(i);
        		try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        };
        
        executorService.execute(r);
        executorService.shutdown();
        
        System.out.println("Main thread continues...");

	}

}

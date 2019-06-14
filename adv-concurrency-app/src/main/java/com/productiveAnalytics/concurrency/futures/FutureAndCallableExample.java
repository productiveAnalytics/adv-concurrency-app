package com.productiveAnalytics.concurrency.futures;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Using Callable and Future
 * 
 * @author lchawathe
 */
public class FutureAndCallableExample {
	private static int DELAY = 1 + (new Random().nextInt(9)); // minimum 1 second
	private static int ALLOWED_DELAY = 3; // Allow task to run upto 3 seconds
	
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Callable<String> callable = () -> {
            // Perform some computation
            System.out.println("Entered    Callable at "+ System.nanoTime() +" nanoSecs");
            System.out.println(String.format("will be busy for next %d seconds", DELAY));
            TimeUnit.SECONDS.sleep(DELAY);
            return "Hello from Callable at "+ System.nanoTime() +" nanoSecs";
        };

        System.out.println("Submitting Callable");
        Future<String> future = executorService.submit(callable);
        
        long startTime = System.nanoTime();
        
        // This line executes immediately
        System.out.println("Do something else while callable is getting executed");
        
        AtomicInteger atomicInt = new AtomicInteger(0);
        while(!future.isDone()) {
        	atomicInt.incrementAndGet();
            
            int elapsedTimeInSec = (int) TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - startTime);
            if (elapsedTimeInSec <= ALLOWED_DELAY) {
            	System.out.println(atomicInt.get() + ". Task is still not done...Snoozing again (1 sec) zzzZZZ");
            	TimeUnit.SECONDS.sleep(1);
            }
            else {
            	System.out.println(atomicInt.get() + ". Task is still not done...Time to cancel !!!");
            	future.cancel(true);
            }
        }

        if(!future.isCancelled()) {
	        System.out.println("Retrieve the result of the future");
	        // Future.get() blocks until the result is available
	        String result = future.get();
	        System.out.println(result);
        } else {
        	System.out.println(String.format("Future was cancelled as it more than %d seconds !!!", ALLOWED_DELAY));
        }

        executorService.shutdown();
    }

}
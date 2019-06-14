package com.productiveAnalytics.concurrency.futures;

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
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Callable<String> callable = () -> {
            // Perform some computation
            System.out.println("Entered    Callable at "+ System.nanoTime() +" nanoSecs");
            TimeUnit.SECONDS.sleep(5);
            return "Hello from Callable at "+ System.nanoTime() +" nanoSecs";
        };

        System.out.println("Submitting Callable");
        Future<String> future = executorService.submit(callable);
        
        // This line executes immediately
        System.out.println("Do something else while callable is getting executed");
        
        AtomicInteger atomicInt = new AtomicInteger(0);
        while(!future.isDone()) {
            System.out.println(atomicInt.incrementAndGet() + ". Task is still not done...");
            TimeUnit.SECONDS.sleep(1);
        }

        System.out.println("Retrieve the result of the future");
        // Future.get() blocks until the result is available
        String result = future.get();
        System.out.println(result);

        executorService.shutdown();
    }

}
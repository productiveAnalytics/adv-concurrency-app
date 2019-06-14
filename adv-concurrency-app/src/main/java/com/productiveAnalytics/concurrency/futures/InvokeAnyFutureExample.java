package com.productiveAnalytics.concurrency.futures;

import java.util.Arrays;
import java.util.concurrent.*;

/**
 * The invokeAny() method accepts a collection of Callables 
 * and returns the result of the fastest Callable
 * 
 * @author lchawathe
 */
public class InvokeAnyFutureExample {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        Callable<String> task1 = () -> {
            Thread.sleep(2000);
            return "Result of Task1";
        };

        Callable<String> task2 = () -> {
            Thread.sleep(5000);
            return "Result of Task2";
        };

        // FASTEST
        Callable<String> task3 = () -> {
            Thread.sleep(1000);
            return "Result of Task3";
        };
        
        Callable<String> task4 = () -> {
            Thread.sleep(3000);
            return "Result of Task4";
        };

        // Returns the result of the fastest callable. (task2 in this case)
        String result = executorService.invokeAny(Arrays.asList(task1, task2, task3, task4));

        System.out.println(result);

        executorService.shutdown();
    }
}

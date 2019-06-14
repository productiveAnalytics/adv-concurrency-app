package com.productiveAnalytics.concurrency.executors;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author LChawathe
 */
public class ScheduledExecutorExample {
	private static int SCHEDULE_LAG = 1 + (new Random().nextInt(9)); // minimum 1 second
	
    public static void main(String[] args) {
        scheduleExecutorWithRunnable();
        try {
			scheduleExecutorWithCallable();
		} catch (InterruptedException | ExecutionException ex) {
			ex.printStackTrace();
		}
    }

	private static void scheduleExecutorWithRunnable() {
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        Runnable runnableTask = () -> {
          System.out.println("{Runnable} Executing  task at " + System.nanoTime());
        };

        System.out.println(String.format("{Runnable} Submitting task at %s to be executed after %d seconds.", System.nanoTime(), SCHEDULE_LAG));
        scheduledExecutorService.schedule(runnableTask, SCHEDULE_LAG, TimeUnit.SECONDS);
        
        scheduledExecutorService.shutdown();
	}
	
	private static void scheduleExecutorWithCallable() throws InterruptedException, ExecutionException {
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        Callable<Long> callableTask = () -> {
          final long execTime = System.nanoTime();
          System.out.println("[Callable] Executing  task at " + execTime);
          return Long.valueOf(execTime);
        };

        final long scheduleTime = System.nanoTime(); 
        System.out.println(String.format("[Callable] Submitting task at %d to be executed after %d seconds.", scheduleTime, SCHEDULE_LAG));
        ScheduledFuture<Long> scheduledFuture = scheduledExecutorService.schedule(callableTask, SCHEDULE_LAG, TimeUnit.SECONDS);
        Long execTimeLong = scheduledFuture.get();
        long nanosDiff = (execTimeLong.longValue() - scheduleTime);
        int secondsDiff = (int) TimeUnit.NANOSECONDS.toSeconds(nanosDiff);
        System.out.println(String.format("[Callable] Total execution time %d (seconds)", secondsDiff));
        
        assert (SCHEDULE_LAG == secondsDiff);
        
        scheduledExecutorService.shutdown();
	}
}
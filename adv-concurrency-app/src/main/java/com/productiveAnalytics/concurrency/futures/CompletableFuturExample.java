package com.productiveAnalytics.concurrency.futures;

import java.util.Random;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

// https://www.callicoder.com/java-8-completablefuture-tutorial/
public class CompletableFuturExample {

	public static void main(String[] args) {
		System.out.println("main(): Started at "+ System.nanoTime());
		try {
			greetWithSimpleCompletableFuture();
		} catch (InterruptedException | ExecutionException ex) {
			ex.printStackTrace();
		}
		System.out.println("main(): Completed at "+ System.nanoTime());
	}

	private static void greetWithSimpleCompletableFuture() throws InterruptedException, ExecutionException {
		System.out.println("greetWithCompletableFuture() Started at "+ System.nanoTime());
		// Create a CompletableFuture
		CompletableFuture<String> whatsYourNameFuture = CompletableFuture.supplyAsync(() -> {
		   final int randomDelay = (new Random()).nextInt(9) + 1;
		   try {
			   System.out.println(String.format("Task would take %d second(s) to perform", randomDelay));
		       TimeUnit.SECONDS.sleep(randomDelay);
		   } catch (InterruptedException e) {
		       throw new IllegalStateException(e);
		   }
		   return "ProductiveAnalytics-"+randomDelay;
		});
	
		// Attach a callback to the Future using thenApply()
		CompletableFuture<String> greetingFuture = whatsYourNameFuture.thenApply(name -> {
		   return "Hello " + name;
		});
	
		// Block and get the result of the future.
		System.out.println(greetingFuture.get()); // Hello ProductiveAnalytics
		System.out.println("greetWithCompletableFuture() Completed at "+ System.nanoTime());
	}

}
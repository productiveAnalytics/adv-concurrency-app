package com.productiveAnalytics.concurrency.futures;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.productiveAnalytics.concurrency.model.UserDetails;

/**
 * thenCompose():
 * 			If your callback function returns a CompletableFuture, 
 * 			and you want a "flattened" result from the CompletableFuture chain (which in most cases you would), 
 * 			then use thenCompose().
 * 
 * thenCombine():
 * 			thenCompose() is used to combine two Futures where one future is dependent on the other,
 * 			thenCombine() is used when you want two Futures to run independently, 
 * 			and do something after BOTH are complete.
 * 
 * 
 * @author lchawathe
 */
public class ComposeAndCombineFutureExample {
	// US format : Jun 30, 2010 7:03:47 AM PDT
	private static final DateFormat DATE_FORMAT = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.LONG, Locale.getDefault());
	
	private static final UserDetails NEW_USER = new UserDetails(100, "Appleton, Saee");
	private static UserDetails EXISTING_ACTIVE_USER;
	private static UserDetails EXISTING_INACTIVE_USER;
	
	static {
		LocalDateTime localDatetime;
		
		try {
			EXISTING_ACTIVE_USER = new UserDetails(200, "Lemonid, Marcus", true, DATE_FORMAT.parse("Jan 31, 2019 11:30:45 PM CST"));
		} catch (ParseException e) {
			localDatetime = LocalDateTime.of(2019, Month.JANUARY, 31, 11, 30, 45);
			EXISTING_ACTIVE_USER = new UserDetails(200, "Lemonid, Mark", true, Date.from(localDatetime.atZone(ZoneId.systemDefault()).toInstant()));
		}
		
		localDatetime = LocalDateTime.of(2000, Month.DECEMBER, 31, 11, 59, 59);
		EXISTING_INACTIVE_USER = new UserDetails(300, "Gandhi, Rahul", false, Date.from(localDatetime.atZone(ZoneId.systemDefault()).toInstant()));
	}
	
	public static void main(String[] args) {
		try {
			demoCompose();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			demoCombine();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static int getRandomDelay() {
		return 1 + new Random().nextInt(10);
	}
	
/*~~~~~~~~~ Using thenCompose() ~~~~~~~~~*/ 	
	private static CompletableFuture<UserDetails> getUsersDetail(int userId) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				final int delayInSeconds = getRandomDelay();
		    	System.out.println("getUsersDetail() will take "+ delayInSeconds +" seconds");
		        TimeUnit.SECONDS.sleep(delayInSeconds);
				switch (userId) {
					case 300 : return EXISTING_INACTIVE_USER;
					
					case 200 : return EXISTING_ACTIVE_USER;
					
					default: throw new RuntimeException("Not existing user :" + userId);
				}
			} catch (InterruptedException e) {
				return NEW_USER;
			}
		});	
	}
	
	private static CompletableFuture<Double> getCreditRating(UserDetails user) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				final int delayInSeconds = getRandomDelay();
		    	System.out.println("getCreditRating() will take "+ delayInSeconds +" seconds");
		        TimeUnit.SECONDS.sleep(delayInSeconds);
				switch (user.getUserId()) {
					case 300 : return 450.25d;
					
					case 200 : return 800.75d;
					
					default: throw new RuntimeException("Not existing user :" + user.getUserId());
				}
			} catch (InterruptedException e) {
				return 300.03d;
			}
			
		});
	}
	
	private static void demoCompose() throws InterruptedException, ExecutionException {
		long startNano, finishNano;
		
		System.out.println(">>>Start  call for active user: "+ (startNano = System.nanoTime()));
		// Using Chaining that gives "nested" CompletableFuture
		CompletableFuture<CompletableFuture<Double>> nestedFuture = getUsersDetail(200).thenApply(activeUser -> getCreditRating(activeUser));
		System.out.println("Active  User 200 has credit score : "+ nestedFuture.get().get());
		System.out.println("<<<Finish call for active user: "+ (finishNano = System.nanoTime()));
		System.out.println("Total call time for active user (micro secs) : "+ TimeUnit.NANOSECONDS.toMicros(finishNano-startNano));
		
		System.out.println();
		
		System.out.println("~~~ Demonstrating thenCompose() ~~~");
		System.out.println(">>>Start  call for inactive user: "+ (startNano = System.nanoTime()));
		// Using theCompose() to get flattened CompletableFuture
		CompletableFuture<Double> flattenedFuture = getUsersDetail(300).thenCompose(inactiveUser -> getCreditRating(inactiveUser));
		System.out.println("Inative User 300 has credit score : "+ flattenedFuture.get());
		System.out.println("<<<Finish call for inactive user: "+ (finishNano = System.nanoTime()));
		System.out.println("Total call time for inactive user (micro secs) : "+ TimeUnit.NANOSECONDS.toMicros(finishNano-startNano));
		System.out.println("~~~ Demonstrating thenCompose() ~~~");
	}
/*~~~~~~~~~ Using thenCompose() ~~~~~~~~~*/	
	
	
/*========= Using thenCombine() =========*/
	private static CompletableFuture<Double> getWeightInKgFuture() {
		return CompletableFuture.supplyAsync(() -> {
			System.out.println("Retrieving weight at "+ System.nanoTime());
		    try {
		    	final int delayInSeconds = getRandomDelay();
		    	System.out.println("weightInKgFuture() will take "+ delayInSeconds +" seconds");
		        TimeUnit.SECONDS.sleep(delayInSeconds);
		    } catch (InterruptedException e) {
		       throw new IllegalStateException(e);
		    }
		    return 65.0;
		});
	}
	
	private static CompletableFuture<Double> getHeightInCmFuture() {
		return CompletableFuture.supplyAsync(() -> {
		
			System.out.println("Retrieving height at "+ System.nanoTime());
		    try {
		    	final int delayInSeconds = getRandomDelay();
		    	System.out.println("heightInCmFuture() will take "+ delayInSeconds +" seconds");
		        TimeUnit.SECONDS.sleep(delayInSeconds);
		    } catch (InterruptedException e) {
		       throw new IllegalStateException(e);
		    }
		    return 177.8;
		});
	}
	
	private static void demoCombine() throws InterruptedException, ExecutionException {
		long startNano, finishNano;

		CompletableFuture<Double> wtInKgsFuture = getWeightInKgFuture();
		CompletableFuture<Double> htInCmsFuture = getHeightInCmFuture();
		
		System.out.println("=== Demonstrating thenCombine() ===");
		System.out.println("Calculating BMI at "+ (startNano = System.nanoTime()));
		CompletableFuture<Double> combinedFuture = wtInKgsFuture
		        										.thenCombine(htInCmsFuture, 
		        													 (weightInKg, heightInCm) -> {
		        														 Double heightInMeter = heightInCm/100;
		        														 return weightInKg/(heightInMeter*heightInMeter);
		        													 }
		        													);
		System.out.println("Your BMI is - " + combinedFuture.get() + " at "+ (finishNano = System.nanoTime()));
		System.out.println("Total time for calculating BMI (micro secs) : "+ TimeUnit.NANOSECONDS.toMicros(finishNano-startNano));
		System.out.println("=== Demonstrating thenCombine() ===");		
	}
/*========= Using thenCombine() =========*/
	
}

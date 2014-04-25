package org.jatakasource.common.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ConcurrencyUtils {	
	private static final int NUM_CPU = Runtime.getRuntime().availableProcessors();
	private static final ExecutorService OUTER_SHARED_POOL = getTightThreadPool(.81d);
	private static final ExecutorService INNER_SHARED_POOL = getTightThreadPool(2.1d);
			
	private ConcurrencyUtils() {
		throw new RuntimeException(ConcurrencyUtils.class.getName() + " is a static class");
	}
	
	/**
	 * @return an {@link ExecutorService} with One worker thread for each CPUm A {@link SynchronousQueue} and a {@link ThreadPoolExecutor.CallerRunsPolicy}
	 */
	public static ExecutorService getTightThreadPool(){
		return getTightThreadPool(1d);
	}
	
	/**
	 * @param threadToCpuRatio - for example, assuming you have 2 CPUs and setting a threadToCpuRation to 3, the result will be a pool with 6 working threads.  
	 * @return an {@link ExecutorService} with defined amount of worker thread for each CPUm A {@link SynchronousQueue} and a {@link ThreadPoolExecutor.CallerRunsPolicy}
	 */
	public static ExecutorService getTightThreadPool(double threadToCpuRatio) {
		int workingThreads = Double.valueOf(NUM_CPU * threadToCpuRatio).intValue();
		return new ThreadPoolExecutor(workingThreads, workingThreads, 60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(true), new ThreadPoolExecutor.CallerRunsPolicy());
	}
	
	/**
	 * @param threadToCpuRatio - for example, assuming you have 2 CPUs and setting a threadToCpuRation to 3, the result will be a pool with 6 working threads.  
	 * @return an {@link ExecutorService} with defined amount of worker thread for each CPUm A {@link SynchronousQueue} and a {@link ThreadPoolExecutor.CallerRunsPolicy}
	 */
	public static ExecutorService getQueuedThreadPool(double threadToCpuRatio, int queueCapacity) {
		int workingThreads = Double.valueOf(NUM_CPU * threadToCpuRatio).intValue();
		return new ThreadPoolExecutor(workingThreads, workingThreads, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(queueCapacity), new ThreadPoolExecutor.CallerRunsPolicy());
	}
	
	/**
	 * @return the number of available CPUs on the current machine - a positive integer
	 */
	public static int getNumberOfCPUs() {
		return NUM_CPU;
	}

	public static ExecutorService getSharedOuterPool() {
		return OUTER_SHARED_POOL;
	}
	
	public static ExecutorService getSharedInnerPool() {
		return INNER_SHARED_POOL;
	}
}

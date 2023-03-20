package cn.cnaworld.framework.infrastructure.utils.threadpool;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * 内部测试线程池工具
 * @author Lucifer
 * @date 2023/3/14
 * @since 1.0.0
 */
public class ThreadPoolUtil {
	
	private static final ExecutorService executorService = Executors.newFixedThreadPool(20, new SimpleThreadPoolFactory("FixedThreadPool"));

	public static void submit(final Runnable task) {
		executorService.submit(task);
	}
	
	public static Future<?> submit(final Callable<?> task) {
		return executorService.submit(task);
	}

	private static class SimpleThreadPoolFactory implements ThreadFactory {

	    private final AtomicInteger threadIdx = new AtomicInteger(0);

	    private final String threadNamePrefix;

	    public SimpleThreadPoolFactory(String prefix) {
	        threadNamePrefix = prefix;
	    }

	    @Override
	    public Thread newThread(Runnable r) {
	        Thread thread = new Thread(r);
	        thread.setName(threadNamePrefix + "-thread-" + threadIdx.getAndIncrement());
	        return thread;
	    }
	}

}

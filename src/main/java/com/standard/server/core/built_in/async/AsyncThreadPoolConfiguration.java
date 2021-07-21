package com.standard.server.core.built_in.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步线程池配置类
 */
@Configuration
public class AsyncThreadPoolConfiguration {

    public static final String THREAD_POOL_BEAN_NAME = "async-thread-pool";

    private final int CORE_POOL_SIZE = 4;

    private final int MAX_POOL_SIZE = 4;

    @Bean(AsyncThreadPoolConfiguration.THREAD_POOL_BEAN_NAME)
    public ThreadPoolTaskExecutor initThreadPoll() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("async-thread-");
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(10);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.initialize();
        return executor;
    }

}

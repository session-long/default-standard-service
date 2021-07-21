package com.standard.server.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Slf4j
@Configuration
public class AsyncThreadPoolFactory {

    public static final String ASYNC_THREAD_SERVICE = "async-thread-service";

    @Value("4")
    private int MAX_POOL_SIZE;

    public AsyncThreadPoolFactory() {
        log.info("MAX_POOL_SIZE: {}", MAX_POOL_SIZE);
    }

    @Bean(AsyncThreadPoolFactory.ASYNC_THREAD_SERVICE)
    public Executor initThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(1);
        executor.setKeepAliveSeconds(15);
        executor.setThreadNamePrefix("default-thread-pool-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }

    @Async(AsyncThreadPoolFactory.ASYNC_THREAD_SERVICE)
    public void executeAsync() {
        log.info("start executeAsync");
        try {
            System.out.println("当前运行的线程名称：" + Thread.currentThread().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("end executeAsync");
    }

//    @Scheduled(fixedRate = 2000)
//    public void fixedRate() {
//        System.out.println("fixedRate>>>" + MAX_POOL_SIZE);
//        this.executeAsync();
//    }
}

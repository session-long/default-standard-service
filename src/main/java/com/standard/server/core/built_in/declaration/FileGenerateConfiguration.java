package com.standard.server.core.built_in.declaration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Slf4j
@Configuration
public class FileGenerateConfiguration {

    public final static String FILE_GENERATOR_THREAD_POOL = "file-generator-thread-pool";

    @Bean(FileGenerateConfiguration.FILE_GENERATOR_THREAD_POOL)
    public ExecutorService initThreadPool() {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        return executorService;
    }

}

package com.standard.server.core.built_in.declaration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;

@Component
@ConditionalOnBean(TaskList.class)
public class FileGenerateWorker {

    @Qualifier(FileGenerateConfiguration.FILE_GENERATOR_THREAD_POOL)
    @Autowired
    private ExecutorService workers;

    @Autowired
    private TaskList tasks;

    private int workerSize = 4;

    @PostConstruct
    public void init() {
        for (int i = 0; i < workerSize; i++) {
            workers.execute(new Worker(tasks));
        }
    }

    @Slf4j
    @ConditionalOnBean(FileGenerateWorker.class)
    static class Worker implements Runnable {

        private final TaskList tasks;

        public Worker(TaskList tasks) {
            this.tasks = tasks;
        }

        @Override
        public void run() {
            log.info("{}: 打卡完成，开始上班。", Thread.currentThread().getName());
            try {
                while (true) {
                    TaskList.Task task = tasks.get();
                    Thread.sleep(1000 * 3);
                    // TODO 增加具体的业务处理逻辑
                }
                // 想下班，这是不可能的。
                // log.info("下班了。");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

package com.standard.server.core.built_in.declaration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@ConditionalOnBean(TaskList.class)
public class FileGenerateBoss {

    @Autowired
    private TaskList tasks;

    @PostConstruct
    public void init() {
        new Thread(new Boss(tasks)).start();
    }

    @Slf4j
    @ConditionalOnBean(FileGenerateBoss.class)
    static class Boss implements Runnable {

        private final TaskList tasks;

        public Boss(TaskList tasks) {
            this.tasks = tasks;
        }

        @Override
        public void run() {
            log.info("{}：我是老板，我要去看下任务列表！", Thread.currentThread().getName());
            try {
                tasks.add(new TaskList.Task("1"));
                tasks.add(new TaskList.Task("2"));
                tasks.add(new TaskList.Task("3"));
                tasks.add(new TaskList.Task("4"));
                log.info("现在任务列表中有{}个任务。", tasks.undo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

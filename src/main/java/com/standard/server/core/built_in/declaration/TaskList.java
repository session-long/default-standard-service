package com.standard.server.core.built_in.declaration;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Component
public class TaskList {

    final ReentrantLock lock;

    private final Condition notFull;

    private final Condition notEmpty;

    final Task[] tasks;

    volatile int undo;

    volatile int doing;

    volatile int done;

    final int capacity = 4;

    public TaskList() {
        log.info("任务列表准备中......");
        this.lock = new ReentrantLock();
        this.notFull = lock.newCondition();
        this.notEmpty = lock.newCondition();
        tasks = new Task[capacity];
        undo = doing = done = 0;
        log.info("任务列表已准备");
    }

    private synchronized int getCount() {
        return this.undo + this.doing;
    }

    public void add(Task item) throws InterruptedException {
        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        try {
            while (getCount() == capacity)
                notFull.await();
            tasks[getCount() % capacity] = item;
            undo++;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public Task get() throws InterruptedException {
        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        try {
            log.info("现在的任务数量：{} {} {}", undo, doing, done);
            while (undo == 0) {
                log.info("没有可以领取的任务。");
                notEmpty.await(1000 * 3, TimeUnit.MILLISECONDS);
            }
            Task task = tasks[undo % capacity];
            task.setThreadId(Thread.currentThread().getId());
            log.info("{}, 我领取到了一个任务（{}）", Thread.currentThread().getId(), task.toString());
            undo--;
            doing++;
            return task;
        } finally {
            lock.unlock();
        }

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Task {

        String name;

        int status;

        long threadId;

        public Task(String name) {
            this.name = name;
            status = -1;
            threadId = -1;
        }
    }
}

package com.study.config;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * 定时任务运行的线程池配置
 */
@EnableScheduling
@Configuration
public class ScheduleConfiguration implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNamePrefix("XH-SAD-SCHEDULE-%d").build();
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2, namedThreadFactory);
        taskRegistrar.setScheduler(executor);
    }

}

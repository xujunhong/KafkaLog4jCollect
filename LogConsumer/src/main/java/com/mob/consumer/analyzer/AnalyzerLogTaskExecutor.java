/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.consumer.analyzer;

/**
 * 用于执行分析任务的facade接口
 * 
 * @author zxc Dec 17, 2015 4:39:02 PM
 */
public interface AnalyzerLogTaskExecutor {

    void execute(AnalyzerContext context) throws LogAnalyzeException;
}

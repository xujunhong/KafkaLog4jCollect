/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.consumer.analyzer;

/**
 * 定义一个日志分析逻辑的声明周期
 * 
 * @author zxc Dec 17, 2015 4:35:44 PM
 */
public interface Analyzelet {

    public void begin(AnalyzerContext analyzerContext);

    /**
     * 处理每一行log的逻辑
     * 
     * @param line
     * @param analyzerContext
     */
    public void doLine(String line, AnalyzerContext analyzerContext);

    public void end(AnalyzerContext analyzerContext);

    /**
     * 错误处理逻辑
     * 
     * @param analyzerContext
     */
    public void onError(AnalyzerContext analyzerContext, Throwable t);

    /**
     * 遇到错误是否立即停止
     * 
     * @return
     */
    public boolean onErrorStop();
}

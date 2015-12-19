/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.consumer.analyzer;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * @author zxc Dec 17, 2015 4:40:44 PM
 */
public abstract class BasicAnalyzelet implements Analyzelet {

    private final Logger logger = LogManager.getLogger(this.getClass());

    protected BasicAnalyzelet() {

    }

    @Override
    public void begin(AnalyzerContext analyzerContext) {
        logger.info("begin analyzelet:" + this.getClass().getName());
    }

    @Override
    public void end(AnalyzerContext analyzerContext) {
        logger.info("analyzelet:" + this.getClass().getName() + " finished!");
    }

    @Override
    public void onError(AnalyzerContext analyzerContext, Throwable t) {
        logger.error("Ops!", t);
    }

    @Override
    public boolean onErrorStop() {
        return false;
    }
}

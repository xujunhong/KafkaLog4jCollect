/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.consumer.analyzer;

/**
 * @author zxc Dec 17, 2015 4:39:27 PM
 */
public class LogAnalyzeException extends Exception {

    private static final long serialVersionUID = 8690213612922412148L;

    public LogAnalyzeException(String msg) {
        super(msg);
    }

    public LogAnalyzeException(Throwable t) {
        super(t);
    }

    public LogAnalyzeException(String msg, Throwable t) {
        super(msg, t);
    }
}

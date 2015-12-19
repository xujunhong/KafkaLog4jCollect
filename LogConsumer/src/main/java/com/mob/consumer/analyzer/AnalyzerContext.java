/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.consumer.analyzer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Log分析上下文
 * 
 * @author zxc Dec 17, 2015 4:36:21 PM
 */
public class AnalyzerContext implements Serializable {

    private static final long         serialVersionUID = -6231784544796822932L;

    private final Map<String, Object> contextMap       = new HashMap<String, Object>();

    public AnalyzerContext() {

    }

    public void addParam(String key, Object value) {
        this.contextMap.put(key, value);
    }

    public void addParam(Map<String, ? extends Object> paramMap) {
        this.contextMap.putAll(contextMap);
    }

    @SuppressWarnings("unchecked")
    public <T> T getParam(String key) {
        return (T) contextMap.get(key);
    }

    public void remove(String key) {
        contextMap.remove(key);
    }
}

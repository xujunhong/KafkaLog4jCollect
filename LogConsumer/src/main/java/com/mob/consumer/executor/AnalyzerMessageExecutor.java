/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.consumer.executor;

import com.lamfire.utils.StringUtils;
import com.mob.common.kafka.consumer.MessageExecutor;

/**
 * @author zxc Dec 17, 2015 4:46:20 PM
 */
public class AnalyzerMessageExecutor implements MessageExecutor {

    private static final String conversionPattern = "%d [%-5p] [%t] - [%l] %m%n";

    @Override
    public void execute(String message) {
        if (StringUtils.isEmpty(message)) {
            return;
        }

    }
}

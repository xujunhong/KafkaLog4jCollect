/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.consumer.executor;

import com.lamfire.utils.StringUtils;
import com.mob.common.kafka.consumer.MessageExecutor;

/**
 * 仅输出打印kafka拉取到的消息
 * 
 * @author zxc Dec 17, 2015 12:21:34 PM
 */
public class EchoHelloLog4jMessageExecutor implements MessageExecutor {

    @Override
    public void execute(String message) {
        if (StringUtils.isEmpty(message)) {
            return;
        }
        System.out.println(message);
    }
}

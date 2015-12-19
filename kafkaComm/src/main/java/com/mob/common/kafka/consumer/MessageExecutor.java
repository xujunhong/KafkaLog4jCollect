/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.common.kafka.consumer;

/**
 * @author zxc Dec 17, 2015 4:16:30 PM
 */
public interface MessageExecutor {

    public void execute(String message);
}

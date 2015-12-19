/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.consumer;

import com.mob.common.kafka.consumer.MessageConsumer;
import com.mob.consumer.executor.EchoHelloLog4jMessageExecutor;

/**
 * @author zxc Dec 17, 2015 4:26:34 PM
 */
public class ConsumerBootServer {

    public static void main(String[] args) {
        MessageConsumer hellConsumer = new MessageConsumer("kafkaLog4jTest", 1);
        hellConsumer.setExecutor(new EchoHelloLog4jMessageExecutor());
        hellConsumer.run();
    }
}

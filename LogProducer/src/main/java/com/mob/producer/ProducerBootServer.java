/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.producer;

import com.mob.common.kafka.producer.ThreadUtil;
import com.mob.producer.test.HelloLog4J;

/**
 * 每隔10毫秒往kafka生产一批日志数据
 * 
 * <pre>
 *      kafka topic name
 *      kafkaLog4jTest
 *      
 *      kafka log config
 *      log4j.appender.kafka=kafka.producer.KafkaLog4jAppender
 *      log4j.appender.kafka.topic=kafkaLog4jTest
 *      log4j.appender.kafka.brokerList=192.168.180.154:9092,192.168.180.155:9092,192.168.180.156:9092
 *      log4j.appender.kafka.compressionType=none
 *      log4j.appender.kafka.syncSend=true
 *      log4j.appender.kafka.layout=org.apache.log4j.PatternLayout
 *      log4j.appender.kafka.layout.ConversionPattern=%d [%-5p] [%t] - [%l] %m%n
 * </pre>
 * 
 * @author zxc Dec 17, 2015 4:21:00 PM
 */
public class ProducerBootServer {

    public static void main(String[] args) throws Exception {
        ThreadUtil.scheduleAtFixed(new HelloLog4J(), 0, 10);
    }
}

/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.common.kafka.producer;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lamfire.utils.PropertiesUtils;

/**
 * @author zxc Dec 17, 2015 4:15:59 PM
 */
public class MessageProducer {

    private static final Logger          LOGGER           = LoggerFactory.getLogger(MessageProducer.class);
    private static final String          default_producer = "kafka-producer.properties";

    private static final MessageProducer instance         = new MessageProducer();

    public static MessageProducer getInstance() {
        return instance;
    }

    private Properties               props;
    private ProducerConfig           config;
    private Producer<String, String> producer;

    private MessageProducer() {
        props = PropertiesUtils.load(default_producer, MessageProducer.class);
        config = new ProducerConfig(props);
    }

    public void send(String topicName, String key, String message) {
        try {
            KeyedMessage<String, String> data = new KeyedMessage<String, String>(topicName, key, message);
            getProducer().send(data);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void send(String topicName, String message) {
        try {
            KeyedMessage<String, String> data = new KeyedMessage<String, String>(topicName, message);
            getProducer().send(data);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private synchronized Producer<String, String> getProducer() {
        if (producer == null) {
            producer = new Producer<String, String>(config);
        }
        return producer;
    }

    public synchronized void close() {
        if (producer != null) {
            producer.close();
            producer = null;
        }
    }
}

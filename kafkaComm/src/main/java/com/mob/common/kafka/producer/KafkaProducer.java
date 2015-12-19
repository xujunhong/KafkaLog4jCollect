/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.common.kafka.producer;

import com.lamfire.json.JSON;
import com.lamfire.logger.Logger;
import com.lamfire.utils.RandomUtils;
import com.lamfire.utils.StringUtils;
import com.mob.common.kafka.cons.MobKafkaConstant;

/**
 * 数据采集(发往kafka)
 * 
 * @author zxc Oct 28, 2015 5:51:30 PM
 */
public class KafkaProducer<T> implements MobKafkaConstant {

    private static final Logger LOGGER      = Logger.getLogger(KafkaProducer.class);
    private static final Logger kafkaLogger = Logger.getLogger("KAFKA_MESS");

    /**
     * 异步发送消息到kafka,并日志记录,不会抛出异常
     * 
     * @param topic
     * @param data
     */
    public void produce(final String topic, final T data) {
        _produce(topic, data, true);
    }

    /**
     * 同步发送消息到kafka,并日志记录,不会抛出异常
     * 
     * @param topic
     * @param data
     */
    public void syncProduce(String topic, T data) {
        _produce(topic, data, false);
    }

    private void _produce(final String topic, T data, boolean isSync) {
        if (data == null) {
            return;
        }
        String json = StringUtils.EMPTY;
        try {
            if (data instanceof String) {
                json = (String) data;
            } else if (data instanceof JSON) {
                json = ((JSON) data).toString();
            } else {
                json = JSON.toJSONString(data);
            }
        } catch (Throwable t) {
            LOGGER.error("[KafkaProducer json format error]", t);
        }
        if (StringUtils.isEmpty(json)) {
            return;
        }
        kafkaLogger.info("[" + topic + "] " + json);
        if (StringUtils.isEmpty(topic)) {
            return;
        }
        if (!isSync) {
            final String _json = json;
            ThreadUtil.submitTask(new Runnable() {

                @Override
                public void run() {
                    doProducer(topic, _json);
                }
            });
        }
        doProducer(topic, json);
    }

    private void doProducer(final String topic, final String _json) {
        try {
            MessageProducer producer = MessageProducer.getInstance();
            producer.send(topic, PARTITIONS_MULTIPLE_KEY + RandomUtils.nextInt(100), _json);
        } catch (Throwable t) {
            LOGGER.error("[KafkaProducer send error]", t);
        }
    }
}

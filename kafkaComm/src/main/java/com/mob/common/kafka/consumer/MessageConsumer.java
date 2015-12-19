/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.common.kafka.consumer;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kafka.consumer.*;
import kafka.javaapi.consumer.ConsumerConnector;

import com.lamfire.utils.PropertiesUtils;

/**
 * kafka消费者
 * 
 * @author zxc Dec 17, 2015 4:16:19 PM
 */
public class MessageConsumer implements Runnable {

    private static Properties pro     = PropertiesUtils.load("kafka-consumer.properties", MessageConsumer.class);

    private String            topic;
    private int               streams = 1;

    private ConsumerConfig    config;
    private ConsumerConnector connector;

    private ExecutorService   threadPool;
    private MessageExecutor   executor;

    /**
     * @param topic 主题名称
     * @param streams 消费者线程数
     */
    public MessageConsumer(String topic, int streams) {
        this.topic = topic;
        this.streams = streams;
        this.config = new ConsumerConfig(pro);
    }

    public MessageConsumer(String topic, int streams, String configFile) {
        this.topic = topic;
        this.streams = streams;
        this.config = new ConsumerConfig(PropertiesUtils.load(configFile, MessageConsumer.class));
    }

    private synchronized ConsumerConnector getConsumer() {
        if (connector == null) {
            connector = Consumer.createJavaConsumerConnector(config);
        }
        return connector;
    }

    public synchronized void close() {
        if (connector != null) {
            connector.shutdown();
            threadPool.shutdown();
            connector = null;
            threadPool = null;
        }
    }

    public void run() {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, this.streams);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = getConsumer().createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> partitions = consumerMap.get(topic);

        threadPool = Executors.newFixedThreadPool(this.streams);

        for (final KafkaStream<byte[], byte[]> partition : partitions) {
            threadPool.submit(new ConsumerExecutor(partition, executor));
        }
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getStreams() {
        return streams;
    }

    public void setStreams(int streams) {
        this.streams = streams;
    }

    public MessageExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(MessageExecutor executor) {
        this.executor = executor;
    }

    static class ConsumerExecutor implements Runnable {

        private KafkaStream<byte[], byte[]> stream;
        private MessageExecutor             executor;

        ConsumerExecutor(KafkaStream<byte[], byte[]> stream, MessageExecutor executor) {
            this.stream = stream;
            this.executor = executor;
        }

        @Override
        public void run() {
            ConsumerIterator<byte[], byte[]> iterator = stream.iterator();
            while (iterator.hasNext()) {
                String msg = new String(iterator.next().message());
                executor.execute(msg);
            }
        }
    }
}

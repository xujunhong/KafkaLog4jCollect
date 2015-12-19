/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.producer.test;

import org.apache.log4j.Logger;

/**
 * @author zxc Dec 17, 2015 12:14:02 PM
 */
public class HelloLog4J implements Runnable {

    // 构造记录器,形参是记录器所在的类,表示要在该类做日志
    private static Logger logger = Logger.getLogger(HelloLog4J.class);

    @Override
    public void run() {
        covertMessage("  ^_^" + System.currentTimeMillis() + "^_^!");
    }

    // 记录下各种级别的信息,这些信息放在哪儿,以哪种方式存放,在log4j.properties文件中配置.
    private static void covertMessage(String msg) {
        logger.debug("This is debug message.-----" + msg);
        logger.info("This is a info message.-----" + msg);
        logger.warn("This is a warn message.-----" + msg);
        logger.error("This is a error message.-----" + msg);
    }
}

/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.appender.plug.test;

import java.util.Iterator;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

/**
 * @author zxc Dec 17, 2015 4:53:40 PM
 */
public class CustomAppender extends AppenderSkeleton {

    private LimitTailSizeList ll;

    public CustomAppender(PatternLayout layOut, int size) {
        this.layout = layOut;
        ll = new LimitTailSizeList(size);
    }

    protected void append(LoggingEvent event) {
        String log = this.layout.format(event);
        ll.add(log);
    }

    public String getLatentLog() {
        StringBuffer sb = new StringBuffer(100000);
        for (Iterator<?> iterator = ll.iterator(); iterator.hasNext();) {
            String log = (String) iterator.next();
            sb.append(log);
        }
        return sb.toString();
    }

    public void close() {
        ll.clear();
        ll = null;
        this.closed = true;
    }

    public boolean requiresLayout() {
        return true;
    }
}

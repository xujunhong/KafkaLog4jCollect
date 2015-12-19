/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.appender.plug.test;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * @author zxc Dec 17, 2015 4:54:27 PM
 */
public class LimitTailSizeList extends ArrayList {

    private int limitSize;

    public LimitTailSizeList(int limitSize) {
        this.limitSize = limitSize;
    }

    public boolean add(Object o) {
        boolean add = super.add(o);
        if (size() > limitSize) {
            removeRange(0, size() - limitSize);
        }
        return add;
    }

    private void initAppender(int maxTailLine) {
        CustomAppender fba = new CustomAppender(
                                                new PatternLayout(
                                                                  "%d [%X{requestURIWithQueryString}] %-5p -[%t] %m  [%c{1}:%M %L] %n"),
                                                maxTailLine);
        Logger.getRootLogger().removeAppender("UI_APPENDER");
        fba.setName("UI_APPENDER");
        fba.setThreshold(org.apache.log4j.Level.DEBUG);
        Logger.getRootLogger().addAppender(fba);
    }
}

/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.appender.plug.layout;

import java.net.UnknownHostException;

/**
 * @author zxc Dec 17, 2015 5:00:52 PM
 */
public class HostData {

    public String hostName;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public HostData() {
        try {
            this.hostName = java.net.InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            setHostName("unknown-host");
        }
    }
}

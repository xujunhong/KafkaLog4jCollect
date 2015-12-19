/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.appender.plug.layout;

import java.util.*;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.*;

import com.lamfire.json.JSON;
import com.lamfire.utils.StringUtils;

/**
 * JSON格式的日志
 * 
 * <pre>
 *      log4j.rootCategory=WARN, RollingLog
 *      log4j.appender.RollingLog=org.apache.log4j.DailyRollingFileAppender
 *      log4j.appender.RollingLog.Threshold=TRACE
 *      log4j.appender.RollingLog.File=api.log
 *      log4j.appender.RollingLog.DatePattern=.yyyy-MM-dd
 *      log4j.appender.RollingLog.layout=com.mob.appender.plug.layout.JSONEventLayout
 *      #log4j.appender.RollingLog.layout.customUserFields=foo:xxx,baz:zzz
 * </pre>
 * 
 * @author zxc Dec 17, 2015 4:59:17 PM
 */
public class JSONEventLayout extends Layout {

    private boolean                    locationInfo                              = false;
    private String                     customUserFields;

    private boolean                    ignoreThrowable                           = false;

    @SuppressWarnings("unused")
    private boolean                    activeIgnoreThrowable                     = ignoreThrowable;
    private String                     hostname                                  = new HostData().getHostName();
    private String                     threadName;
    private long                       timestamp;
    private String                     ndc;
    private Map<?, ?>                  mdc;
    private LocationInfo               info;
    private HashMap<String, Object>    exceptionInformation;
    private static Integer             version                                   = 1;

    private JSON                       logEvent;

    public static final TimeZone       UTC                                       = TimeZone.getTimeZone("UTC");
    public static final FastDateFormat ISO_DATETIME_TIME_ZONE_FORMAT_WITH_MILLIS = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                                                                                                              UTC);
    public static final String         ADDITIONAL_DATA_PROPERTY                  = "net.logstash.log4j.JSONEventLayout.UserFields";

    public static String dateFormat(long timestamp) {
        return ISO_DATETIME_TIME_ZONE_FORMAT_WITH_MILLIS.format(timestamp);
    }

    /**
     * For backwards compatibility, the default is to generate location information in the log messages.
     */
    public JSONEventLayout() {
        this(true);
    }

    /**
     * Creates a layout that optionally inserts location information into log messages.
     *
     * @param locationInfo whether or not to include location information in the log messages.
     */
    public JSONEventLayout(boolean locationInfo) {
        this.locationInfo = locationInfo;
    }

    public String format(LoggingEvent loggingEvent) {
        threadName = loggingEvent.getThreadName();
        timestamp = loggingEvent.getTimeStamp();
        exceptionInformation = new HashMap<String, Object>();
        mdc = loggingEvent.getProperties();
        ndc = loggingEvent.getNDC();

        logEvent = new JSON();
        String whoami = this.getClass().getSimpleName();

        logEvent.put("@version", version);
        logEvent.put("@timestamp", dateFormat(timestamp));

        if (getUserFields() != null) {
            String userFlds = getUserFields();
            LogLog.debug("[" + whoami + "] Got user data from log4j property: " + userFlds);
            addUserFields(userFlds);
        }

        if (System.getProperty(ADDITIONAL_DATA_PROPERTY) != null) {
            if (getUserFields() != null) {
                LogLog.warn("["
                            + whoami
                            + "] Loading UserFields from command-line. This will override any UserFields set in the log4j configuration file");
            }
            String userFieldsProperty = System.getProperty(ADDITIONAL_DATA_PROPERTY);
            LogLog.debug("[" + whoami + "] Got user data from system property: " + userFieldsProperty);
            addUserFields(userFieldsProperty);
        }

        logEvent.put("source_host", hostname);
        logEvent.put("message", loggingEvent.getRenderedMessage());

        if (loggingEvent.getThrowableInformation() != null) {
            final ThrowableInformation throwableInformation = loggingEvent.getThrowableInformation();
            if (throwableInformation.getThrowable().getClass().getCanonicalName() != null) {
                exceptionInformation.put("exception_class",
                                         throwableInformation.getThrowable().getClass().getCanonicalName());
            }
            if (throwableInformation.getThrowable().getMessage() != null) {
                exceptionInformation.put("exception_message", throwableInformation.getThrowable().getMessage());
            }
            if (throwableInformation.getThrowableStrRep() != null) {
                String stackTrace = StringUtils.join(throwableInformation.getThrowableStrRep(), "\n");
                exceptionInformation.put("stacktrace", stackTrace);
            }
            addEventData("exception", exceptionInformation);
        }

        if (locationInfo) {
            info = loggingEvent.getLocationInformation();
            addEventData("file", info.getFileName());
            addEventData("line_number", info.getLineNumber());
            addEventData("class", info.getClassName());
            addEventData("method", info.getMethodName());
        }

        addEventData("logger_name", loggingEvent.getLoggerName());
        addEventData("mdc", mdc);
        addEventData("ndc", ndc);
        addEventData("level", loggingEvent.getLevel().toString());
        addEventData("thread_name", threadName);

        return logEvent.toString() + "\n";
    }

    public boolean ignoresThrowable() {
        return ignoreThrowable;
    }

    /**
     * Query whether log messages include location information.
     *
     * @return true if location information is included in log messages, false otherwise.
     */
    public boolean getLocationInfo() {
        return locationInfo;
    }

    /**
     * Set whether log messages should include location information.
     *
     * @param locationInfo true if location information should be included, false otherwise.
     */
    public void setLocationInfo(boolean locationInfo) {
        this.locationInfo = locationInfo;
    }

    public String getUserFields() {
        return customUserFields;
    }

    public void setUserFields(String userFields) {
        this.customUserFields = userFields;
    }

    public void activateOptions() {
        activeIgnoreThrowable = ignoreThrowable;
    }

    private void addUserFields(String data) {
        if (null != data) {
            String[] pairs = data.split(",");
            for (String pair : pairs) {
                String[] userField = pair.split(":", 2);
                if (userField[0] != null) {
                    String key = userField[0];
                    String val = userField[1];
                    addEventData(key, val);
                }
            }
        }
    }

    private void addEventData(String keyname, Object keyval) {
        if (null != keyval) {
            logEvent.put(keyname, keyval);
        }
    }
}

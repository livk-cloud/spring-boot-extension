package com.livk.autoconfigure.mybatis.monitor.event;

import org.springframework.context.ApplicationEvent;

/**
 * The type Monitor sql time out event.
 *
 * @author livk
 */
public class MonitorSQLTimeOutEvent extends ApplicationEvent {
    /**
     * Instantiates a new Monitor sql time out event.
     *
     * @param source the source
     */
    public MonitorSQLTimeOutEvent(MonitorSQLInfo source) {
        super(source);
    }

    /**
     * Info monitor sql info.
     *
     * @return the monitor sql info
     */
    public MonitorSQLInfo info() {
        return (MonitorSQLInfo) source;
    }
}

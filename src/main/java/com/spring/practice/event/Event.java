package com.spring.practice.event;

public abstract class Event<T> {

    /**
     * 自身资源
     */
    protected T source;

    private final long timestamp;

    public Event(T source) {
        this.source = source;
        this.timestamp = System.currentTimeMillis();
    }

    public T getSource() {
        return source;
    }

    public void setSource(T source) {
        this.source = source;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

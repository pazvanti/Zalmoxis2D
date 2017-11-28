package com.zalmoxis2d.event.events;

import com.zalmoxis2d.event.EventDispatcher;

public class Event {
    public static final String ENTER_FRAME = "enterFrame";
    private String type;
    private EventDispatcher currentTarget;

    public Event(String type) {
        this.type = type;
    }

    public void setCurrentTarget(EventDispatcher currentTarget) {
        this.currentTarget = currentTarget;
    }

    public EventDispatcher getCurrentTarget() {
        return this.currentTarget;
    }

    public String getType() {
        return this.type;
    }
}

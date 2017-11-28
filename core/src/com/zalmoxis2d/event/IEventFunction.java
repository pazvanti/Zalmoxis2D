package com.zalmoxis2d.event;

import com.zalmoxis2d.event.events.Event;

public interface IEventFunction<T extends Event> {
    public void dispatch(T event);
}

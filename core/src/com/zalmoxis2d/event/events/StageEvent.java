package com.zalmoxis2d.event.events;

import com.zalmoxis2d.display.Stage;

public class StageEvent extends Event {
    public static final String SHOW = "show";
    public static final String RESIZE = "resize";
    public static final String PAUSE = "pause";
    public static final String RESUME = "resume";
    public static final String HIDE = "hide";

    private Stage stage = null;
    public StageEvent(Stage stage, String type) {
        super(type);
        this.stage = stage;
    }

    public Stage getStage() {
        return this.stage;
    }
}

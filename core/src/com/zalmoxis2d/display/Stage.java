package com.zalmoxis2d.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.zalmoxis2d.event.EventDispatcher;
import com.zalmoxis2d.event.EventHandler;
import com.zalmoxis2d.event.events.KeyEvent;
import com.zalmoxis2d.event.events.StageEvent;
import com.zalmoxis2d.event.events.TouchEvent;

import java.util.Set;

public class Stage implements Screen, InputProcessor {
    private SpriteBatch spriteBatch;
    private DisplayObject mainDisplayObject;

    private static final Stage INSTANCE = new Stage();

    private Stage() {
        // Private constructor
    }

    public void init() {
        this.mainDisplayObject = new DisplayObject();
        this.spriteBatch = new SpriteBatch();
    }

    public static Stage getInstance() {
        return INSTANCE;
    }

    public void addChild(DisplayObject displayObject) {
        this.mainDisplayObject.addChild(displayObject);
    }

    public void removeChild(DisplayObject displayObject) {
        this.mainDisplayObject.removeChild(displayObject, true);
    }

    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        mainDisplayObject.render(spriteBatch);
        spriteBatch.end();
    }


    @Override
    public boolean keyDown(int keycode) {
        return keyEvent(keycode, KeyEvent.KEY_DOWN);
    }

    @Override
    public boolean keyUp(int keycode) {
        return keyEvent(keycode, KeyEvent.KEY_UP);
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 screenTouchPoints = new Vector2(screenX, screenY);
        screenTouchPoints = screenCoordsToStageCoords(screenTouchPoints);
        return touchEvent((int)screenTouchPoints.x, (int)screenTouchPoints.y, pointer, button, TouchEvent.TOUCH_DOWN);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector2 screenTouchPoints = new Vector2(screenX, screenY);
        screenTouchPoints = screenCoordsToStageCoords(screenTouchPoints);
        return touchEvent((int)screenTouchPoints.x, (int)screenTouchPoints.y, pointer, button, TouchEvent.TOUCH_UP);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void show() {
        triggerStageEvent(StageEvent.SHOW);
    }

    @Override
    public void render(float delta) {
    }

    @Override
    public void resize(int width, int height) {
        triggerStageEvent(StageEvent.RESIZE);
    }

    @Override
    public void pause() {
        triggerStageEvent(StageEvent.PAUSE);
    }

    @Override
    public void resume() {
        triggerStageEvent(StageEvent.RESUME);
    }

    @Override
    public void hide() {
        triggerStageEvent(StageEvent.HIDE);
    }

    @Override
    public void dispose() {
        this.mainDisplayObject.dispose();
    }

    private void triggerStageEvent(String eventType) {
        Set<EventDispatcher> itemsWithEvent = getItemsWithEvent(eventType);
        if (itemsWithEvent == null || itemsWithEvent.isEmpty()) return;

        for(EventDispatcher eventDispatcher:itemsWithEvent) {
            eventDispatcher.dispatchEvents(eventType, new StageEvent(this, eventType));
        }
    }

    private boolean keyEvent(int keycode, String type) {
        Set<EventDispatcher> itemsWithEvent = getItemsWithEvent(type);
        if (itemsWithEvent == null || itemsWithEvent.isEmpty()) return false;

        for(EventDispatcher eventDispatcher:itemsWithEvent) {
            eventDispatcher.dispatchEvents(type, new KeyEvent(keycode, type));
        }
        return true;
    }

    private boolean touchEvent(int screenX, int screenY, int pointer, int button, String type) {
        Set<EventDispatcher> itemsWithEvent = getItemsWithEvent(type);
        if (itemsWithEvent == null || itemsWithEvent.isEmpty()) return false;

        EventDispatcher eventDispatcherTriggered = null;
        for(EventDispatcher eventDispatcher:itemsWithEvent) {
            if (eventDispatcher instanceof DisplayObject) {
                DisplayObject displayObject = (DisplayObject)eventDispatcher;
                if (displayObject.touched(new Vector2(screenX, screenY))) {
                    eventDispatcherTriggered = eventDispatcher;
                }
            }
        }

        if (eventDispatcherTriggered != null) {
            eventDispatcherTriggered.dispatchEvents(type, new TouchEvent(button, screenX, screenY, type));
            return true;
        }

        return false;
    }

    private Set<EventDispatcher> getItemsWithEvent(String eventType) {
        EventHandler eventHandler = EventHandler.getInstance();
        Set<EventDispatcher> itemsWithEvent = eventHandler.getItemsTriggered(eventType);
        if (itemsWithEvent == null || itemsWithEvent.isEmpty()) return null;
        return itemsWithEvent;
    }

    private Vector2 screenCoordsToStageCoords(Vector2 screenCords) {
        int height = Gdx.graphics.getHeight();
        screenCords.y = height - screenCords.y;
        return screenCords;
    }
}

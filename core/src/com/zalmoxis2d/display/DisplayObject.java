package com.zalmoxis2d.display;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.zalmoxis2d.event.EventDispatcher;
import java.util.ArrayList;
import java.util.List;

public class DisplayObject extends EventDispatcher {
    protected Vector2 globalCoordinates = new Vector2(0, 0);
    protected BoundingBox boundingBox = null;

    private Vector2 coordinates = new Vector2(0, 0);
    private DisplayObject parent = null;
    private List<DisplayObject> children = new ArrayList<>();
    private Sprite sprite = null;
    private float alpha = 1;
    private float rotation;
    private Vector2 rotationOrigin = new Vector2(0, 0);

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void addChild(DisplayObject child) {
        this.children.add(0, child);
        child.parent = this;
        child.calculateGlobalCoordinates();
        this.boundingBox = null;
    }

    public boolean removeChild(DisplayObject child) {
        return removeChild(child, true);
    }

    public boolean removeChild(DisplayObject child, boolean dispose) {
        if (child == null) return false;
        boolean removed = this.children.remove(child);
        if (dispose && removed) {
            child.dispose();
        }
        this.boundingBox = null;
        return removed;
    }

    public void setX(float x) {
        this.coordinates.x = x;
        calculateGlobalCoordinates();
    }

    public float getX() {
        return this.coordinates.x;
    }

    public void setY(float y) {
        this.coordinates.y = y;
        calculateGlobalCoordinates();
    }

    public float getY() {
        return this.coordinates.y;
    }

    public float getRotation() {
        return this.rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
        if (this.sprite != null) {
            this.sprite.setRotation(rotation);
        }

        for (DisplayObject child:children) {
            child.setRotation(rotation);
        }
    }

    public Vector2 getRotationOrigin() {
        return rotationOrigin;
    }

    public void setRotationOrigin(Vector2 rotationOrigin) {
        this.rotation = rotation;
    }

    public float getWidth() {
        if (boundingBox == null) {
            calculateBoundingBox();
        }
        return this.boundingBox.getWidth();
    }

    public float getHeight() {
        if (boundingBox == null) {
            calculateBoundingBox();
        }
        return boundingBox.getHeight();
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        this.sprite.setAlpha(alpha);
    }

    public float getAlpha() {
        return this.alpha;
    }

    public void bringToFront() {
        this.parent.children.remove(this);
        this.parent.children.add(this.parent.children.size(), this);
    }

    public void sendToBack() {
        this.parent.removeChild(this, false);
        this.parent.addChild(this);
    }

    /**
     * Clears the entire display object and all it's children
     */
    public void dispose() {
        if (this.sprite != null) {
            this.sprite = null;
        }

        for (DisplayObject child:this.children) {
            child.dispose();
        }

        this.children.clear();
    }

    protected void render(SpriteBatch spriteBatch) {
        if (this.sprite != null) {
            sprite.draw(spriteBatch);
        }

        for (DisplayObject child:this.children) {
            child.render(spriteBatch);
        }
    }

    protected void calculateGlobalCoordinates() {
        if (this.parent == null) return;

        this.globalCoordinates.x = this.coordinates.x + this.parent.globalCoordinates.x;
        this.globalCoordinates.y = this.coordinates.y + this.parent.globalCoordinates.y;

        if (this.sprite != null) {
            this.sprite.setX(this.globalCoordinates.x);
            this.sprite.setY(this.globalCoordinates.y);
        }

        for (DisplayObject child:this.children) {
            child.calculateGlobalCoordinates();
        }
    }

    public BoundingBox getBounds() {
        if (boundingBox == null) {
            calculateBoundingBox();
        }
        return this.boundingBox;
    }

    protected void calculateBoundingBox() {
        this.boundingBox = new BoundingBox();
        if (this.sprite != null) {
            Rectangle rectangle = this.sprite.getBoundingRectangle();
            BoundingBox spriteBB = new BoundingBox(new Vector3(rectangle.x, rectangle.y, 0), new Vector3(rectangle.width, rectangle.height, 0));
            this.boundingBox.ext(spriteBB);
        }

        for (DisplayObject child:this.children) {
            this.boundingBox.ext(child.getBounds());
        }
    }
}

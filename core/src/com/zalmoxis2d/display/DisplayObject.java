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
    protected Vector2 globalRotationOrigin = new Vector2(0, 0);
    protected Vector2 rotationOrigin = new Vector2(0, 0);
    protected BoundingBox boundingBox = null;
    protected Sprite sprite = null;
    protected float alpha = 1;
    protected float rotation = 0;
    protected float globalRotation = 0;
    protected boolean recalculationsNeeded = false;

    private Vector2 coordinates = new Vector2(0, 0);
    private DisplayObject parent = null;
    private List<DisplayObject> children = new ArrayList<>();

    public String name;
    public String toString() {
        return this.name;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        recalculationsNeeded = true;
    }

    public void addChild(DisplayObject child) {
        this.children.add(0, child);
        child.parent = this;
        recalculationsNeeded = true;
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
        recalculationsNeeded = true;
    }

    public float getX() {
        return this.coordinates.x;
    }

    public void setY(float y) {
        this.coordinates.y = y;
        recalculationsNeeded = true;
    }

    public float getY() {
        return this.coordinates.y;
    }

    public float getRotation() {
        return this.rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
        recalculationsNeeded = true;
    }

    public Vector2 getRotationOrigin() {
        return rotationOrigin;
    }

    public void setRotationOrigin(Vector2 rotationOrigin) {
        this.rotationOrigin = rotationOrigin;
        recalculationsNeeded = true;
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
        if (recalculationsNeeded) {
            calculateAndApplyGlobals();
        }
        if (this.sprite != null) {
            sprite.draw(spriteBatch);
        }

        for (DisplayObject child:this.children) {
            child.render(spriteBatch);
        }
    }

    private void calculateAndApplyGlobals() {
        if (this.parent == null) {
            this.globalCoordinates.x = this.coordinates.x;
            this.globalCoordinates.y = this.coordinates.y;
            this.globalRotation = this.rotation;
            this.globalRotationOrigin = this.rotationOrigin;
        } else {
            this.globalCoordinates.x = this.coordinates.x + this.parent.globalCoordinates.x;
            this.globalCoordinates.y = this.coordinates.y + this.parent.globalCoordinates.y;
            this.globalRotation = this.rotation + this.parent.globalRotation;
            this.globalRotationOrigin.x = this.rotationOrigin.x + this.parent.globalRotationOrigin.x - this.coordinates.x;
            this.globalRotationOrigin.y = this.rotationOrigin.y + this.parent.globalRotationOrigin.y - this.coordinates.y;
        }

        if (this.sprite != null) {
            this.sprite.setX(this.globalCoordinates.x);
            this.sprite.setY(this.globalCoordinates.y);
            //this.sprite.setOrigin(this.rotationOrigin.x, this.rotationOrigin.y);
            this.sprite.setRotation(this.rotation);
        }

        for (DisplayObject child:children) {
            child.calculateAndApplyGlobals();
        }

        this.recalculationsNeeded = false;
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

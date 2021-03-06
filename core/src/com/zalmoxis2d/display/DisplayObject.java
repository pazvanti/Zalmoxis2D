package com.zalmoxis2d.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.zalmoxis2d.event.EventDispatcher;

import java.util.ArrayList;
import java.util.List;

public class DisplayObject extends EventDispatcher {
    protected Vector2 globalCoordinates = new Vector2(0, 0);
    protected Vector2 globalRotationOrigin = new Vector2(0, 0);
    protected Vector2 rotationOrigin = new Vector2(0, 0);
    protected Rectangle boundingBox = null;
    protected Sprite sprite = null;
    protected float alpha = 1;
    protected float rotation = 0;
    protected float globalRotation = 0;
    protected boolean recalculationsNeeded = false;

    private Vector2 coordinates = new Vector2(0, 0);
    private DisplayObject parent = null;
    private List<DisplayObject> children = new ArrayList<>();
    private SpriteBatch spriteBatch = new SpriteBatch();
    private FrameBuffer fbo;
    private boolean fboValid = false;

    public String name;
    public String toString() {
        return this.name;
    }

    public DisplayObject() {
        fbo = FrameBuffer.createFrameBuffer(Pixmap.Format.RGBA8888, 300, 150, false);
        fbo.getColorBufferTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
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

    public void setWidth(int width) {
        if (this.sprite != null) {
            this.sprite.setSize(width, this.sprite.getHeight());
        }
    }

    public float getHeight() {
        if (boundingBox == null) {
            calculateBoundingBox();
        }
        return boundingBox.getHeight();
    }

    public void setHeight(int height) {

        if (this.sprite != null) {
            this.sprite.setSize(this.sprite.getWidth(), height);
        }
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
        if (!fboValid) {
            fbo.begin();
            Gdx.gl.glClearColor(1, 1, 1, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            /*if (recalculationsNeeded) {
                This may not be needed if we render each child in the sprite batch of it's parent and do the transformations properly
                calculateAndApplyGlobals();
            }*/

            this.spriteBatch.begin();
            if (this.sprite != null) {
                this.spriteBatch.draw(sprite.getTexture(), 0, 0);
            }

            for (DisplayObject child : this.children) {
                child.render(this.spriteBatch);
            }
            this.spriteBatch.end();
            fbo.end();
            fboValid = true;
        }
        spriteBatch.draw(fbo.getColorBufferTexture(), this.getX(), this.getY());
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

    public Rectangle getBounds() {
        if (boundingBox == null) {
            calculateBoundingBox();
        }
        return this.boundingBox;
    }

    public boolean touched(Vector2 touchPoint) {
        return touched(touchPoint, true);
    }

    public boolean touched(Vector2 touchPoint, boolean includeBlankArea) {
        if (includeBlankArea) {
            Rectangle boundingRectangle = this.getBounds();
            Rectangle rectangle = new Rectangle(this.globalCoordinates.x,
                    this.globalCoordinates.y,
                    boundingRectangle.getWidth(),
                    boundingRectangle.getHeight());
            return rectangle.contains(touchPoint);
        } else {
            boolean touched = false;
            if (this.sprite != null) {
                touched = this.sprite.getBoundingRectangle().contains(touchPoint);
            }
            if (!touched) {
                for (DisplayObject child:this.children) {
                    if (touched) break;
                    touched = child.touched(touchPoint, includeBlankArea);
                }
            }
            return touched;
        }
    }

    protected void calculateBoundingBox() {
        this.boundingBox = new Rectangle();
        if (this.sprite != null) {
            Rectangle rectangle = this.sprite.getBoundingRectangle();
            this.boundingBox.merge(rectangle);
        }

        for (DisplayObject child:this.children) {
            this.boundingBox.merge(child.getBounds());
        }
    }
}

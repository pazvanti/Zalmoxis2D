package com.zalmoxis2d.text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.zalmoxis2d.display.DisplayObject;

public class TextField extends DisplayObject {
    private String text;
    private Color color;
    private float scale = 1;
    private BitmapFont font = new BitmapFont();

    public TextField(String text) {
        super();
        this.text = text;
        this.color = Color.WHITE;
        this.font.setColor(color);
        this.font.getData().setScale(this.scale);
    }

    public TextField(String text, Color color) {
        super();
        this.text = text;
        this.color = color;
        this.font.setColor(this.color);
        this.font.getData().setScale(this.scale);
    }

    public TextField(String text, Color color, BitmapFont font) {
        super();
        this.text = text;
        this.color = color;
        this.font = font;
        this.font.setColor(this.color);
        this.font.getData().setScale(this.scale);
    }

    public TextField(String text, Color color, BitmapFont font, float scale) {
        super();
        this.text = text;
        this.color = color;
        this.font = font;
        this.scale = scale;
        this.font.setColor(this.color);
        this.font.getData().setScale(this.scale);
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
        this.font.setColor(this.color);
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setScale(float scale) {
        this.scale = scale;
        this.font.getData().setScale(scale);
    }

    public float getScale() {
        return this.scale;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        BoundingBox boundingBox = this.getBounds();
        font.draw(spriteBatch, this.text, this.globalCoordinates.x, this.globalCoordinates.y + boundingBox.getHeight());
    }

    @Override
    public void addChild(DisplayObject child) {
        throw new UnsupportedOperationException("Can't add a child to a TextField");
    }

    @Override
    public boolean removeChild(DisplayObject child) {
        throw new UnsupportedOperationException("Can't remove a child from a TextField");
    }

    @Override
    public boolean removeChild(DisplayObject child, boolean dispose) {
        throw new UnsupportedOperationException("Can't remove a child from a TextField");
    }

    @Override
    protected void calculateBoundingBox() {
        this.boundingBox = new BoundingBox();
        GlyphLayout layout = new GlyphLayout(font, text);
        this.boundingBox.ext(layout.width, layout.height, 0);
    }
}

package com.zalmoxis2d.display;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Bitmap extends DisplayObject {
    public Bitmap(String imagePath) {
        Texture texture = new Texture(imagePath);
        this.sprite = new Sprite(texture);
    }

    public Bitmap(Texture texture) {
        this.sprite = new Sprite(texture);
    }

    public Bitmap(Sprite sprite) {
        this.sprite = sprite;
    }

    @Override
    public void addChild(DisplayObject child) {
        throw new UnsupportedOperationException("Can't add a child to a Bitmap object");
    }

    @Override
    public boolean removeChild(DisplayObject child) {
        throw new UnsupportedOperationException("Can't remove child from a Bitmap object");
    }

    @Override
    public boolean removeChild(DisplayObject child, boolean dispose) {
        throw new UnsupportedOperationException("Can't remove child from a Bitmap object");
    }
}

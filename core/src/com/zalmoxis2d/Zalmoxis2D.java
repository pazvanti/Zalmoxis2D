package com.zalmoxis2d;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.zalmoxis2d.display.DisplayObject;
import com.zalmoxis2d.display.Stage;
import com.zalmoxis2d.text.TextField;

public class Zalmoxis2D extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Stage stage;
	DisplayObject displayObject;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		batch.enableBlending();
		img = new Texture("badlogic.jpg");
		this.stage = Stage.getInstance();
		displayObject = new DisplayObject();
		displayObject.setSprite(new Sprite(img));

		this.stage.init();

		DisplayObject test = new DisplayObject();
		test.setSprite(new Sprite(img));
		test.setX(400);
		test.setY(100);
		test.setAlpha(0.1f);

		displayObject.addChild(test);
		displayObject.setX(150);

		stage.addChild(displayObject);

		TextField textField = new TextField("Test Text");
		stage.addChild(textField);

		System.out.println("Text dimensions: " + textField.getWidth() + " -- " + textField.getHeight());
		System.out.println("DO dimensions: " + displayObject.getWidth() + " -- " + displayObject.getHeight());
	}

	@Override
	public void render () {
		stage.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}

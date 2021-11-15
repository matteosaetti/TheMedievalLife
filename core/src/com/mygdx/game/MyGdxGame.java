package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.w3c.dom.Text;

public class MyGdxGame extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public Texture img;
	public FitViewport viewport;
	@Override
	public void create () {

		batch = new SpriteBatch();
		font = new BitmapFont();
		img= new Texture("sfondo1.jpg");
		viewport = new FitViewport(1920,1080);
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		viewport.apply(true);
		batch.begin();
		batch.draw(img,0,0);
		batch.end();
	}
	@Override
	public void resize(final int width, final int height){
		viewport.update(width,height);

	}
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
		img.dispose();
	}


}


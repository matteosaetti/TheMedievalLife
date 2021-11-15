package com.mygdx.game;

import com.badlogic.gdx.Screen;


import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
public class GameScreen implements Screen {
    final MyGdxGame game;
    Rectangle player;
    Texture playerImage;
    OrthographicCamera camera;
    public GameScreen(final MyGdxGame game) {
        this.game = game;

        playerImage = new Texture(Gdx.files.internal("bucket.png"));
        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 800);
        camera.position.x=640;
        camera.position.y=400;
        camera.update();
        //create player
        player = new Rectangle();
        player.width = 32;
        player.height = 32;


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // clear the screen with a dark blue color. The
        // arguments to clear are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        ScreenUtils.clear(0, 0, 0.2f, 1);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);

        // begin a new batch and draw the bucket and
        // all drops


        // process user input
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

        }

        game.batch.begin();

        game.batch.draw(playerImage, player.x, player.y, player.width, player.height);

        game.batch.end();

        if (Gdx.input.isKeyPressed(Keys.A))
            player.x -= 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Keys.D))
            player.x += 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Keys.W))
            player.y += 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Keys.S))
            player.y -= 200 * Gdx.graphics.getDeltaTime();

        if (player.x < 0)
            player.x = 0;
        if (player.x > 800 - 64)
            player.x = 800 - 64;
        if (player.y > 480 - 64)
            player.y = 480 - 64;
        if (player.y < 0)
            player.y = 0;


    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        playerImage.dispose();
    }
}

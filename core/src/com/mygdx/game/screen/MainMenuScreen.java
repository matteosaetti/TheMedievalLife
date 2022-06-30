package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.audio.AudioType;
import com.mygdx.game.input.GameKeys;
import com.mygdx.game.input.InputListener;
import com.mygdx.game.input.InputManager;
import com.mygdx.game.utils.MovingTexture;

public class MainMenuScreen extends AbstractScreen implements InputListener {
    private final float BG_WIDTH = 22;
    private final float BG_HEIGHT = 12;
    private final float PLAY_BUTT_WIDTH = 5;
    private final float PLAY_BUTT_HEIGHT = 1.3F;
    private final float SETT_BUTT_WIDTH = 5.5F;
    private final float SETT_BUTT_HEIGHT = 0.75F;

    private AssetManager assetManager;
    private MovingTexture BG;
    private Texture texture_playbutton_active;
    private Texture texture_playbutton_inactive;
    private Texture texture_settingsbutton_active;
    private Texture texture_settingsbutton_inactive;

    private Vector3 coordsPointed;

    private final OrthographicCamera camera;

    public MainMenuScreen(final MyGdxGame context) {
        super(context);
        //Others
        this.camera = context.getGameCamera();
        coordsPointed = new Vector3();
        assetManager = context.getAssetManager();
        //texture
        BG = new MovingTexture(Gdx.files.internal("loading/loading_photo.jpg"), -1f, -1f, -0.5f, -0.5f);
        BG.setWidth(32);
        BG.setHeight(18);
        texture_playbutton_active = new Texture(Gdx.files.internal("buttons/playbutton_red.png"));
        texture_playbutton_inactive = new Texture(Gdx.files.internal("buttons/playbutton_lightgrey.png"));
        texture_settingsbutton_active = new Texture(Gdx.files.internal("buttons/settingsbutton_red.png"));
        texture_settingsbutton_inactive = new Texture(Gdx.files.internal("buttons/settingsbutton_lightgrey.png"));
    }

    @Override
    protected Table getScreenUI(Skin skin) {
        return null;
    }


    @Override
    public void show() {
        ScreenUtils.clear(0, 0, 0, 1);
        batch.setProjectionMatrix(camera.combined);
        audioManager.playAudio(AudioType.THEME1);
        BG.resume();
        inputManager.addInputListener(this);
    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        //get coords of mouse in coordsPointed
        camera.update();
        coordsPointed.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(coordsPointed);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        //draw moving background
        BG.updatePosition(16, 9, 1);
        batch.draw(BG, BG.getX(), BG.getY(), 20*2, 12*2);

        //draw texture play button
        batch.draw(texture_playbutton_inactive, 8-PLAY_BUTT_WIDTH/2, 1, PLAY_BUTT_WIDTH, PLAY_BUTT_HEIGHT);
        if(coordsPointed.x > 8-PLAY_BUTT_WIDTH/2 && coordsPointed.x < 8+PLAY_BUTT_WIDTH/2 &&
                coordsPointed.y < 1 + PLAY_BUTT_HEIGHT && coordsPointed.y > 1){
            batch.draw(texture_playbutton_active, 8-PLAY_BUTT_WIDTH/2, 1, PLAY_BUTT_WIDTH, PLAY_BUTT_HEIGHT);
            if(Gdx.input.justTouched()){
                BG.stop();
                audioManager.playAudio(AudioType.CLICK2_HEAVY);
                audioManager.playAudio(AudioType.CLICK3_SUCCESS);

                context.setScreen(ScreenType.GAME);
            }
        }

        //draw settings button
        batch.draw(texture_settingsbutton_inactive, 8-SETT_BUTT_WIDTH/2, 2.5f, SETT_BUTT_WIDTH, SETT_BUTT_HEIGHT);
        if(coordsPointed.x > 8-SETT_BUTT_WIDTH/2 && coordsPointed.x < 8 + SETT_BUTT_WIDTH/2 &&
                coordsPointed.y < 2.5f + SETT_BUTT_HEIGHT && coordsPointed.y > 2.5f) {
            batch.draw(texture_settingsbutton_active, 8 - SETT_BUTT_WIDTH / 2, 2.5f, SETT_BUTT_WIDTH, SETT_BUTT_HEIGHT);
            if (Gdx.input.justTouched()) {
                audioManager.playAudio(AudioType.CLICK2_HEAVY);
                context.setScreen(ScreenType.SETTINGS);
            }
        }
        batch.end();
    }


    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        inputManager.removeInputListener(this);
    }

    /**
     * Before a pause() or exit(), dispose no-more used Objects that Java Garbage Collector doesn't secure
     */
    @Override
    public void dispose() {
        //Texture
        BG.dispose();
        texture_playbutton_inactive.dispose();
        texture_playbutton_active.dispose();
        texture_settingsbutton_active.dispose();
        texture_settingsbutton_inactive.dispose();
    }

    /**
     * Implements method of interface InputListener
     *
     * @see InputListener
     */
    @Override
    public void keyPressed(InputManager manager, GameKeys key) {
        switch (key){
            case NEXT: //set screen 'LOADING'
                BG.stop();
                audioManager.playAudio(AudioType.CLICK3_SUCCESS);
                context.setScreen(ScreenType.GAME);
            case BACK:
                    //Gdx.app.exit();
                break;
            default:
                break;
        }


    }

    /**
     * Implements method of interface InputListener
     *
     * @see InputListener
     * */

    @Override
    public void keyUp(InputManager manager, GameKeys key) {

    }

    @Override
    public void scroll(InputManager manager, float amount) {

    }



}

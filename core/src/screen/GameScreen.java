package screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.MyGdxGame;

import static com.mygdx.game.MyGdxGame.*;

public class GameScreen  extends AbstractScreen{

    private final BodyDef bodyDef;
    private final FixtureDef fixtureDef;

    public GameScreen(final MyGdxGame context) {
        super(context);

        bodyDef = new BodyDef();
        fixtureDef= new FixtureDef();

        //create a circle
        bodyDef.position.set(4.5f,15);
        bodyDef.gravityScale = 1;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bodyDef);
        body.setUserData("CIRCLE");

        fixtureDef.isSensor = false;
        fixtureDef.restitution = 0.5f;
        fixtureDef.friction = 0.2f;
        fixtureDef.filter.categoryBits = BIT_CIRCLE;
        fixtureDef.filter.maskBits = BIT_GROUND;
        CircleShape cShape = new CircleShape();
        cShape.setRadius(0.5f);
        fixtureDef.shape = cShape;
        body.createFixture(fixtureDef);
        cShape.dispose();

        fixtureDef.isSensor = true;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 0.2f;
        fixtureDef.filter.categoryBits = BIT_CIRCLE;
        fixtureDef.filter.maskBits = BIT_BOX;
        ChainShape chainShape = new ChainShape();
        chainShape.createChain(new float[]{-0.5f, -0.7f, 0.5f, -0.7f});
        fixtureDef.shape = chainShape;
        body.createFixture(fixtureDef);
        chainShape.dispose();

        //create a box
        bodyDef.position.set(5.3f,2);
        bodyDef.gravityScale = 1;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        body.setUserData("BOX");

        fixtureDef.isSensor = false;
        fixtureDef.restitution = 0;
        fixtureDef.friction = 0.2f;
        fixtureDef.filter.categoryBits = BIT_BOX;
        fixtureDef.filter.maskBits = BIT_GROUND | BIT_CIRCLE;
        PolygonShape pShape = new PolygonShape();
        pShape.setAsBox(0.5f, 0.5f);
        fixtureDef.shape = pShape;
        body.createFixture(fixtureDef);
        pShape.dispose();

        //create a platform
        bodyDef.position.set(4.5f,2);
        bodyDef.gravityScale = 1;
        bodyDef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bodyDef);
        body.setUserData("PLATFORM");

        fixtureDef.isSensor = false;
        fixtureDef.restitution = 0;
        fixtureDef.friction = 0.2f;
        fixtureDef.filter.categoryBits = BIT_GROUND;
        fixtureDef.filter.maskBits = -1;
        pShape = new PolygonShape();
        pShape.setAsBox(4, 0.5f);
        fixtureDef.shape = pShape;
        body.createFixture(fixtureDef);
        pShape.dispose();

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            context.setScreen(ScreenType.LOADING);
        }
        viewport.apply(true);
        box2DDebugRenderer.render(world, viewport.getCamera().combined);
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

    }
}

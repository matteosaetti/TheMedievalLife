package com.mygdx.game.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.ecs.ECSEngine;
import com.mygdx.game.ecs.component.AnimationComponent;
import com.mygdx.game.ecs.component.B2DComponent;

import java.util.EnumMap;

import static com.mygdx.game.MyGdxGame.UNIT_SCALE;

public class GameRenderer implements Disposable, MapListener {
    public static final String TAG = GameRenderer.class.getSimpleName();

    private final OrthographicCamera gameCamera;
    private final SpriteBatch spriteBatch;
    private final AssetManager assetManager;
    private final EnumMap<AnimationType, Animation<Sprite>> animationCache;
    private final FitViewport viewport;

    private final ImmutableArray<Entity> animatedEntities;
    private final OrthogonalTiledMapRenderer mapRenderer;

    private final GLProfiler profiler;
    private final Box2DDebugRenderer box2DDebugRenderer;
    private final World world;

    private final Array<TiledMapTileLayer> tileMapLayer;

    public GameRenderer(final MyGdxGame context, EnumMap<AnimationType, Animation<Sprite>> animationCache){

        gameCamera = context.getGameCamera();
        assetManager = context.getAssetManager();
        viewport = context.getScreenViewport();
        spriteBatch = context.getSpriteBatch();

        animationCache = new EnumMap<AnimationType, Animation<Sprite>>(AnimationType.class);

        animatedEntities = context.getEcsEngine().getEntitiesFor(Family.all(AnimationComponent.class, B2DComponent.class).get());
        this.animationCache = animationCache;

        mapRenderer = new OrthogonalTiledMapRenderer(null, UNIT_SCALE, spriteBatch);
        context.getMapManager().addMapListener(this);
        tileMapLayer = new Array<TiledMapTileLayer>();

        profiler = new GLProfiler(Gdx.graphics);
        profiler.disable();
        if(profiler.isEnabled()){
            box2DDebugRenderer = new Box2DDebugRenderer();
            world = context.getWorld();
        }
        else{
            box2DDebugRenderer = null;
            world = null;
        }
    }
    public void render(final float alpha){
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply(false);
        spriteBatch.begin();
        if (mapRenderer.getMap() != null) {
            AnimatedTiledMapTile.updateAnimationBaseTime();
            mapRenderer.setView(gameCamera);
            for(final TiledMapTileLayer layer : tileMapLayer){
                mapRenderer.renderTileLayer(layer);
            }
        }

        for(final Entity entity : animatedEntities){
            renderEntity(entity,alpha);
        }
        spriteBatch.end();

        if(profiler.isEnabled()) {
            Gdx.app.debug("RenderInfo", "Bindings " + profiler.getTextureBindings());
            Gdx.app.debug("RenderInfo", "Draw calls " + profiler.getDrawCalls());
            profiler.reset();
            box2DDebugRenderer.render(world,gameCamera.combined);
        }
    }

    private void renderEntity(final Entity entity, final float alpha) {
        final B2DComponent b2DComponent = ECSEngine.b2dCmpMapper.get(entity);
        final AnimationComponent aniComponent = ECSEngine.aniCmpMapper.get(entity);

        if(aniComponent.aniType!=null){
            final Animation<Sprite> animation = getAnimation(aniComponent.aniType);
            final Sprite frame = animation.getKeyFrame(aniComponent.aniTime);
            b2DComponent.renderPosition.lerp(b2DComponent.body.getPosition(), alpha);
            frame.setBounds(b2DComponent.renderPosition.x - aniComponent.width * 0.5f, b2DComponent.renderPosition.y - b2DComponent.height * 0.5f, aniComponent.width, aniComponent.height);
            frame.draw(spriteBatch);
        }




    }

    private Animation<Sprite> getAnimation(AnimationType aniType) {
        Animation<Sprite> animation = animationCache.get(aniType);
        if(animation==null){
            //create animation
            Gdx.app.debug(TAG, "creating new animation of type " + aniType);
            final TextureAtlas.AtlasRegion atlasRegion = assetManager.get(aniType.getAtlasPath(), TextureAtlas.class).findRegion(aniType.getAtlasKey());
            final TextureRegion[][] textureRegions = atlasRegion.split(64,64);
            animation = new Animation<Sprite>(aniType.getFrameTime(), getKeyFrames(textureRegions[aniType.getRawIndex()]), Animation.PlayMode.LOOP);
            animationCache.put(aniType, animation);
        }

        return animation;
    }

    private Array<? extends Sprite> getKeyFrames(TextureRegion[] textureRegion) {
        final Array<Sprite> keyFrames = new Array<Sprite>();

        for(final TextureRegion region : textureRegion){
            final Sprite sprite = new Sprite(region);
            sprite.setOriginCenter();
            keyFrames.add(sprite);
        }

        return keyFrames;
    }

    @Override
    public void dispose() {
        if(box2DDebugRenderer != null){
            box2DDebugRenderer.dispose();
        }
        mapRenderer.dispose();
    }

    @Override
    public void mapChange(final Map map) {
        mapRenderer.setMap(map.getTiledMap());
        map.getTiledMap().getLayers().getByType(TiledMapTileLayer.class, tileMapLayer);


    }
}

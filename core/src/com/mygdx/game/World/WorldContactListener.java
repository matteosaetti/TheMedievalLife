package com.mygdx.game.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.map.MapType;
import com.mygdx.game.ui.ActionType;
import com.mygdx.game.ui.GameUI;

public class WorldContactListener implements ContactListener {
    private final MyGdxGame context;
    private Array<PortalListener> portalListeners;

    public WorldContactListener(MyGdxGame context){
        this.context = context;
        portalListeners = new Array<>();
    }

    public void addPortalListener(PortalListener portalListener){
        portalListeners.add(portalListener);
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        // if Player-<...>
        if(fixA.getUserData() == "player_sensor" || fixB.getUserData() == "player_sensor"){
            Fixture player = fixA.getUserData() == "player_sensor" ? fixA : fixB;
            Fixture other = fixA.getUserData() == "player_sensor" ? fixB : fixA;

            // if Player-Portal
            if(other.getUserData() != null && other.getUserData().getClass().equals(Portal.class)){
                GameUI.getInstance().getActionPossible().showAction(ActionType.PORTAL);

                //during contact listener, changing map with loadMap() would cause a crash because box2d World is locked
                for(PortalListener portalListener : portalListeners){
                    portalListener.PortalCrossed((Portal) other.getUserData());
                }
            }
        }
        else
            Gdx.app.debug(this.getClass().getSimpleName(),
                    fixA.getUserData().getClass().getSimpleName() + " : "
                            + fixB.getUserData().getClass().getSimpleName());
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        // if Player-<...>
        if(fixA.getUserData() == "player_sensor" || fixB.getUserData() == "player_sensor"){
            Fixture player = fixA.getUserData() == "player_sensor" ? fixA : fixB;
            Fixture other = fixA.getUserData() == "player_sensor" ? fixB : fixA;

            // if Player-Portal
            if(other.getUserData() != null && other.getUserData().getClass().equals(Portal.class)){
                GameUI.getInstance().getActionPossible().hideAction(ActionType.PORTAL);
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}

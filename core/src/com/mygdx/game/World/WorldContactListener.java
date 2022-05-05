package com.mygdx.game.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.ui.ActionType;
import com.mygdx.game.ui.GameUI;

public class WorldContactListener implements ContactListener {
    private final MyGdxGame context;
    private Array<PortalListener> portalListenerArray;

    public WorldContactListener(MyGdxGame context) {
        this.context = context;
        portalListenerArray = new Array<>();
    }

   public void addPortalListener(PortalListener portalListener){
        portalListenerArray.add(portalListener);
   }


    @Override
    public void beginContact(Contact contact) {

      Fixture fixA = contact.getFixtureA();
      Fixture fixB = contact.getFixtureB();

      //contact player-other
      if(fixA.getUserData() == "player_sensor" || fixB.getUserData() == "player_sensor"){
          Fixture player = fixA.getUserData() == "player_sensor" ? fixA : fixB;
          Fixture other = fixA.getUserData() == "player_sensor" ? fixB : fixA;
          //contact player-portal
          if(other.getUserData() != null && other.getUserData().getClass().equals(Portal.class)){
              GameUI.getInstance().getActionPossible().showAction(ActionType.PORTAL);

              for(PortalListener portalListener : portalListenerArray){
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
        final Fixture fixA = contact.getFixtureA();
        final Fixture fixB = contact.getFixtureB();

        //if player contact with other
        if(fixA.getUserData() == "player_sensor" || fixB.getUserData() == "player_sensor"){
            Fixture player = fixA.getUserData() == "player_sensor" ? fixA : fixB;
            Fixture other = fixA.getUserData() == "player_sensor" ? fixB : fixA;

            // if player contact with portal
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

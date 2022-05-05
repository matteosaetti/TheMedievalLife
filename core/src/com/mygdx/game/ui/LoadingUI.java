package com.mygdx.game.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.StringBuilder;
public class LoadingUI extends Table {

    private final ProgressBar progressBar;
    private final TextButton loadingString;
    private final TextButton textButton;

    public LoadingUI(final Skin skin) {
        super(skin);
        setFillParent(true);

        progressBar = new ProgressBar(0,1, 0.01f, false, skin, "default");
        loadingString = new TextButton("LOADING...", skin, "huge");
        textButton = new TextButton("NONE", skin, "debug");
        progressBar.setAnimateDuration(1);
        textButton.getLabel().setWrap(true);

       loadingString.setVisible(false);
       loadingString.addListener(new InputListener(){
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
               return true;
            }
        });


       add(loadingString).expandX().fillX().center().row();
       add(textButton).expandX().fillX().bottom().row();
       add(progressBar).expandX().fillX().bottom().pad(20,25,20,25);
       bottom();

    }

    public void setProgress(final float progress){
        progressBar.setValue(progress);

        final StringBuilder stringBuilder = textButton.getLabel().getText();
        stringBuilder.setLength(0);
        stringBuilder.append(loadingString);
        stringBuilder.append( " (");
        stringBuilder.append(progress * 100);
        stringBuilder.append("% )");
        textButton.getLabel().invalidateHierarchy();

        if(progress >= 1 && !loadingString.isVisible()){
            loadingString.setVisible(true);
            loadingString.setColor(1,1,1,0);
            loadingString.addAction(Actions.forever(Actions.sequence(Actions.alpha(1, 1), Actions.alpha(0,1))));
        }
    }

    public void setProgressBar(final float progress) {
        progressBar.setValue(progress);
    }

    public void setLoadingStatus(final int remaining) {
        loadingString.setText("assets remaining: " + remaining);
    }
}

package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.mygdx.game.World.Entities.NPC.NPC;
import com.mygdx.game.ui.conversation.Conversation;
import com.mygdx.game.ui.conversation.ConversationChoice;
import com.mygdx.game.ui.conversation.ConversationGraph;

import java.util.ArrayList;

public class Dialogue extends Table {
    private Skin skin;
    private Label dialogueText;
    private Label dialogueSpeaker;
    private List _listItems;

    private ConversationGraph graph;
    private Json json;


    public Dialogue(Skin skin, String text, String speaker) {
        this.skin=skin;
        this.json = new Json();
        this.setVisible(false);
        setDebug(false);

        //Init WidgetsGroup
        Stack dialogueStack = new Stack();;
        Table table = new Table(skin);


        //Init Widgets
        Image dialogueBox=new Image(skin.getDrawable("dialogbox"));
        dialogueBox.setSize(600,120);
        dialogueText= new Label(text, skin, "debug");
        dialogueText.setAlignment(Align.left);
        dialogueText.setWrap(true);
        dialogueSpeaker=new Label(speaker, skin, "debug");
        dialogueSpeaker.setAlignment(Align.topLeft);
        _listItems = new List<ConversationChoice>(skin);
        _listItems.addListener(new ClickListener() {
                                   @Override
                                   public void clicked (InputEvent event, float x, float y) {
                                       ConversationChoice choice = (ConversationChoice)_listItems.getSelected();

                                       if( choice == null ) return;
                                       graph.notify(graph, choice.getConversationCommandEvent());
                                       populateConversationDialog(choice.getDestinationId());
                                   }
                               }
        );

        ScrollPane scrollPane = new ScrollPane(_listItems, skin);
        scrollPane.setOverscroll(false, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setForceScroll(true, false);
        scrollPane.setScrollBarPositions(false, true);

        //Layout setup
        add(dialogueStack).top();
        dialogueStack.add(dialogueBox);
        dialogueStack.add(table);


        table.setDebug(false);
        table.top();
        table.add(dialogueSpeaker).pad(15, 28, 0, 0).top().left();
        table.row();
        table.add(dialogueText).width(600).pad(20,20,0,20);
        table.row();
        table.add(scrollPane).top();

    }

    public void loadConversation(NPC npc, String personalizedMessage){
        clearDialog();

        //Set Speaker name
        if(npc == null){
            this.setSpeaker("Sistema");
            setMessage(personalizedMessage);
            GameUI.getInstance().getAction().setVisible(true);
            return;
        }
        else
            this.setSpeaker(npc.getNPCname());


        //Set ConversationGraph
        String conversationFilenamePath = npc.getConversationConfigPath();
        if( conversationFilenamePath.isEmpty() || !Gdx.files.internal(conversationFilenamePath).exists() ){
            Gdx.app.debug(this.getClass().getSimpleName(), "Conversation file does not exist!");
            return;
        }

        setConversationGraph(json.fromJson(ConversationGraph.class, Gdx.files.internal(conversationFilenamePath)));
    }

    public void setConversationGraph(ConversationGraph graph){
        if( graph != null ) graph.removeAllObservers();
        this.graph = graph;
        populateConversationDialog(graph.getCurrentConversationID());
    }

    private void populateConversationDialog(String conversationID){
        clearDialog();

        Conversation conversation = graph.getConversationByID(conversationID);
        if( conversation == null ) return;
        graph.setCurrentConversation(conversationID);
        setMessage(conversation.getDialog());

        ArrayList<ConversationChoice> choices = graph.getCurrentChoices();
        if( choices == null ) return;
        _listItems.setItems(choices.toArray());
        _listItems.setSelectedIndex(-1);
    }

    public void setMessage(String string){
        dialogueText.setText(string);
    }

    public void setSpeaker(String string){
        dialogueSpeaker.setText(string);
    }

    private void clearDialog(){
        setMessage("");
        _listItems.clearItems();
    }
}

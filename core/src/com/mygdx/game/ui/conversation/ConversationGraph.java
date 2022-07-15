package com.mygdx.game.ui.conversation;

import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;


public class ConversationGraph extends ConversationGraphSubject {
    private Hashtable<String, Conversation> conversations;
    private Hashtable<String, ArrayList<ConversationChoice>> associatedChoices;
    private String currentConversationID = null;

    public ConversationGraph(){
    }

    public ConversationGraph(Hashtable<String, Conversation> conversations, String rootID){
        setConversations(conversations);
        setCurrentConversation(rootID);
    }

    public void setConversations(Hashtable<String, Conversation> conversations) {
        if( conversations.size() < 0 ){
            throw new IllegalArgumentException(
                    "Can't have a negative amount of conversations");
        }
        this.conversations = conversations;
        this.associatedChoices = new Hashtable<String, ArrayList<ConversationChoice>>(conversations.size());
        for( Conversation conversation: conversations.values() ){
            associatedChoices.put( conversation.getId(), new ArrayList<ConversationChoice>());
        }
        this.conversations = conversations;
    }

    public ArrayList<ConversationChoice> getCurrentChoices(){
        return associatedChoices.get(currentConversationID);
    }

    public String getCurrentConversationID(){
        return this.currentConversationID;
    }

    public boolean isValid(String conversationID){
        Conversation conversation = conversations.get(conversationID);
        return conversation != null;
    }

    public boolean isReachable(String sourceID, String sinkID){
        if( !isValid(sourceID) || !isValid(sinkID) ) return false;
        if( conversations.get(sourceID) == null ) return false;

        //First get edges/choices from the source
        ArrayList<ConversationChoice> list = associatedChoices.get(sourceID);
        if( list == null ) return false;
        for(ConversationChoice choice: list){
            if(choice.getSourceId().equalsIgnoreCase(sourceID) &&
                    choice.getDestinationId().equalsIgnoreCase(sinkID)
            ){
                return true;
            }
        }
        return false;
    }

    public Conversation getConversationByID(String id){
        if( !isValid(id) ){
            System.out.println("Id " + id + " is not valid!");
            return null;
        }
        return conversations.get(id);
    }

    public void setCurrentConversation(String id){
        Conversation conversation = getConversationByID(id);
        if( conversation == null ) return;
        currentConversationID = id;
    }

    public void addChoice(ConversationChoice conversationChoice){
        ArrayList<ConversationChoice> list =
                associatedChoices.get(conversationChoice.getSourceId());
        if( list == null) return;
        list.add(conversationChoice);
    }

    public String getCurrentConversation(){
        return conversations.get(currentConversationID).getDialog();
    }
    public String toJson(){
        Json json = new Json();
        return json.prettyPrint(this);
    }
}

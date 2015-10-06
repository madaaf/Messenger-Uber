package com.excilys.voisinsenor.network.event;

import com.excilys.voisinsenor.model.Message;
import java.util.List;

/**
 * Created by mada on 25/09/15.
 */
public class GetConversationEvent {
    List<Message> conversation;

    public List<Message> getConversation() {
        return conversation;
    }

    public void setConversation(List<Message> conversation) {
        this.conversation = conversation;
    }
}

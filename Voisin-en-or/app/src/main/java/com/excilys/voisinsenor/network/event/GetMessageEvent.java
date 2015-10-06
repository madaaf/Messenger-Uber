package com.excilys.voisinsenor.network.event;

import com.excilys.voisinsenor.model.Message;

/**
 * Created by mada on 14/09/15.
 */
public class GetMessageEvent {
    private Message message;

    public GetMessageEvent(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}

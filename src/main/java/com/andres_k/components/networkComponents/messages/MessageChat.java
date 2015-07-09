package com.andres_k.components.networkComponents.messages;

import com.andres_k.components.networkComponents.MessageModel;

/**
 * Created by andres_k on 08/07/2015.
 */
public class MessageChat extends MessageModel {
    private boolean all;
    private String message;

    public MessageChat() {
    }

    public MessageChat(String pseudo, String id, boolean all, String message) {
        this.pseudo = pseudo;
        this.id = id;
        this.all = all;
        this.message = message;
    }

    // GETTERS
    public boolean isAll() {
        return this.all;
    }

    public String getMessage() {
        return this.message;
    }
}

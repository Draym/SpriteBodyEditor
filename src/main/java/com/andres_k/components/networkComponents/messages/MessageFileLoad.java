package com.andres_k.components.networkComponents.messages;

import com.andres_k.components.networkComponents.MessageModel;

/**
 * Created by andres_k on 09/07/2015.
 */
public class MessageFileLoad extends MessageModel {
    private String path;

    public MessageFileLoad(String pseudo, String id, String path){
        this.pseudo = pseudo;
        this.id = id;
        this.path = path;
    }

    public String getPath(){
        return this.path;
    }
}

package com.andres_k.components.networkComponents.messages;

import com.andres_k.components.networkComponents.MessageModel;

/**
 * Created by andres_k on 09/07/2015.
 */
public class MessageSelectImage extends MessageModel {
    private String file;

    public MessageSelectImage(String pseudo, String id, String file){
        this.pseudo = pseudo;
        this.id = id;
        this.file = file;
    }

    public String getFile() {
        return this.file;
    }
}

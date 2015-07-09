package com.andres_k.components.gameComponents.gameObject;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 09/07/2015.
 */
public class BodyCreator {
    private String path;
    private Image image;
    private List<BodySprite> bodies;

    public BodyCreator(String path, Image image){
        this.path = path;
        this.image = image;
        this.bodies = new ArrayList<>();
    }

    public void draw(Graphics g){
        g.drawImage(this.image, 0, 0);
        for (BodySprite body : this.bodies){
            body.draw(g);
        }
    }

    public void update(){

    }

    public void event(int key, char c){

    }

    public void onClick(float x, float y){
        for (BodySprite body : this.bodies){
            body.isOnFocus(x, y);
        }
    }
}

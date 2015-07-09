package com.andres_k.components.gameComponents.gameObject;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;


/**
 * Created by andres_k on 09/07/2015.
 */
public class BodySprite {
    private Rectangle body;
    private boolean focused;
    private EnumGameObject type;

    public BodySprite(Rectangle body, EnumGameObject type) {
        this.body = body;
        this.type = type;
        this.focused = false;
    }

    public void draw(Graphics g) {
        if (this.focused) {
            g.setColor(Color.yellow);
        } else if (this.type == EnumGameObject.UNBREAKABLE) {
            g.setColor(Color.cyan);
        } else if (this.type == EnumGameObject.ATTACK_BODY) {
            g.setColor(Color.red);
        } else if (this.type == EnumGameObject.UNBREAKABLE) {
            g.setColor(Color.green);
        }
        g.draw(this.body);
    }

    public boolean isOnFocus(float x, float y) {
        if (this.body.contains(x, y)) {
            this.focused = true;
        } else {
            this.focused = false;
        }
        return this.focused;
    }


    // GETTERS
    public boolean isFocused() {
        return this.focused;
    }

    public Rectangle getBody() {
        return this.body;
    }

    public EnumGameObject getType() {
        return this.type;
    }

    // SETTERS
    public void setType(EnumGameObject type) {
        this.type = type;
    }

    public void setFocused(boolean value) {
        this.focused = value;
    }
}

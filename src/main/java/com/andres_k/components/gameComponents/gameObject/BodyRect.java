package com.andres_k.components.gameComponents.gameObject;

import com.andres_k.utils.configs.GlobalVariable;
import com.andres_k.utils.stockage.Pair;
import com.andres_k.utils.tools.ColorTools;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;


/**
 * Created by andres_k on 09/07/2015.
 */
public class BodyRect {
    private Rectangle body;
    private EnumGameObject type;

    private boolean focused;
    private Pair<Float, Float> origin;

    public BodyRect(Rectangle body, EnumGameObject type, float posX, float posY) {
        this.body = new Rectangle(body.getMinX(), body.getMinY(), body.getWidth(), body.getHeight());
        this.origin = new Pair<>(posX, posY);
        this.type = type;
        this.focused = false;
    }

    public BodyRect(JSONObject object, float posX, float posY) throws JSONException {
        this.type = EnumGameObject.getEnumByValue(object.getString("type"));
        this.origin = new Pair<>(posX, posY);
        this.body = new Rectangle((float) object.getDouble("posX") + origin.getV1(), (float) object.getDouble("posY") + origin.getV2(), (float) object.getDouble("sizeX"), (float) object.getDouble("sizeY"));
    }

    public void draw(Graphics g) {
        if (this.focused) {
            g.setColor(ColorTools.get(ColorTools.Colors.TRANSPARENT_YELLOW));
            g.fillRect((this.body.getMinX() * GlobalVariable.zoom) - GlobalVariable.originX, (this.body.getMinY() * GlobalVariable.zoom) - GlobalVariable.originY,
                    this.body.getWidth() * GlobalVariable.zoom, this.body.getHeight() * GlobalVariable.zoom);
        } else if (this.type == EnumGameObject.DEFENSE_BODY) {
            g.setColor(Color.cyan);
        } else if (this.type == EnumGameObject.ATTACK_BODY) {
            g.setColor(Color.orange);
        } else if (this.type == EnumGameObject.BLOCK_BODY) {
            g.setColor(Color.green);
        }
        if (!focused) {
            g.drawRect((this.body.getMinX() * GlobalVariable.zoom) - GlobalVariable.originX, (this.body.getMinY() * GlobalVariable.zoom) - GlobalVariable.originY,
                    this.body.getWidth() * GlobalVariable.zoom, this.body.getHeight() * GlobalVariable.zoom);
        }
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

    @Override
    public String toString(){
        JSONObject object = new JSONObject();

        try {
            object.put("type", this.type.getValue());
            object.put("posX", this.body.getMinX() - this.origin.getV1());
            object.put("posY", this.body.getMinY() - this.origin.getV2());
            object.put("sizeX", this.body.getWidth());
            object.put("sizeY", this.body.getHeight());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object.toString();
    }
}

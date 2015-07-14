package com.andres_k.components.gameComponents.gameObject;

import com.andres_k.utils.configs.GlobalVariable;
import com.andres_k.utils.stockage.Pair;
import com.andres_k.utils.tools.Debug;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 09/07/2015.
 */
public class BodySprite {
    private List<BodyRect> bodies;
    private Rectangle spriteBody;
    private Pair<Float, Float> center;

    public BodySprite(Rectangle spriteBody) {
        this.spriteBody = spriteBody;
        this.bodies = new ArrayList<>();
        this.center = new Pair<>(this.spriteBody.getWidth() / 2, this.spriteBody.getHeight() / 2);
    }

    public BodySprite(JSONObject object) throws JSONException {
        this.bodies = new ArrayList<>();
        this.center = new Pair<>((float) object.getDouble("centerX"), (float) object.getDouble("centerY"));
        this.spriteBody = new Rectangle((float) object.getDouble("posX"), (float) object.getDouble("posY"), (float) object.getDouble("sizeX"), (float) object.getDouble("sizeY"));
        JSONArray array = object.getJSONArray("rectangles");

        for (int i = 0; i < array.length(); ++i){
            this.bodies.add(new BodyRect(array.getJSONObject(i), this.spriteBody.getMinX(), this.spriteBody.getMinY()));
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.drawRect((this.spriteBody.getMinX() * GlobalVariable.zoom) - GlobalVariable.originX, (this.spriteBody.getMinY() * GlobalVariable.zoom) - GlobalVariable.originY,
                this.spriteBody.getWidth() * GlobalVariable.zoom, this.spriteBody.getHeight() * GlobalVariable.zoom);
        for (BodyRect body : this.bodies) {
            body.draw(g);
        }
    }

    public void update() {
    }

    public boolean changeFocusedType(EnumGameObject type) {
        for (int i = 0; i < this.bodies.size(); ++i) {
            if (this.bodies.get(i).isFocused()) {
                Debug.debug("index: " + i);
                this.bodies.get(i).setType(type);
                if (this.bodies.get(i).getType() == EnumGameObject.NULL) {
                    this.bodies.remove(i);
                    --i;
                }
                return true;
            }
        }
        return false;
    }

    public boolean isOnFocus(float x, float y) {
        boolean find = false;
        for (BodyRect body : this.bodies) {
            if (find == false) {
                if (body.isOnFocus(x, y)) {
                    find = true;
                }
            }
        }
        return this.spriteBody.contains(x, y);
    }

    public boolean intersect(Shape object) {
        if (this.spriteBody.contains(object.getMinX(), object.getMinY()) && this.spriteBody.contains(object.getMaxX(), object.getMaxY())) {
            return false;
        }
        return true;
    }

    public void addRect(Rectangle body, EnumGameObject type) {
        if (body.getWidth() >= 2 && body.getHeight() >= 2 && !this.intersect(body)) {
            this.bodies.add(new BodyRect(body, type, this.spriteBody.getMinX(), this.spriteBody.getMinY()));
        }
    }

    public void addCircle(Circle body, EnumGameObject type) {
        if (body.getRadius() >= 2 && !this.intersect(body)) {
            this.bodies.add(new BodyRect(body, type, this.spriteBody.getMinX(), this.spriteBody.getMinY()));
        }
    }

    @Override
    public String toString() {
        JSONObject object = new JSONObject();

        try {
            object.put("centerX", this.center.getV1());
            object.put("centerY", this.center.getV2());

            object.put("posX", this.spriteBody.getMinX());
            object.put("posY", this.spriteBody.getMinY());
            object.put("sizeX", this.spriteBody.getWidth());
            object.put("sizeY", this.spriteBody.getHeight());
            JSONArray arrayRect = new JSONArray();
            for (BodyRect body : this.bodies) {
                arrayRect.put(new JSONObject(body.toString()));
            }
            object.put("rectangles", arrayRect);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object.toString();
    }
}

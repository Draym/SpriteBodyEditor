package com.andres_k.components.gameComponents.gameObject;

import com.andres_k.utils.configs.GlobalVariable;
import com.andres_k.utils.stockage.Pair;
import com.andres_k.utils.tools.ColorTools;
import com.andres_k.utils.tools.Console;
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
import java.util.UUID;


/**
 * Created by andres_k on 09/07/2015.
 */
public class BodyRect {
    //  private Rectangle body;


    private Pair<Float, Float> positions;
    private Pair<Float, Float> sizes;
    private EnumGameObject type;
    private String id;

    private List<String> links;

    private boolean focused;
    private Pair<Float, Float> origin;

    public BodyRect(Rectangle body, EnumGameObject type, float posX, float posY) {
        this(body, type, posX, posY, 0, 0);
    }

    public BodyRect(Rectangle body, EnumGameObject type, float posX, float posY, float addX, float addY) {
        this.positions = new Pair<>(body.getMinX() + addX, body.getMinY() + addY);
        this.sizes = new Pair<>(body.getWidth(), body.getHeight());
        this.origin = new Pair<>(posX, posY);
        this.type = type;
        this.focused = false;
        this.id = UUID.randomUUID().toString();
        this.links = new ArrayList<>();
        Console.debug("SAVE RECTANGLE at [" + this.positions.getV1() + ", " + this.positions.getV2() + "] with  {" + this.sizes.getV1() + ", " + this.sizes.getV2() + "}");
    }

    public BodyRect(Circle body, EnumGameObject type, float posX, float posY) {
        this(body, type, posX, posY, 0, 0);
    }

    public BodyRect(Circle body, EnumGameObject type, float posX, float posY, float addX, float addY) {
        this.positions = new Pair<>(body.getCenterX() + addX, body.getCenterY() + addY);
        this.sizes = new Pair<>(body.getRadius(), -1f);
        this.origin = new Pair<>(posX, posY);
        this.type = type;
        this.focused = false;
        this.id = UUID.randomUUID().toString();
        this.links = new ArrayList<>();
        Console.debug("SAVE CIRCLE at [" + this.positions.getV1() + ", " + this.positions.getV2() + "] with  {" + this.sizes.getV1() + ", " + this.sizes.getV2() + "}");
    }

    public BodyRect(JSONObject object, float posX, float posY) throws JSONException {
        this.type = EnumGameObject.getEnumByValue(object.getString("type"));
        this.origin = new Pair<>(posX, posY);
        this.focused = false;
        this.id = object.getString("id");
        this.links = new ArrayList<>();
        JSONArray array = object.getJSONArray("links");
        for (int i = 0; i < array.length(); ++i) {
            this.links.add(array.getString(i));
        }
        this.positions = new Pair<>((float) object.getDouble("posX") + this.origin.getV1(), (float) object.getDouble("posY") + this.origin.getV2());
        this.sizes = new Pair<>((float) object.getDouble("sizeX"), (float) object.getDouble("sizeY"));
        Console.debug("New Rect: " + this);
    }

    public void draw(Graphics g) {
        if (this.focused) {
            g.setColor(ColorTools.get(ColorTools.Colors.TRANSPARENT_YELLOW));
            g.fill(this.getBodyDraw());
        } else if (this.type == EnumGameObject.DEFENSE_BODY) {
            g.setColor(Color.cyan);
        } else if (this.type == EnumGameObject.ATTACK_BODY) {
            g.setColor(Color.orange);
        } else if (this.type == EnumGameObject.BLOCK_BODY) {
            g.setColor(Color.green);
        }
        if (!this.focused) {
            g.draw(this.getBodyDraw());
        }
    }

    public Shape getBody() {
        if (this.sizes.getV2() < 0) {
            return new Circle(this.positions.getV1(), this.positions.getV2(), this.sizes.getV1());
        } else {
            return new Rectangle(this.positions.getV1(), this.positions.getV2(), this.sizes.getV1(), this.sizes.getV2());
        }
    }

    public Shape getOriginBody() {
        if (this.sizes.getV2() < 0) {
            return new Circle(this.positions.getV1() - this.origin.getV1(), this.positions.getV2() - this.origin.getV2(), this.sizes.getV1());
        } else {
            return new Rectangle(this.positions.getV1() - this.origin.getV1(), this.positions.getV2() - this.origin.getV2(), this.sizes.getV1(), this.sizes.getV2());
        }
    }

    public Shape getBodyDraw() {
        if (this.sizes.getV2() < 0) {
            return new Circle((this.positions.getV1() * GlobalVariable.zoom) - GlobalVariable.originX, (this.positions.getV2() * GlobalVariable.zoom) - GlobalVariable.originY,
                    this.sizes.getV1() * GlobalVariable.zoom);
        } else {
            return new Rectangle((this.positions.getV1() * GlobalVariable.zoom) - GlobalVariable.originX, (this.positions.getV2() * GlobalVariable.zoom) - GlobalVariable.originY,
                    this.sizes.getV1() * GlobalVariable.zoom, this.sizes.getV2() * GlobalVariable.zoom);
        }
    }

    public boolean isOnFocus(float x, float y) {
        boolean focus = false;
        Shape body = this.getBody();
        if (x >= body.getMinX() && y >= body.getMinY()
                && x <= body.getMaxX() && y <= body.getMaxY())
            focus = true;
        if (this.focused && focus) {
            this.focused = false;
        } else {
            this.focused = focus;
        }
        return focus;
    }

    public void addLink(String id) {
        for (String link : this.links) {
            if (link.equals(id))
                return;
        }
        this.links.add(id);
    }

    public void removeLink(String id) {
        for (int i = 0; i < this.links.size(); ++i) {
            if (this.links.get(i).equals(id)) {
                this.links.remove(i);
                --i;
            }
        }
    }

    // GETTERS
    public String getId() {
        return this.id;
    }

    public List<String> getLinks() {
        return this.links;
    }

    public boolean isFocused() {
        return this.focused;
    }

    public EnumGameObject getType() {
        return this.type;
    }

    public Pair<Float, Float> getOrigin() {
        return this.origin;
    }

    public Pair<Float, Float> getPositions() {
        return this.positions;
    }

    public Pair<Float, Float> getSizes() {
        return this.sizes;
    }

    // SETTERS
    public void setType(EnumGameObject type) {
        this.type = type;
    }

    public void setFocused(boolean value) {
        this.focused = value;
    }

    @Override
    public String toString() {
        JSONObject object = new JSONObject();

        try {
            object.put("id", this.id);
            object.put("type", this.type.getValue());
            object.put("posX", (int) (this.positions.getV1() - this.origin.getV1()));
            object.put("posY", (int) (this.positions.getV2() - this.origin.getV2()));
            object.put("sizeX", this.sizes.getV1().intValue());
            object.put("sizeY", this.sizes.getV2().intValue());

            JSONArray array = new JSONArray();
            for (String value : this.links) {
                array.put(value);
            }
            object.put("links", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object.toString();
    }
}

package com.andres_k.components.gameComponents.gameObject;

import com.andres_k.utils.configs.GlobalVariable;
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
import java.util.stream.Collectors;

/**
 * Created by andres_k on 09/07/2015.
 */
public class BodySprite {
    private List<BodyRect> bodies;
    private Rectangle sprite;
    private Rectangle body;

    public BodySprite(Rectangle sprite) {
        this.sprite = sprite;
        this.body = new Rectangle(sprite.getMinX(), sprite.getMinY(), sprite.getWidth(), sprite.getHeight());
        this.bodies = new ArrayList<>();
    }

    public BodySprite(JSONObject object) throws JSONException {
        this.bodies = new ArrayList<>();
        this.sprite = new Rectangle((float) object.getDouble("SpritePosX"), (float) object.getDouble("SpritePosY"), (float) object.getDouble("SpriteSizeX"), (float) object.getDouble("SpriteSizeY"));
        this.body = new Rectangle((float) object.getDouble("BodyPosX") + this.sprite.getMinX(), (float) object.getDouble("BodyPosY") + this.sprite.getMinY(), (float) object.getDouble("BodySizeX"), (float) object.getDouble("BodySizeY"));
        JSONArray array = object.getJSONArray("rectangles");

        for (int i = 0; i < array.length(); ++i) {
            this.bodies.add(new BodyRect(array.getJSONObject(i), this.sprite.getMinX(), this.sprite.getMinY()));
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.drawRect((this.sprite.getMinX() * GlobalVariable.zoom) - GlobalVariable.originX, (this.sprite.getMinY() * GlobalVariable.zoom) - GlobalVariable.originY,
                this.sprite.getWidth() * GlobalVariable.zoom, this.sprite.getHeight() * GlobalVariable.zoom);
        g.setColor(Color.green);
        g.drawRect((this.body.getMinX() * GlobalVariable.zoom) - GlobalVariable.originX, (this.body.getMinY() * GlobalVariable.zoom) - GlobalVariable.originY,
                this.body.getWidth() * GlobalVariable.zoom, this.body.getHeight() * GlobalVariable.zoom);
        for (BodyRect body : this.bodies) {
            body.draw(g);

            for (String id : body.getLinks()) {
                BodyRect target = this.getBodyRect(id);

                if (target != null) {
                    Shape b1 = body.getBodyDraw();
                    Shape b2 = target.getBodyDraw();
                    g.setColor(Color.magenta);
                    g.drawLine(b1.getCenterX(), b1.getCenterY(), b2.getCenterX(), b2.getCenterY());
                }
            }
        }
    }

    public void update() {
    }

    public boolean changeFocusedType(EnumGameObject type) {
        for (int i = 0; i < this.bodies.size(); ++i) {
            if (this.bodies.get(i).isFocused()) {
                Console.debug("index: " + i);
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
            if (!find) {
                if (body.isOnFocus(x, y)) {
                    find = true;
                }
            }
        }
        return this.isInSprite(x, y);
    }

    public boolean isInBody(Shape object) {
        return (object.getMinX() >= this.body.getMinX() && object.getMinY() >= this.body.getMinY()
                && object.getMaxX() <= this.body.getMaxX() && object.getMaxY() <= this.body.getMaxY());
    }

    public boolean isInSprite(Shape object) {
        Console.debug("minX: " + object.getMinX() + " >= ? " + this.sprite.getMinX());
        Console.debug("minY: " + object.getMinY() + " >= ? " + this.sprite.getMinY());
        Console.debug("maxX: " + object.getMaxX() + " <= ? " + this.sprite.getMaxX());
        Console.debug("maxY: " + object.getMaxY() + " <= ? " + this.sprite.getMaxY());
        return (object.getMinX() >= this.sprite.getMinX() && object.getMinY() >= this.sprite.getMinY()
                && object.getMaxX() < this.sprite.getMaxX() && object.getMaxY() < this.sprite.getMaxY());
    }

    public boolean isInSprite(float x, float y) {
        return (x >= this.sprite.getMinX() && y >= this.sprite.getMinY()
                && x < this.sprite.getMaxX() && y < this.sprite.getMaxY());
    }

    public void changeBody(Rectangle body) {
        if (body.getWidth() >= 1 && body.getHeight() >= 1 && isInSprite(body)) {
            this.body = body;
        }
    }

    public void addBodyRect(List<BodyRect> bodies) {
        for (BodyRect item : bodies) {
            if (item.getOriginBody() instanceof Rectangle) {
                this.bodies.add(new BodyRect((Rectangle) item.getOriginBody(), item.getType(), this.sprite.getMinX(), this.sprite.getMinY(), this.sprite.getMinX(), this.sprite.getMinY()));
            } else {
                this.bodies.add(new BodyRect((Circle) item.getOriginBody(), item.getType(), this.sprite.getMinX(), this.sprite.getMinY(), this.sprite.getMinX(), this.sprite.getMinY()));
            }
        }
    }

    public void addRect(Rectangle body, EnumGameObject type) {
        if (body.getWidth() >= 1 && body.getHeight() >= 1 && this.isInBody(body)) {
            this.bodies.add(new BodyRect(body, type, this.sprite.getMinX(), this.sprite.getMinY()));
        }
    }

    public void addCircle(Circle body, EnumGameObject type) {
        if (body.getRadius() >= 2 && this.isInBody(body)) {
            this.bodies.add(new BodyRect(body, type, this.sprite.getMinX(), this.sprite.getMinY()));
        }
    }

    public void addLinkToFocusedRect() {
        for (BodyRect rect : this.bodies) {
            if (rect.isFocused()) {
                for (BodyRect target : this.bodies) {
                    if (target.isFocused() && !target.getId().equals(rect.getId())) {
                        rect.addLink(target.getId());
                        target.addLink(rect.getId());
                    }
                }
            }
        }
    }

    public void removeLinkFromFocusedRect() {
        for (BodyRect rect : this.bodies) {
            if (rect.isFocused()) {
                for (BodyRect target : this.bodies) {
                    if (target.isFocused() && !target.getId().equals(rect.getId())) {
                        rect.removeLink(target.getId());
                        target.removeLink(rect.getId());
                    }
                }
            }
        }
    }

    public List<BodyRect> getFocusedBodyRect() {
        List<BodyRect> result = this.bodies.stream().filter(BodyRect::isFocused).collect(Collectors.toList());
        return (result.size() != 0 ? result : null);
    }

    public BodyRect getBodyRect(String id) {
        for (int i = 0; i < this.bodies.size(); ++i) {
            if (this.bodies.get(i).getId().equals(id)) {
                return this.bodies.get(i);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        JSONObject object = new JSONObject();

        try {
            object.put("SpritePosX", this.sprite.getMinX());
            object.put("SpritePosY", this.sprite.getMinY());
            object.put("SpriteSizeX", this.sprite.getWidth());
            object.put("SpriteSizeY", this.sprite.getHeight());

            object.put("BodyPosX", this.body.getMinX() - this.sprite.getMinX());
            object.put("BodyPosY", this.body.getMinY() - this.sprite.getMinY());
            object.put("BodySizeX", this.body.getWidth());
            object.put("BodySizeY", this.body.getHeight());
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

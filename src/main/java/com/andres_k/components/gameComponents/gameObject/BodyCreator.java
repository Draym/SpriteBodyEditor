package com.andres_k.components.gameComponents.gameObject;

import com.andres_k.utils.configs.GlobalVariable;
import com.andres_k.utils.tools.Console;
import com.andres_k.utils.tools.FilesTools;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres_k on 09/07/2015.
 */
public class BodyCreator {
    private String path;
    private Image image;
    private float sizeXSprite;
    private float sizeYSprite;
    private List<BodySprite> bodies;

    private List<BodyRect> bodyRectCopy;

    public BodyCreator(String path, Image image, float sizeX, float sizeY) {
        this.path = path;
        this.image = image;
        this.sizeXSprite = sizeX;
        this.sizeYSprite = sizeY;
        this.bodies = new ArrayList<>();
        this.bodyRectCopy = new ArrayList<>();
        for (int i = 0; (i + this.sizeYSprite) <= this.image.getHeight(); i += this.sizeYSprite) {
            for (int i2 = 0; (i2 + this.sizeXSprite) <= this.image.getWidth(); i2 += this.sizeXSprite) {
                this.bodies.add(new BodySprite(new Rectangle(i2, i, this.sizeXSprite, this.sizeYSprite)));
            }
        }
    }

    public BodyCreator(JSONObject object) throws JSONException, SlickException {
        this.bodies = new ArrayList<>();
        this.bodyRectCopy = new ArrayList<>();
        this.path = object.getString("path");
        this.image = new Image(GlobalVariable.folder + this.path);
        this.sizeXSprite = (float) object.getDouble("spriteSizeX");
        this.sizeXSprite = (float) object.getDouble("spriteSizeY");
        JSONArray array = object.getJSONArray("sprites");

        for (int i = 0; i < array.length(); ++i) {
            this.bodies.add(new BodySprite(array.getJSONObject(i)));
        }
    }

    public void draw(Graphics g) {
        this.image.draw(-GlobalVariable.originX, -GlobalVariable.originY, GlobalVariable.zoom);
        for (BodySprite body : this.bodies) {
            body.draw(g);
        }
    }

    public void update() {
        this.bodies.forEach(BodySprite::update);
    }

    public void changeFocusedType(EnumGameObject type) {
        for (BodySprite body : this.bodies) {
            if (body.changeFocusedType(type)) {
                return;
            }
        }
    }

    public void copyBodyRect() {
        Console.debug("try to copy");
        this.bodyRectCopy.clear();
        for (BodySprite body : this.bodies) {
            List<BodyRect> result = body.getFocusedBodyRect();
            if (result != null) {
                Console.debug("--> copy one");
                this.bodyRectCopy.addAll(result);
                break;
            }
        }
    }

    public void pasteBodyRect(BodySprite item) {
        Console.debug("try to paste: " + this.bodyRectCopy.size());
        if (this.bodyRectCopy.size() == 1) {
            Console.debug("paste one");
            item.addRect(new Rectangle((GlobalVariable.mouseX + GlobalVariable.originX) / GlobalVariable.zoom, (GlobalVariable.mouseY + GlobalVariable.originY) / GlobalVariable.zoom, this.bodyRectCopy.get(0).getSizes().getV1(), this.bodyRectCopy.get(0).getSizes().getV2()), this.bodyRectCopy.get(0).getType());
        } else {
            item.addBodyRect(this.bodyRectCopy);
        }
    }

    public void addLinkToFocusedRect() {
        for (BodySprite body : this.bodies) {
            body.addLinkToFocusedRect();
        }
    }

    public void removeLinkToFocusedRect() {
        for (BodySprite body : this.bodies) {
            body.removeLinkFromFocusedRect();
        }
    }

    public BodySprite onClick(float x, float y) {
        for (BodySprite body : this.bodies) {
            if (body.isOnFocus(x, y)) {
                return body;
            }
        }
        return null;
    }

    public void saveInFile() {
        String jsonName = this.path.substring(0, this.path.indexOf(".")) + ".json";
        FilesTools.writeInFile(GlobalVariable.folder + jsonName, this.toString());
    }

    @Override
    public String toString() {
        JSONObject object = new JSONObject();

        try {
            object.put("path", this.path);
            object.put("spriteNumber", this.bodies.size());
            object.put("spriteSizeX", this.sizeXSprite);
            object.put("spriteSizeY", this.sizeYSprite);

            JSONArray arraySprite = new JSONArray();

            for (BodySprite bodyRect : this.bodies) {
                arraySprite.put(new JSONObject(bodyRect.toString()));
            }
            object.put("sprites", arraySprite);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }


    public List<BodyRect> getBodyRectCopy() {
        return this.bodyRectCopy;
    }

}

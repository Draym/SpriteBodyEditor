package com.andres_k.components.gameComponents.controllers;

import com.andres_k.components.gameComponents.gameObject.BodyCreator;
import com.andres_k.components.graphicComponents.graphic.EnumWindow;
import com.andres_k.components.networkComponents.messages.MessageFileCreate;
import com.andres_k.components.taskComponent.EnumTargetTask;
import com.andres_k.utils.stockage.Tuple;
import com.andres_k.utils.tools.Debug;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import java.util.List;
import java.util.Observable;

/**
 * Created by andres_k on 08/07/2015.
 */
public class GameController extends WindowController {
    private BodyCreator bodyCreator;

    Image tmp = null;

    public GameController(){
        this.bodyCreator = null;
    }

    @Override
    public void init() {
    }

    @Override
    public void renderWindow(Graphics g) {
        if (tmp != null)
        g.drawImage(tmp, 0, 0);
    }

    @Override
    public void updateWindow() {
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Tuple) {
            Tuple<EnumTargetTask, EnumTargetTask, Object> received = (Tuple<EnumTargetTask, EnumTargetTask, Object>) arg;
            Debug.debug("RECEIVED Game: " + received);
            if (received.getV1().equals(EnumTargetTask.WINDOWS) && received.getV2().equals(EnumTargetTask.GAME)) {
                if (received.getV3() instanceof EnumWindow) {
                    if (received.getV3() == EnumWindow.EXIT && this.window != null) {
                        this.window.quit();
                    } else if (this.stateWindow != null) {
                        this.stateWindow.enterState(((EnumWindow) received.getV3()).getValue());
                    }
                } else if (received.getV3() instanceof MessageFileCreate){
                    List<Object> objects = ((MessageFileCreate) received.getV3()).getObjects();
                    try {
                        this.bodyCreator = new BodyCreator((String) objects.get(0), new Image("Sprite&Body/" + objects.get(0)));
                    } catch (Exception e) {
                        e.printStackTrace();
                        this.bodyCreator = null;
                    }
                }
            }
        }
    }
}

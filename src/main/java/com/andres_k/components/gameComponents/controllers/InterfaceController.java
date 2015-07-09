package com.andres_k.components.gameComponents.controllers;

import com.andres_k.components.graphicComponents.graphic.EnumWindow;
import com.andres_k.components.graphicComponents.userInterface.overlay.EnumOverlayElement;
import com.andres_k.components.graphicComponents.userInterface.tools.elements.Element;
import com.andres_k.components.graphicComponents.userInterface.tools.elements.StringElement;
import com.andres_k.components.graphicComponents.userInterface.tools.items.StringTimer;
import com.andres_k.components.networkComponents.messages.MessageFileCreate;
import com.andres_k.components.networkComponents.messages.MessageFileLoad;
import com.andres_k.components.networkComponents.messages.MessageSelectImage;
import com.andres_k.components.taskComponent.EnumTargetTask;
import com.andres_k.components.taskComponent.TaskFactory;
import com.andres_k.utils.stockage.Pair;
import com.andres_k.utils.stockage.Tuple;
import com.andres_k.utils.tools.Debug;
import com.andres_k.utils.tools.StringTools;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

/**
 * Created by andres_k on 08/07/2015.
 */
public class InterfaceController extends WindowController {
    private HashMap<String, Image> files;
    private String currentPath;
    private Image currentImage;

    public InterfaceController() throws JSONException, SlickException {
        this.files = new HashMap<>();
        this.currentImage = null;
        this.currentPath = null;
        this.initSavedFiles();
    }

    public void initSavedFiles() throws JSONException, SlickException {
        String saveFiles = StringTools.readFile("saveFiles.json");
        JSONObject jsonFiles = new JSONObject(saveFiles);

        JSONArray arrayFiles = jsonFiles.getJSONArray("files");

        for (int i = 0; i < arrayFiles.length(); ++i) {
            this.files.put(arrayFiles.getString(i), null);
        }
    }

    public void sendFilesToOverlay() {
        for (Map.Entry<String, Image> file : this.files.entrySet()) {
            this.setChanged();
            this.notifyObservers(TaskFactory.createTask(EnumTargetTask.INTERFACE, EnumTargetTask.INTERFACE_OVERLAY, new Pair<>(EnumOverlayElement.TABLE_LIST,
                    new StringElement(new StringTimer(file.getKey()), Color.black, EnumOverlayElement.TABLE_LIST.getValue() + ":" + file.getKey(), Element.PositionInBody.MIDDLE_MID))));
        }
    }

    public void initImageFile() throws SlickException {
        for (Map.Entry<String, Image> file : this.files.entrySet()) {
            file.setValue(new Image("Sprite&Body/" + file.getKey()));
        }
    }

    @Override
    public void init() throws SlickException {
        this.initImageFile();
        this.sendFilesToOverlay();

    }

    @Override
    public void renderWindow(Graphics g) {
        if (this.currentImage != null) {
            g.drawImage(this.currentImage, 500, 500);
        }
    }

    @Override
    public void updateWindow() {

    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Tuple) {
            Tuple<EnumTargetTask, EnumTargetTask, Object> received = (Tuple<EnumTargetTask, EnumTargetTask, Object>) arg;

            Debug.debug("RECEIVED Interface: " + received);
            if (received.getV1().equals(EnumTargetTask.WINDOWS) && received.getV2().equals(EnumTargetTask.INTERFACE)) {
                if (received.getV3() instanceof EnumWindow) {
                    if (received.getV3() == EnumWindow.EXIT && this.window != null) {
                        this.window.quit();
                    } else if (this.stateWindow != null) {
                        this.stateWindow.enterState(((EnumWindow) received.getV3()).getValue());
                    }
                } else if (received.getV3() instanceof MessageFileCreate) {
                    this.launchWithConfig((MessageFileCreate) received.getV3());
                } else if (received.getV3() instanceof MessageSelectImage) {
                    if (this.files.containsKey(((MessageSelectImage) received.getV3()).getFile())) {
                        this.currentImage = this.files.get(((MessageSelectImage) received.getV3()).getFile());
                        this.currentPath = ((MessageSelectImage) received.getV3()).getFile();
                    } else {
                        this.currentImage = null;
                        this.currentPath = null;
                    }
                }
            }
        }
    }

    public void launchWithConfig(MessageFileCreate file){
        this.setChanged();
        this.notifyObservers(TaskFactory.createTask(EnumTargetTask.INTERFACE, EnumTargetTask.GAME, file));
        this.stateWindow.enterState(EnumWindow.GAME.getValue());
    }

    public void launchWithPath(MessageFileLoad file){
        this.setChanged();
        this.notifyObservers(TaskFactory.createTask(EnumTargetTask.INTERFACE, EnumTargetTask.GAME, file));
        this.stateWindow.enterState(EnumWindow.GAME.getValue());
    }

    public String getCurrentPath(){
        return this.currentPath;
    }
}

package com.andres_k.components.gameComponents.controllers;

import com.andres_k.components.graphicComponents.graphic.EnumWindow;
import com.andres_k.components.graphicComponents.userInterface.overlay.EnumOverlayElement;
import com.andres_k.components.graphicComponents.userInterface.tools.elements.Element;
import com.andres_k.components.graphicComponents.userInterface.tools.elements.StringElement;
import com.andres_k.components.graphicComponents.userInterface.tools.items.StringTimer;
import com.andres_k.components.networkComponents.messages.MessageFileLoad;
import com.andres_k.components.networkComponents.messages.MessageFileNew;
import com.andres_k.components.networkComponents.messages.MessageSelectImage;
import com.andres_k.components.taskComponent.EnumTargetTask;
import com.andres_k.components.taskComponent.TaskFactory;
import com.andres_k.utils.configs.GlobalVariable;
import com.andres_k.utils.stockage.Pair;
import com.andres_k.utils.stockage.Tuple;
import com.andres_k.utils.tools.StringTools;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.newdawn.slick.*;

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

    @Override
    public void enter() {

    }

    @Override
    public void leave() {
        try {
            this.saveFilesInFile();
        } catch (JSONException e) {
            e.printStackTrace();
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
            g.drawImage(this.currentImage, 400, 300);
        }
    }

    @Override
    public void updateWindow(GameContainer gameContainer) {

    }

    @Override
    public void keyPressed(int key, char c) {

    }

    @Override
    public void keyReleased(int key, char c) {
        if (key == Input.KEY_ENTER && this.currentPath != null) {
            this.launchWithPath(new MessageFileLoad("admin", "admin", this.currentPath));
        } else if (key == Input.KEY_BACK || key == Input.KEY_DELETE){
            if (this.files.containsKey(this.currentPath)){
                this.setChanged();
                this.notifyObservers(TaskFactory.createTask(EnumTargetTask.INTERFACE, EnumTargetTask.INTERFACE_OVERLAY, new Pair<>(EnumOverlayElement.TABLE_LIST,
                        new StringElement(null, Color.black, EnumOverlayElement.TABLE_LIST.getValue() + ":" + this.currentPath, Element.PositionInBody.MIDDLE_MID))));
                this.files.remove(this.currentPath);
                this.currentPath = null;
                this.currentImage = null;
            }
        }
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
    }

    @Override
    public void mousePressed(int button, int x, int y) {

    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Tuple) {
            Tuple<EnumTargetTask, EnumTargetTask, Object> received = (Tuple<EnumTargetTask, EnumTargetTask, Object>) arg;

            if (received.getV1().equals(EnumTargetTask.WINDOWS) && received.getV2().equals(EnumTargetTask.INTERFACE)) {
                if (received.getV3() instanceof EnumWindow) {
                    if (this.stateWindow != null) {
                        this.stateWindow.enterState(((EnumWindow) received.getV3()).getValue());
                    }
                } else if (received.getV3() instanceof MessageFileNew) {
                    this.launchWithConfig((MessageFileNew) received.getV3());
                } else if (received.getV3() instanceof MessageSelectImage) {
                    if (this.files.containsKey(((MessageSelectImage) received.getV3()).getFile())) {
                        this.currentImage = this.files.get(((MessageSelectImage) received.getV3()).getFile());
                        this.currentPath = ((MessageSelectImage) received.getV3()).getFile();
                    } else {
                        this.currentImage = null;
                        this.currentPath = null;
                    }
                } else if (received.getV3() instanceof MessageFileLoad) {
                    try {
                        this.files.put(((MessageFileLoad) received.getV3()).getPath(), new Image(GlobalVariable.folder + ((MessageFileLoad) received.getV3()).getPath()));
                        this.setChanged();
                        this.notifyObservers(TaskFactory.createTask(EnumTargetTask.INTERFACE, EnumTargetTask.INTERFACE_OVERLAY, new Pair<>(EnumOverlayElement.TABLE_LIST,
                                new StringElement(new StringTimer(((MessageFileLoad) received.getV3()).getPath()), Color.black, EnumOverlayElement.TABLE_LIST.getValue() + ":" + ((MessageFileLoad) received.getV3()).getPath(), Element.PositionInBody.MIDDLE_MID))));
                    } catch (SlickException e) {
                        e.printStackTrace();
                    }
                } else if (received.getV3() instanceof EnumOverlayElement) {
                    if (received.getV3() == EnumOverlayElement.EXIT) {
                        this.window.quit();
                    }
                }
            }
        }

    }

    public void saveFilesInFile() throws JSONException {
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();

        for (Map.Entry<String, Image> entry : this.files.entrySet()) {
            array.put(entry.getKey());
        }
        object.put("files", array);
        StringTools.writeInFile("saveFiles.json", object.toString());
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

    public void initImageFile() {
        try {
            for (Map.Entry<String, Image> file : this.files.entrySet()) {
                file.setValue(new Image(GlobalVariable.folder + file.getKey()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void launchWithConfig(MessageFileNew file) {
        this.setChanged();
        this.notifyObservers(TaskFactory.createTask(EnumTargetTask.INTERFACE, EnumTargetTask.GAME, file));
    }

    public void launchWithPath(MessageFileLoad file) {
        this.setChanged();
        this.notifyObservers(TaskFactory.createTask(EnumTargetTask.INTERFACE, EnumTargetTask.GAME, file));
    }

    public String getCurrentPath() {
        return this.currentPath;
    }
}

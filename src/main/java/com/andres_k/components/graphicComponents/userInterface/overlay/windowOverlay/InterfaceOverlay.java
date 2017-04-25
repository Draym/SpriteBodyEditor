package com.andres_k.components.graphicComponents.userInterface.overlay.windowOverlay;

import com.andres_k.components.gameComponents.animations.AnimatorOverlayData;
import com.andres_k.components.graphicComponents.input.EnumInput;
import com.andres_k.components.graphicComponents.input.InputData;
import com.andres_k.components.graphicComponents.sounds.MusicController;
import com.andres_k.components.graphicComponents.sounds.SoundController;
import com.andres_k.components.graphicComponents.userInterface.elements.InterfaceElement;
import com.andres_k.components.graphicComponents.userInterface.elements.generic.GenericElement;
import com.andres_k.components.graphicComponents.userInterface.elements.table.TableMenuElement;
import com.andres_k.components.graphicComponents.userInterface.elements.table.TableSelectElement;
import com.andres_k.components.graphicComponents.userInterface.overlay.EnumOverlayElement;
import com.andres_k.components.graphicComponents.userInterface.overlay.Overlay;
import com.andres_k.components.graphicComponents.userInterface.tools.elements.*;
import com.andres_k.components.graphicComponents.userInterface.tools.items.BodyRect;
import com.andres_k.components.graphicComponents.userInterface.tools.items.StringTimer;
import com.andres_k.components.networkComponents.MessageModel;
import com.andres_k.components.taskComponent.EnumTargetTask;
import com.andres_k.components.taskComponent.TaskFactory;
import com.andres_k.utils.configs.WindowConfig;
import com.andres_k.utils.stockage.Pair;
import com.andres_k.utils.stockage.Tuple;
import com.andres_k.utils.tools.ColorTools;
import com.andres_k.utils.tools.Console;
import com.andres_k.utils.tools.StringTools;
import org.codehaus.jettison.json.JSONException;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;

/**
 * Created by andres_k on 20/06/2015.
 */
public class InterfaceOverlay extends Overlay {

    public InterfaceOverlay() throws JSONException {
        super();

        this.initElements();
        this.initPreference();
    }

    @Override
    public void initElements() {
        float menuX = (WindowConfig.getSizeX() / 2) - 150;
        float menuY = (WindowConfig.getSizeY() / 2) - 150;

        this.elements.put(EnumOverlayElement.TABLE_MENU_CONTROLS, new TableMenuElement(EnumOverlayElement.TABLE_MENU_CONTROLS, this.genericSendTask,
                new BodyRect(new Rectangle(menuX, menuY, 400, 300), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLUE))));
        this.elements.put(EnumOverlayElement.TABLE_MENU_SETTINGS, new GenericElement(EnumOverlayElement.TABLE_MENU_SETTINGS,
                new BodyRect(new Rectangle(menuX, menuY, 300, 310), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLUE)), new Pair<>(false, true), false, new boolean[]{true, true}));
        this.elements.put(EnumOverlayElement.TABLE_MENU_NEW, new GenericElement(EnumOverlayElement.TABLE_MENU_NEW, this.genericSendTask,
                new BodyRect(new Rectangle(menuX, menuY, 300, 200), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLUE)), new Pair<>(false, true), false, new boolean[]{true, true}));

        this.elements.put(EnumOverlayElement.TABLE_MENU, new GenericElement(EnumOverlayElement.TABLE_MENU, this.genericSendTask,
                new BodyRect(new Rectangle(menuX, menuY, 300, 310), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLUE)), new Pair<>(true, true), false, new boolean[]{true, true}));


        this.elements.put(EnumOverlayElement.TABLE_LIST, new TableSelectElement(EnumOverlayElement.TABLE_LIST,
                new BodyRect(new Rectangle(0, 200, 300, 600), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLUE)), this.genericSendTask));
    }

    @Override
    public void initElementsComponent(AnimatorOverlayData animatorOverlayData) {
        this.animatorOverlayData = animatorOverlayData;

        this.initTableMenu();
        this.initTableMenuNew();
        this.initTableMenuControls();
        this.initTableMenuSettings();

        this.initTableListFile();
    }

    @Override
    public void enter() {
        this.initTableMenuControls();
    }

    private void initTableListFile(){
        InterfaceElement tableList = this.elements.get(EnumOverlayElement.TABLE_LIST);

        tableList.doTask(new StringElement(new StringTimer("File list"), Color.black, EnumOverlayElement.TABLE_LIST.getValue() + ":" +EnumOverlayElement.TABLE_LIST.getValue(), Element.PositionInBody.MIDDLE_MID));
    }

    private void initTableMenu() {
        InterfaceElement tableMenu = this.elements.get(EnumOverlayElement.TABLE_MENU);

        tableMenu.doTask(new ButtonElement(new ImageElement(new BodyRect(new Rectangle(tableMenu.getBody().getMinX() + 20, tableMenu.getBody().getMinY() + 20, tableMenu.getBody().getSizeX() - 40, 60), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLUE)),
                this.animatorOverlayData.getMenuAnimator(EnumOverlayElement.NEW), Element.PositionInBody.MIDDLE_MID), EnumOverlayElement.TABLE_MENU_NEW));
        tableMenu.doTask(new ButtonElement(new ImageElement(new BodyRect(new Rectangle(tableMenu.getBody().getMinX() + 20, tableMenu.getBody().getMinY() + 90, tableMenu.getBody().getSizeX() - 40, 60), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLUE)),
                this.animatorOverlayData.getMenuAnimator(EnumOverlayElement.CONTROLS), Element.PositionInBody.MIDDLE_MID), EnumOverlayElement.TABLE_MENU_CONTROLS));
        tableMenu.doTask(new ButtonElement(new ImageElement(new BodyRect(new Rectangle(tableMenu.getBody().getMinX() + 20, tableMenu.getBody().getMinY() + 160, tableMenu.getBody().getSizeX() - 40, 60), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLUE)),
                this.animatorOverlayData.getMenuAnimator(EnumOverlayElement.SETTINGS), Element.PositionInBody.MIDDLE_MID), EnumOverlayElement.TABLE_MENU_SETTINGS));
        tableMenu.doTask(new ButtonElement(new ImageElement(new BodyRect(new Rectangle(tableMenu.getBody().getMinX() + 20, tableMenu.getBody().getMinY() + 230, tableMenu.getBody().getSizeX() - 40, 60), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLUE)),
                this.animatorOverlayData.getMenuAnimator(EnumOverlayElement.EXIT), Element.PositionInBody.MIDDLE_MID), EnumOverlayElement.EXIT));
    }

    private void initTableMenuControls() {
        InterfaceElement tableMenuControls = this.elements.get(EnumOverlayElement.TABLE_MENU_CONTROLS);

        tableMenuControls.doTask(new ButtonElement(new StringElement(new StringTimer("Controls"), Color.black,
                EnumOverlayElement.CONTROLS.getValue() + ":" + EnumOverlayElement.CONTROLS.getValue(), Element.PositionInBody.MIDDLE_MID), EnumOverlayElement.BUTTON));

        tableMenuControls.doTask(new Pair<>("clear", EnumOverlayElement.CONTROLS.getValue() + ":" + EnumOverlayElement.CONTROLS.getValue()));
        for (Map.Entry<EnumInput, String> entry : InputData.getAvailableInput().entrySet()) {
            tableMenuControls.doTask(new ButtonElement(new StringElement(new StringTimer(entry.getKey().getValue() + ":" +
                    StringTools.duplicateString(" ", 14 - entry.getKey().getValue().length()) + entry.getValue() +
                    StringTools.duplicateString(" ", 18 - entry.getValue().length())), Color.black,
                    EnumOverlayElement.CONTROLS.getValue() + ":" + entry.getKey().getValue(), Element.PositionInBody.MIDDLE_MID), EnumOverlayElement.CONTROLS));
        }
    }

    private void initTableMenuSettings() {
        InterfaceElement tableMenuSettings = this.elements.get(EnumOverlayElement.TABLE_MENU_SETTINGS);
        float posX = tableMenuSettings.getBody().getMinX();
        float posY = tableMenuSettings.getBody().getMinY();
        float sizeX = tableMenuSettings.getBody().getSizeX();

        tableMenuSettings.doTask(new StringElement(new BodyRect(new Rectangle(posX, posY, sizeX, StringTools.charSizeY())), new StringTimer("Settings"), Color.black, Element.PositionInBody.MIDDLE_MID));

        posY += (StringTools.charSizeY() * 2);
        tableMenuSettings.doTask(new StringElement(new BodyRect(new Rectangle(posX, posY, tableMenuSettings.getBody().getSizeX() / 2, StringTools.charSizeY())), new StringTimer("Sounds"), Color.black, Element.PositionInBody.MIDDLE_MID));
        posY += (StringTools.charSizeY() * 2);
        tableMenuSettings.doTask(new StringElement(new BodyRect(new Rectangle(posX, posY, (int) (tableMenuSettings.getBody().getSizeX() / 1.1), StringTools.charSizeY())), new StringTimer(String.valueOf((int) (SoundController.getVolume() * 100))), Color.black, EnumOverlayElement.SOUNDS_VALUE.getValue(), Element.PositionInBody.RIGHT_MID));
        tableMenuSettings.doTask(new ImageElement(new BodyRect(new Rectangle(posX + 10, posY + 4, 202, 12), ColorTools.get(ColorTools.Colors.TRANSPARENT_BLACK)), EnumOverlayElement.SOUNDS_GRAPH.getValue() + EnumOverlayElement.BORDER.getValue(), Element.PositionInBody.LEFT_MID));
        tableMenuSettings.doTask(new ImageElement(new BodyRect(new Rectangle(posX + 11, posY + 5, 200, 10), ColorTools.get(ColorTools.Colors.TRANSPARENT_BLUE)), EnumOverlayElement.SOUNDS_GRAPH.getValue(), Element.PositionInBody.LEFT_MID));

        posY += 50;
        tableMenuSettings.doTask(new StringElement(new BodyRect(new Rectangle(posX, posY, tableMenuSettings.getBody().getSizeX() / 2, StringTools.charSizeY())), new StringTimer("Musics"), Color.black, Element.PositionInBody.MIDDLE_MID));
        posY += (StringTools.charSizeY() * 2);
        tableMenuSettings.doTask(new StringElement(new BodyRect(new Rectangle(posX, posY, (int) (tableMenuSettings.getBody().getSizeX() / 1.1), StringTools.charSizeY())), new StringTimer(String.valueOf((int) (MusicController.getVolume() * 100))), Color.black, EnumOverlayElement.MUSICS_VALUE.getValue(), Element.PositionInBody.RIGHT_MID));
        tableMenuSettings.doTask(new ImageElement(new BodyRect(new Rectangle(posX + 10, posY + 4, 202, 12), ColorTools.get(ColorTools.Colors.TRANSPARENT_BLACK)), EnumOverlayElement.MUSICS_GRAPH.getValue() + EnumOverlayElement.BORDER.getValue(), Element.PositionInBody.LEFT_MID));
        tableMenuSettings.doTask(new ImageElement(new BodyRect(new Rectangle(posX + 11, posY + 5, 200, 10), ColorTools.get(ColorTools.Colors.TRANSPARENT_BLUE)), EnumOverlayElement.MUSICS_GRAPH.getValue(), Element.PositionInBody.LEFT_MID));

        tableMenuSettings.doTask(new Pair<>(EnumOverlayElement.SOUNDS_GRAPH, new Pair<>("cutBody", SoundController.getVolume() / SoundController.getMaxVolume())));
        tableMenuSettings.doTask(new Pair<>(EnumOverlayElement.MUSICS_GRAPH, new Pair<>("cutBody", MusicController.getVolume() / MusicController.getMaxVolume())));
    }

    public void initTableMenuNew() {
        InterfaceElement tableMenuNew = this.elements.get(EnumOverlayElement.TABLE_MENU_NEW);

        float posX = tableMenuNew.getBody().getMinX();
        float posY = tableMenuNew.getBody().getMinY();

        posY += (StringTools.charSizeY() * 2);
        tableMenuNew.doTask(new StringElement(new BodyRect(new Rectangle(posX + 10, posY, tableMenuNew.getBody().getSizeX() - 10, StringTools.charSizeY())), new StringTimer("image"), Color.black, Element.PositionInBody.LEFT_MID));
        tableMenuNew.doTask(new SelectionField(new BodyRect(new Rectangle(posX + 60, posY, 200, StringTools.charSizeY()), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLUE)),
                new StringElement(new StringTimer(""), Color.black, Element.PositionInBody.LEFT_MID), EnumOverlayElement.SELECT_FIELD.getValue() + EnumOverlayElement.NEW.getValue() + "path", true));

        posY += (StringTools.charSizeY() * 2);
        tableMenuNew.doTask(new StringElement(new BodyRect(new Rectangle(posX + 10, posY, tableMenuNew.getBody().getSizeX() + 10, StringTools.charSizeY())), new StringTimer("sprite size:"), Color.black, Element.PositionInBody.LEFT_MID));
        posY += (StringTools.charSizeY());
        tableMenuNew.doTask(new StringElement(new BodyRect(new Rectangle(posX + 20, posY, tableMenuNew.getBody().getSizeX() + 20, StringTools.charSizeY())), new StringTimer("sizeX"), Color.black, Element.PositionInBody.LEFT_MID));
        tableMenuNew.doTask(new SelectionField(new BodyRect(new Rectangle(posX + 70, posY, 70, StringTools.charSizeY()), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLUE)),
                new StringElement(new StringTimer(""), Color.black, Element.PositionInBody.LEFT_MID), EnumOverlayElement.SELECT_FIELD.getValue() + EnumOverlayElement.NEW.getValue() + "sizeX", true));
        posY += (StringTools.charSizeY());
        tableMenuNew.doTask(new StringElement(new BodyRect(new Rectangle(posX + 20, posY, tableMenuNew.getBody().getSizeX() + 20, StringTools.charSizeY())), new StringTimer("sizeY"), Color.black, Element.PositionInBody.LEFT_MID));
        tableMenuNew.doTask(new SelectionField(new BodyRect(new Rectangle(posX + 70, posY, 70, StringTools.charSizeY()), ColorTools.get(ColorTools.Colors.TRANSPARENT_GREYBLUE)),
                new StringElement(new StringTimer(""), Color.black, Element.PositionInBody.LEFT_MID), EnumOverlayElement.SELECT_FIELD.getValue() + EnumOverlayElement.NEW.getValue() + "sizeY", true));
        posY += (StringTools.charSizeY());

        tableMenuNew.doTask(new ButtonElement(new ImageElement(new BodyRect(new Rectangle(posX + (tableMenuNew.getBody().getSizeX() / 2) - 25, posY + 20, 50, 27)),
                this.animatorOverlayData.getMenuAnimator(EnumOverlayElement.GO), Element.PositionInBody.MIDDLE_MID), EnumOverlayElement.GO));
    }

    // TASK
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Tuple) {
            Tuple<EnumTargetTask, EnumTargetTask, Object> received = (Tuple<EnumTargetTask, EnumTargetTask, Object>) arg;

            if (received.getV1().equals(EnumTargetTask.WINDOWS) && received.getV2().isIn(EnumTargetTask.INTERFACE_OVERLAY)) {

                Console.debug("\nOVERLAY RECEIVED tuple: " + arg);
                if (received.getV3() instanceof Pair && ((Pair) received.getV3()).getV1() instanceof EnumOverlayElement) {
                    Pair<EnumOverlayElement, Object> task = (Pair<EnumOverlayElement, Object>) received.getV3();

                    List<EnumOverlayElement> targets = new ArrayList<>();
                    targets.addAll(EnumOverlayElement.getChildren(task.getV1()));
                    for (EnumOverlayElement target : targets) {
                        Console.debug("CHIDL: " + targets.size() + " -> send to " + target);
                        if (this.elements.containsKey(target)) {
                            Console.debug("send " + task.getV2() + " to " + target);
                            this.elements.get(target).doTask(task.getV2());
                        }
                    }
                } else {
                    Console.debug("\n*************\nWARNING!\nyou shouldn't call this method like this : " + received.getV3());
                    for (Map.Entry<EnumOverlayElement, InterfaceElement> entry : this.elements.entrySet()) {
                        entry.getValue().doTask(received.getV3());
                    }
                }
            }
        } else if (arg instanceof Pair) {
            Console.debug("OVERLAY RECEIVED pair: " + arg);
            if (((Pair) arg).getV2() instanceof MessageModel) {
                this.setChanged();
                this.notifyObservers(TaskFactory.createTask(EnumTargetTask.INTERFACE_OVERLAY, EnumTargetTask.INTERFACE, ((Pair) arg).getV2()));
            } else if (((Pair) arg).getV1() instanceof EnumOverlayElement && ((Pair) arg).getV2() instanceof EnumOverlayElement) {
                Pair<EnumOverlayElement, EnumOverlayElement> received = (Pair<EnumOverlayElement, EnumOverlayElement>) arg;

                if (received.getV2() == EnumOverlayElement.EXIT && this.elements.containsKey(received.getV1())) {
                    this.elements.get(received.getV1()).stop();
                    this.setChanged();
                    this.notifyObservers(TaskFactory.createTask(EnumTargetTask.INTERFACE_OVERLAY, EnumTargetTask.INTERFACE, received.getV2()));
                } else if (this.elements.containsKey(received.getV1()) && this.elements.containsKey(received.getV2())) {
                    this.elements.get(received.getV1()).stop();
                    this.elements.get(received.getV2()).start();
                }
            } else if (((Pair) arg).getV2() instanceof Pair) {
                Pair<EnumOverlayElement, Pair> received = (Pair<EnumOverlayElement, Pair>) arg;
                if (this.elements.containsKey(received.getV1())) {
                    this.elements.get(received.getV1()).doTask(received.getV2());
                    this.overlayConfigs.setAvailableInput(received.getV1(), this.elements.get(received.getV1()).getReachable());
                }
            }
        }
    }


    // FUNCTIONS

    public void doTask(Object task) {
        if (task instanceof Integer) {
            if ((Integer) task == EnumInput.OVERLAY_1.getIndex() || (Integer) task == EnumInput.OVERLAY_2.getIndex()) {
                String value = EnumInput.getEnumByIndex((Integer) task).getValue();
                this.current = Integer.valueOf(value.substring(value.indexOf("_") + 1)) - 1;
                this.current = (this.current < 0 ? 0 : this.current);
                this.current = (this.current > 1 ? 1 : this.current);
                this.elements.get(EnumOverlayElement.TABLE_MENU_SCREEN).doTask(this.current);
            }
        }
    }

    public boolean event(int key, char c, EnumInput type) {
//        Console.debug("\n NEW EVENT: " + Input.getKeyName(key) + " (" + type + ")");
        for (Map.Entry<EnumOverlayElement, InterfaceElement> entry : this.elements.entrySet()) {
            boolean[] reachable = entry.getValue().getReachable();
            if (reachable[this.current]) {
                Object result = null;
                if (type == EnumInput.PRESSED) {
                    result = entry.getValue().eventPressed(key, c);
                } else if (type == EnumInput.RELEASED) {
                    result = entry.getValue().eventReleased(key, c);
                }
                // TODO: A ENVOYER AUX AUTRE JOUEURS
                if (result instanceof MessageModel) {
                    this.setChanged();
                    this.notifyObservers(TaskFactory.createTask(EnumTargetTask.INTERFACE_OVERLAY, EnumTargetTask.INTERFACE, result));
                    return true;
                } else if (result instanceof Boolean) {
                    return (Boolean) result;
                } else if (result instanceof Pair) {
                    Pair<Object, Object> task = (Pair<Object, Object>) result;

                    if (task.getV1() instanceof EnumInput && task.getV2() instanceof String) {
                        if (InputData.setAvailableInput((EnumInput) task.getV1(), (String) task.getV2())) {
                            this.elements.get(EnumOverlayElement.TABLE_MENU_CONTROLS).doTask(task.getV2());
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
}

package com.andres_k.components.graphicComponents.userInterface.overlay;

import com.andres_k.components.gameComponents.animations.AnimatorOverlayData;
import com.andres_k.components.graphicComponents.input.EnumInput;
import com.andres_k.components.graphicComponents.userInterface.elements.InterfaceElement;
import com.andres_k.components.taskComponent.GenericSendTask;
import com.andres_k.utils.configs.ConfigPath;
import org.codehaus.jettison.json.JSONException;
import org.newdawn.slick.Graphics;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by andres_k on 05/07/2015.
 */
public abstract class Overlay extends Observable implements Observer {
    protected int current;
    protected OverlayConfigs overlayConfigs;
    protected Map<EnumOverlayElement, InterfaceElement> elements;
    protected AnimatorOverlayData animatorOverlayData;
    protected GenericSendTask genericSendTask;

    protected Overlay() throws JSONException {
        this.current = 0;
        this.overlayConfigs = new OverlayConfigs(ConfigPath.preferenceOverlay, ConfigPath.dataOverlay);

        this.genericSendTask = new GenericSendTask();
        this.genericSendTask.addObserver(this);

        this.elements = new LinkedHashMap<>();
    }

    // INIT
    public abstract void initElements();
    public abstract void initElementsComponent(AnimatorOverlayData animatorOverlayData);

    public void initPreference() {
        for (Map.Entry<EnumOverlayElement, boolean[]> entry : this.overlayConfigs.getAvailablePreference().entrySet()) {
            if (this.elements.containsKey(entry.getKey())) {
                this.elements.get(entry.getKey()).setReachable(entry.getValue());
            }
        }
    }

    // FUNCTIONS
    public abstract void enter();

    public void leave() {
        for (Map.Entry<EnumOverlayElement, InterfaceElement> entry : this.elements.entrySet()) {
            entry.getValue().leave();
        }
    }

    public void draw(Graphics g) {
        for (Map.Entry<EnumOverlayElement, InterfaceElement> entry : this.elements.entrySet()) {
            boolean[] reachable = entry.getValue().getReachable();
            if (reachable[this.current]) {
                entry.getValue().draw(g);
            }
        }
    }

    public void updateOverlay() {
        for (Map.Entry<EnumOverlayElement, InterfaceElement> entry : this.elements.entrySet()) {
            entry.getValue().update();
        }
    }

    public void sliderMove(int x, int y) {
        for (Map.Entry<EnumOverlayElement, InterfaceElement> entry : this.elements.entrySet()) {
            entry.getValue().sliderMove(x, y);
        }
    }

    public abstract void doTask(Object task);

    public abstract boolean event(int key, char c, EnumInput type);

    public boolean isOnFocus(int x, int y) {
        boolean result = false;

        for (Map.Entry<EnumOverlayElement, InterfaceElement> entry : this.elements.entrySet()) {
            boolean[] reachable = entry.getValue().getReachable();
            if (reachable[this.current]) {
                if (entry.getValue().isOnFocus(x, y) != null) {
                    result = true;
                }
            }
        }
        return result;
    }

    public boolean isFocused() {
        for (Map.Entry<EnumOverlayElement, InterfaceElement> entry : this.elements.entrySet()) {
            boolean[] reachable = entry.getValue().getReachable();
            if (reachable[this.current]) {
                if (entry.getValue().isActivated()) {
                    return true;
                }
            }
        }
        return false;
    }
}

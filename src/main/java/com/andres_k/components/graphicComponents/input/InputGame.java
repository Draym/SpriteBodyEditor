package com.andres_k.components.graphicComponents.input;


import org.codehaus.jettison.json.JSONException;
import org.newdawn.slick.Input;

import java.util.Observable;

/**
 * Created by andres_k on 11/03/2015.
 */
public class InputGame extends Observable {
    private InputData inputData;
    private boolean valid;

    public InputGame(InputData inputData) throws JSONException {

        if (inputData != null) {
            this.inputData = inputData;
            this.valid = true;
        } else {
            this.valid = false;
        }
    }

    public int checkInput(int key, EnumInput mode) {
        String keyName;

        if (this.valid == false)
            return -1;
        if (key == -2) {
            keyName = "MOUSE_LEFT_BUTTON";
        } else if (key == -3) {
            keyName = "MOUSE_RIGHT_BUTTON";
        } else {
            keyName = Input.getKeyName(key);
        }
        return EnumInput.getIndexByValue(this.inputData.getInputByValue(keyName));
    }
}
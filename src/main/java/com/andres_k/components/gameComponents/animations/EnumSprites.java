package com.andres_k.components.gameComponents.animations;


/**
 * Created by andres_k on 13/03/2015.
 */
public enum EnumSprites {
    //index
    NOTHING(0),
    ROUND(6),
    MENU(7),

    //roundOverlay
    NEW_ROUND(ROUND.getIndex()), END_ROUND(ROUND.getIndex()), TIMER(ROUND.getIndex()),
    //menuOverlay
    EXIT(MENU.getIndex()), SETTINGS(MENU.getIndex()), CONTROLS(MENU.getIndex()), SCREEN(MENU.getIndex()),
    NEW(MENU.getIndex()), GO(MENU.getIndex()), SAVE(MENU.getIndex());


    private final int index;

    EnumSprites(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}

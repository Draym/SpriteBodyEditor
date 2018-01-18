package com.andres_k.components.graphicComponents.input;

/**
 * Created by andres_k on 16/03/2015.
 */
public enum EnumInput {
    NOTHING("NOTHING"),
    RELEASED("RELEASED"), PRESSED("PRESSED"),

    RECTANGLE("RECTANGLE"), CIRCLE("CIRCLE"), BODY("BODY"),
    ATTACK("ATTACK"), DEFENSE("DEFENSE"), BLOCK("BLOCK"), DELETE("DELETE"), COPY("COPY"), PASTE("PASTE"), LINK("LINK"), REMOVE_LINK("REMOVE LINKS"),

    MOVE_UP("MOVE_UP"), MOVE_DOWN("MOVE_DOWN"), MOVE_LEFT("MOVE_LEFT"), MOVE_RIGHT("MOVE_RIGHT"),

    OVERLAY_1("OVERLAY_1"), OVERLAY_2("OVERLAY_2");

    private final int index;
    private final String value;

    EnumInput(String value) {
        this.index = -1;
        this.value = value;
    }

    EnumInput(int index, String value) {
        this.index = index;
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public int getIndex() {
        return this.index;
    }

    public static EnumInput getEnumByIndex(int index) {
        EnumInput[] enums = EnumInput.values();
        int enumsNumber = enums.length;
        for (int i = 0; i < enumsNumber; i++) {
            EnumInput type = enums[i];
            if (index == type.getIndex()) {
                return type;
            }
        }
        return NOTHING;
    }

    public static EnumInput getEnumByValue(String value) {
        EnumInput[] enums = EnumInput.values();
        int enumsNumber = enums.length;
        for (int i = 0; i < enumsNumber; i++) {
            EnumInput type = enums[i];
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return NOTHING;
    }

    public static int getIndexByValue(String value) {
        EnumInput[] enums = EnumInput.values();
        int enumsNumber = enums.length;
        for (int i = 0; i < enumsNumber; i++) {
            EnumInput type = enums[i];
            if (type.getValue().equals(value)) {
                return type.getIndex();
            }
        }
        return NOTHING.getIndex();
    }
}

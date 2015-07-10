package com.andres_k.components.graphicComponents.input;

/**
 * Created by andres_k on 16/03/2015.
 */
public enum EnumInput {
    NOTHING(-3, "NOTHING"),
    RELEASED(-2, "RELEASED"), PRESSED(-1, "PRESSED"),
    ATTACK(0, "ATTACK"), DEFENSE(1, "DEFENSE"), BLOCK(2, "BLOCK"), DELETE(3, "DELETE"),
    OVERLAY_1(4, "OVERLAY_1"), OVERLAY_2(5, "OVERLAY_2");

    private final int index;
    private final String value;

    EnumInput(int index, String value)
    {
        this.index = index;
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }

    public int getIndex(){
        return this.index;
    }

    public static EnumInput getEnumByIndex(int index){
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

    public static EnumInput getEnumByValue(String value){
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

    public static int getIndexByValue(String value){
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

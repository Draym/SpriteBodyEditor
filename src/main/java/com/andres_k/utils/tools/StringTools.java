package com.andres_k.utils.tools;

/**
 * Created by andres_k on 24/03/2015.
 */

public class StringTools {

    public static String duplicateString(String value, int number) {
        String result = "";

        for (int i = 0; i < number; ++i) {
            result += value;
        }
        return result;
    }

    public static float charSizeX() {
        return 9.2f;
    }

    public static float charSizeY() {
        return 20f;
    }
}

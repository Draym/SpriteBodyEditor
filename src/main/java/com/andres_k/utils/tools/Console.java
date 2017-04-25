package com.andres_k.utils.tools;

import com.andres_k.utils.configs.GlobalVariable;

/**
 * Created by andres_k on 13/03/2015.
 */
public class Console {
    public static void debug(String message){
        if (GlobalVariable.debug)
            System.out.println(message);
    }
}

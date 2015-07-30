package com.andres_k.master;


import com.andres_k.components.graphicComponents.graphic.Windows;
import com.andres_k.components.graphicComponents.input.InputData;
import com.andres_k.components.graphicComponents.sounds.MusicController;
import com.andres_k.components.graphicComponents.sounds.SoundController;
import com.andres_k.components.taskComponent.EnumTargetTask;
import com.andres_k.components.taskComponent.GenericSendTask;
import com.andres_k.utils.configs.Config;
import com.andres_k.utils.configs.WindowConfig;
import com.andres_k.utils.stockage.Tuple;
import org.codehaus.jettison.json.JSONException;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by andres_k on 10/03/2015.
 */
public class MasterGame implements Observer {
    private GenericSendTask masterTask;
    private Windows windows;

    public MasterGame() throws SlickException, JSONException {
        WindowConfig.init();
        SoundController.init();
        MusicController.init();
        InputData.init(Config.input);

        this.masterTask = new GenericSendTask();
        this.masterTask.addObserver(this);
        this.windows = new Windows("Sprite&Body Editor", this.masterTask);
    }

    public void start() {
        try {
            this.startGame();
        } catch (SlickException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startGame() throws SlickException, JSONException {
        AppGameContainer appGame = new AppGameContainer(this.windows);
        appGame.setDisplayMode(WindowConfig.getIntSizeX(), WindowConfig.getIntSizeY(), false);
        appGame.start();
    }

    @Override
    public void update(Observable o, Object arg) {
        Tuple<EnumTargetTask, EnumTargetTask, Object> task = (Tuple<EnumTargetTask, EnumTargetTask, Object>) arg;

        //Debug.debug("masterTask " + task);
        if (task.getV2().isIn(EnumTargetTask.WINDOWS)){
            this.windows.doTask(o, task);
        }
    }
}

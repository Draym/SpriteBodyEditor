package com.andres_k.components.graphicComponents.graphic.windowGame;

import com.andres_k.components.gameComponents.animations.AnimatorOverlayData;
import com.andres_k.components.gameComponents.controllers.GameController;
import com.andres_k.components.graphicComponents.graphic.WindowBasedGame;
import com.andres_k.components.graphicComponents.input.EnumInput;
import com.andres_k.components.graphicComponents.input.InputData;
import com.andres_k.components.graphicComponents.userInterface.overlay.windowOverlay.GameOverlay;
import com.andres_k.components.taskComponent.GenericSendTask;
import com.andres_k.utils.tools.Debug;
import org.codehaus.jettison.json.JSONException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by andres_k on 08/07/2015.
 */
public class WindowGame extends WindowBasedGame {

    public WindowGame(int idWindow, GenericSendTask interfaceTask) throws JSONException {
        Debug.debug("constructor");
        this.idWindow = idWindow;

        this.animatorOverlay = new AnimatorOverlayData();

        this.controller = new GameController();
        interfaceTask.addObserver(this.controller);
        this.controller.addObserver(interfaceTask);

        InputData inputData = new InputData("configInput.json");
        this.overlay = new GameOverlay(inputData);
        interfaceTask.addObserver(this.overlay);
        this.overlay.addObserver(interfaceTask);
        Debug.debug("end constructor Interface");
    }

    @Override
    public int getID() {
        return this.idWindow;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        Debug.debug("init GAME");
        this.container = gameContainer;
        this.stateWindow = stateBasedGame;

        this.animatorOverlay.init();

        this.controller.setStateWindow(this.stateWindow);
        this.controller.setWindow(this);

        this.controller.init();
        this.overlay.initElementsComponent(this.animatorOverlay);
        Debug.debug("end init GAME");
    }


    @Override
    public void enter(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        Debug.debug("enter GAME");

        this.container.setTargetFrameRate(60);
        this.container.setShowFPS(false);
        this.container.setAlwaysRender(false);
        this.container.setVSync(false);

        Debug.debug("end enter GAME");
    }


    @Override
    public void leave(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        Debug.debug("LEAVE GAME");
        this.clean();
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
//        Debug.debug("draw");
        this.controller.renderWindow(graphics);
        this.overlay.draw(graphics);
        //      Debug.debug("end draw");
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        //    Debug.debug("update");
        this.controller.updateWindow();
        this.overlay.updateOverlay();
        //  Debug.debug("end update");
    }

    @Override
    public void keyPressed(int key, char c) {
        boolean absorbed = this.overlay.event(key, c, EnumInput.PRESSED);
    }

    @Override
    public void keyReleased(int key, char c) {
        boolean absorbed = this.overlay.event(key, c, EnumInput.RELEASED);
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        if (!this.overlay.isOnFocus(x, y)) {
        }
    }

    @Override
    public void quit() {
        this.clean();
        this.container.exit();
    }

    @Override
    public void clean(){
        this.overlay.leave();
    }

}

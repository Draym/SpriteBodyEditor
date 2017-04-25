package com.andres_k.components.graphicComponents.graphic.windowInterface;

import com.andres_k.components.gameComponents.animations.AnimatorOverlayData;
import com.andres_k.components.gameComponents.controllers.InterfaceController;
import com.andres_k.components.graphicComponents.graphic.WindowBasedGame;
import com.andres_k.components.graphicComponents.input.EnumInput;
import com.andres_k.components.graphicComponents.userInterface.overlay.windowOverlay.InterfaceOverlay;
import com.andres_k.components.taskComponent.GenericSendTask;
import com.andres_k.utils.configs.WindowConfig;
import com.andres_k.utils.tools.Console;
import org.codehaus.jettison.json.JSONException;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by andres_k on 08/07/2015.
 */
public class WindowInterface extends WindowBasedGame {

    private Image background;
    private Image title;

    public WindowInterface(int idWindow, GenericSendTask interfaceTask) throws JSONException, SlickException {
        this.idWindow = idWindow;

        this.animatorOverlay = new AnimatorOverlayData();

        this.controller = new InterfaceController();
        interfaceTask.addObserver(this.controller);
        this.controller.addObserver(interfaceTask);

        this.overlay = new InterfaceOverlay();
        interfaceTask.addObserver(this.overlay);
        this.overlay.addObserver(interfaceTask);
    }

    @Override
    public int getID() {
        return this.idWindow;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.container = gameContainer;
        this.stateWindow = stateBasedGame;

        this.animatorOverlay.init();


        this.overlay.initElementsComponent(this.animatorOverlay);

        this.controller.setStateWindow(this.stateWindow);
        this.controller.setWindow(this);
        this.controller.init();
        Console.debug("end init Interface");

        this.background = new Image("image/background.png");
        this.title = new Image("image/title.png");
    }


    @Override
    public void enter(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.container.setTargetFrameRate(60);
        this.container.setShowFPS(false);
        this.container.setAlwaysRender(false);
        this.container.setVSync(false);

        this.overlay.enter();
        this.controller.enter();
    }


    @Override
    public void leave(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.controller.leave();
        this.clean();
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, WindowConfig.getSizeX(), WindowConfig.getSizeY());
        graphics.drawImage(this.background, 0, 0);
        graphics.drawImage(this.title, 50, 50);

        this.controller.renderWindow(graphics);
        this.overlay.draw(graphics);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        this.controller.updateWindow(gameContainer);
        this.overlay.updateOverlay();
    }

    @Override
    public void keyPressed(int key, char c) {
        boolean absorbed = this.overlay.event(key, c, EnumInput.PRESSED);
        if (!absorbed) {
            this.controller.keyPressed(key, c);
        }
    }

    @Override
    public void keyReleased(int key, char c) {
        boolean absorbed = this.overlay.event(key, c, EnumInput.RELEASED);
        if (!absorbed) {
            this.controller.keyReleased(key, c);
        }
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        this.controller.mousePressed(button, x, y);
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        if (!this.overlay.isOnFocus(x, y)) {
            this.controller.mouseReleased(button, x, y);
        }
    }

    @Override
    public void quit() {
        this.clean();
        this.container.exit();
    }

    @Override
    public void clean() {
        this.overlay.leave();
    }
}

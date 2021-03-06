package com.andres_k.components.gameComponents.controllers;

import com.andres_k.components.gameComponents.gameObject.BodyCreator;
import com.andres_k.components.gameComponents.gameObject.BodyRect;
import com.andres_k.components.gameComponents.gameObject.BodySprite;
import com.andres_k.components.gameComponents.gameObject.EnumGameObject;
import com.andres_k.components.graphicComponents.graphic.EnumWindow;
import com.andres_k.components.graphicComponents.input.EnumInput;
import com.andres_k.components.graphicComponents.input.InputGame;
import com.andres_k.components.graphicComponents.userInterface.overlay.EnumOverlayElement;
import com.andres_k.components.networkComponents.messages.MessageFileLoad;
import com.andres_k.components.networkComponents.messages.MessageFileNew;
import com.andres_k.components.taskComponent.EnumTargetTask;
import com.andres_k.components.taskComponent.TaskFactory;
import com.andres_k.utils.configs.GlobalVariable;
import com.andres_k.utils.stockage.Pair;
import com.andres_k.utils.stockage.Tuple;
import com.andres_k.utils.tools.FilesTools;
import com.andres_k.utils.tools.MathTools;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;

import java.io.File;
import java.util.List;
import java.util.Observable;

/**
 * Created by andres_k on 08/07/2015.
 */
public class GameController extends WindowController {
    private BodyCreator bodyCreator;
    private BodySprite bodyFocused;
    private List<BodyRect> bodyCopied;
    private boolean drawBodyCopied;
    private InputGame inputGame;
    private Pair<Float, Float> startClick;
    private Pair<Float, Float> endClick;

    private float upOriginX;
    private float upOriginY;
    private float multDecal;
    private EnumGameObject current;

    public GameController() throws JSONException {
        this.bodyCreator = null;
        this.bodyFocused = null;
        this.startClick = null;
        this.endClick = null;
        this.inputGame = new InputGame();
        this.upOriginX = 0;
        this.upOriginY = 0;
        this.multDecal = 1;
        this.current = EnumGameObject.RECTANGLE;
    }

    @Override
    public void enter() {
        GlobalVariable.init();
    }

    @Override
    public void leave() {
        if (this.bodyCreator != null) {
            this.bodyCreator.saveInFile();
        }
    }

    @Override
    public void init() {
        this.bodyCopied = null;
        this.drawBodyCopied = false;
    }

    @Override
    public void renderWindow(Graphics g) {
        if (this.bodyCreator != null)
            this.bodyCreator.draw(g);
        if (this.startClick != null && this.endClick != null) {
            g.setColor(Color.pink);
            if (this.current == EnumGameObject.RECTANGLE) {
                g.draw(MathTools.createRectangle(this.startClick, this.endClick));
            } else if (this.current == EnumGameObject.CIRCLE) {
                g.draw(MathTools.createCircle(this.startClick, this.endClick));
            } else if (this.current == EnumGameObject.BODY) {
                g.draw(MathTools.createRectangle(this.startClick, this.endClick));
            }
        }
        if (this.bodyCopied != null && this.drawBodyCopied && this.bodyCopied.size() == 1) {
            g.setColor(Color.red);
            for (BodyRect item : this.bodyCopied) {
                g.draw(new Rectangle(GlobalVariable.mouseX, GlobalVariable.mouseY, item.getSizes().getV1() * GlobalVariable.zoom, item.getSizes().getV2() * GlobalVariable.zoom));
            }
        }
    }

    @Override
    public void updateWindow(GameContainer gameContainer) {
        Input input = gameContainer.getInput();

        GlobalVariable.mouseX = input.getMouseX();
        GlobalVariable.mouseY = input.getMouseY();

        this.updateSliding();
        if (this.bodyCreator != null)
            this.bodyCreator.update();
        if (this.startClick != null) {
            if (this.endClick == null) {
                this.endClick = new Pair<>((float) GlobalVariable.mouseX, (float) GlobalVariable.mouseY);
            } else {
                this.endClick.setV1((float) GlobalVariable.mouseX);
                this.endClick.setV2((float) GlobalVariable.mouseY);
            }
        }
    }

    public void updateSliding() {
        if (this.upOriginX != 0 && this.upOriginY != 0)
            this.multDecal += 0.1;
        GlobalVariable.originX += ((this.upOriginX * this.multDecal) / GlobalVariable.zoom);
        GlobalVariable.originY += ((this.upOriginY * this.multDecal) / GlobalVariable.zoom);
    }

    @Override
    public void keyPressed(int key, char c) {
        EnumInput result = this.inputGame.checkInput(key, EnumInput.RELEASED);

        if (result == EnumInput.MOVE_UP) {
            this.upOriginX = 0;
            this.upOriginY = -5 * GlobalVariable.zoom;
        } else if (result == EnumInput.MOVE_DOWN) {
            this.upOriginX = 0;
            this.upOriginY = 5 * GlobalVariable.zoom;
        } else if (result == EnumInput.MOVE_RIGHT) {
            this.upOriginX = 5 * GlobalVariable.zoom;
            this.upOriginY = 0;
        } else if (result == EnumInput.MOVE_LEFT) {
            this.upOriginX = -5 * GlobalVariable.zoom;
            this.upOriginY = 0;
        } else if (result == EnumInput.PASTE) {
            this.bodyCopied = this.bodyCreator.getBodyRectCopy();
            this.drawBodyCopied = true;
        }
    }

    @Override
    public void keyReleased(int key, char c) {
        if (this.bodyCreator != null) {
            EnumInput result = this.inputGame.checkInput(key, EnumInput.RELEASED);

            EnumGameObject type = null;

            if (result == EnumInput.DELETE) {
                type = EnumGameObject.NULL;
            } else if (result == EnumInput.ATTACK) {
                type = EnumGameObject.ATTACK_BODY;
            } else if (result == EnumInput.DEFENSE) {
                type = EnumGameObject.DEFENSE_BODY;
            } else if (result == EnumInput.BLOCK) {
                type = EnumGameObject.BLOCK_BODY;
            } else if (result == EnumInput.MOVE_UP || result == EnumInput.MOVE_DOWN || result == EnumInput.MOVE_RIGHT || result == EnumInput.MOVE_LEFT) {
                this.upOriginX = 0;
                this.upOriginY = 0;
                this.multDecal = 1;
            } else if (result == EnumInput.RECTANGLE) {
                this.current = EnumGameObject.RECTANGLE;
            } else if (result == EnumInput.CIRCLE) {
                this.current = EnumGameObject.CIRCLE;
            } else if (result == EnumInput.BODY) {
                this.current = EnumGameObject.BODY;
            } else if (result == EnumInput.COPY) {
                this.bodyCreator.copyBodyRect();
            } else if (result == EnumInput.PASTE) {
                this.bodyCreator.pasteBodyRect(this.bodyFocused);
                this.bodyCopied = null;
                this.drawBodyCopied = false;
            } else if (result == EnumInput.LINK) {
                this.bodyCreator.addLinkToFocusedRect();
            } else if (result == EnumInput.REMOVE_LINK) {
                this.bodyCreator.removeLinkToFocusedRect();
            }
            if (type != null) {
                this.bodyCreator.changeFocusedType(type);
            }
        }
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        if (this.bodyCreator != null)
            if (button == 0) {
                int tmpX = (int) ((x + GlobalVariable.originX) / GlobalVariable.zoom);
                int tmpY = (int) ((y + GlobalVariable.originY) / GlobalVariable.zoom);
                this.bodyFocused = this.bodyCreator.onClick(tmpX, tmpY);

                if (this.bodyFocused != null) {
                    this.startClick = new Pair<>((float) x, (float) y);
                }
            }
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        if (button == 0 && this.bodyFocused != null && this.startClick != null && this.endClick != null) {
            this.startClick.setV1((this.startClick.getV1() + GlobalVariable.originX) / GlobalVariable.zoom);
            this.startClick.setV2((this.startClick.getV2() + GlobalVariable.originY) / GlobalVariable.zoom);
            this.endClick.setV1((this.endClick.getV1() + GlobalVariable.originX) / GlobalVariable.zoom);
            this.endClick.setV2((this.endClick.getV2() + GlobalVariable.originY) / GlobalVariable.zoom);

            if (this.current == EnumGameObject.RECTANGLE) {
                this.bodyFocused.addRect(MathTools.createRectangle(this.startClick, this.endClick), EnumGameObject.DEFENSE_BODY);
            } else if (this.current == EnumGameObject.CIRCLE) {
                this.bodyFocused.addCircle(MathTools.createCircle(this.startClick, this.endClick), EnumGameObject.DEFENSE_BODY);
            } else if (this.current == EnumGameObject.BODY) {
                this.bodyFocused.changeBody(MathTools.createRectangle(this.startClick, this.endClick));
            }
            this.startClick = null;
            this.endClick = null;
        }
    }

    @Override
    public void mouseWheelMove(int newValue) {
        if (newValue > 0) {
            GlobalVariable.zoom += 0.15;
        } else if (GlobalVariable.zoom > 0.1) {
            GlobalVariable.zoom -= 0.15;
        }
    }

    public boolean loadJsonFile(String path) throws SlickException, JSONException {
        File file = new File(GlobalVariable.folder + path.substring(0, path.indexOf(".")) + ".json");

        if (file.exists() && !file.isDirectory()) {
            this.bodyCreator = new BodyCreator(new JSONObject(FilesTools.readFile(GlobalVariable.folder + path.substring(0, path.indexOf(".")) + ".json")));
            return true;
        }
        return false;
    }

    public void createNewJsonFile(String path, float sizeX, float sizeY) throws SlickException, JSONException {
        if (!this.loadJsonFile(path)) {
            this.bodyCreator = new BodyCreator(path, new Image(GlobalVariable.folder + path), sizeX, sizeY);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Tuple) {
            Tuple<EnumTargetTask, EnumTargetTask, Object> received = (Tuple<EnumTargetTask, EnumTargetTask, Object>) arg;
            if (received.getV1().equals(EnumTargetTask.WINDOWS) && received.getV2().equals(EnumTargetTask.GAME)) {
                if (received.getV3() instanceof EnumWindow) {
                    this.stateWindow.enterState(((EnumWindow) received.getV3()).getValue());
                } else if (received.getV3() instanceof MessageFileNew) {
                    List<Object> objects = ((MessageFileNew) received.getV3()).getObjects();
                    try {
                        if (objects.size() >= 3) {
                            this.createNewJsonFile((String) objects.get(0), Float.valueOf((String) objects.get(1)), Float.valueOf((String) objects.get(2)));
                            this.stateWindow.enterState(EnumWindow.GAME.getValue());
                            this.setChanged();
                            this.notifyObservers(TaskFactory.createTask(EnumTargetTask.GAME, EnumTargetTask.INTERFACE, new MessageFileLoad("admin", "admin", (String) objects.get(0))));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        this.bodyCreator = null;
                        this.window.quit();
                    }
                } else if (received.getV3() instanceof MessageFileLoad) {
                    try {
                        this.loadJsonFile(((MessageFileLoad) received.getV3()).getPath());
                        this.stateWindow.enterState(EnumWindow.GAME.getValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                        this.bodyCreator = null;
                        this.window.quit();
                    }
                } else if (received.getV3() instanceof EnumOverlayElement) {
                    if (received.getV3() == EnumOverlayElement.EXIT) {
                        this.window.quit();
                    } else if (received.getV3() == EnumOverlayElement.SAVE) {
                        this.bodyCreator.saveInFile();
                    }
                }
            }
        }
    }
}

package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.ProgressBar;
import com.simsilica.lemur.style.BaseStyles;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import mygame.JSONLoader;
import org.json.simple.JSONObject;

/**
 *
 * @author misat11
 */
public class Main extends SimpleApplication {

    public Container mainmenu;
    public Container pausemenu;
    public Container settingsmenu;
    public Container exitdialog;
    public Container multiplayermenu;
    public Container progressbar;
    public ProgressBar progress;
    public int key_up = KeyInput.KEY_W;
    public int key_down = KeyInput.KEY_S;
    public int key_left = KeyInput.KEY_A;
    public int key_right = KeyInput.KEY_D;

    public String state = "loading";
    public JSONObject actualMap;
    public HashMap<String, Spatial> mapSpatials;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        state = "mainmenu";

        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);
        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_ESCAPE));
        inputManager.addMapping("Up", new KeyTrigger(key_up));
        inputManager.addMapping("Down", new KeyTrigger(key_down));
        inputManager.addMapping("Left", new KeyTrigger(key_left));
        inputManager.addMapping("Right", new KeyTrigger(key_right));
        inputManager.addListener(actionListener, "Pause");
        inputManager.addListener(actionListener, "Up");
        inputManager.addListener(actionListener, "Down");
        inputManager.addListener(actionListener, "Left");
        inputManager.addListener(actionListener, "Right");

        getInputManager().setCursorVisible(true);

        flyCam.setEnabled(false);

        GuiGlobals.initialize(this);

        BaseStyles.loadGlassStyle();

        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");

        mainmenu = new Container();
        pausemenu = new Container();
        exitdialog = new Container();
        settingsmenu = new Container();
        multiplayermenu = new Container();
        progressbar = new Container();

        guiNode.attachChild(mainmenu);

        // MAIN MENU
        mainmenu.setLocalTranslation(300, 300, 0);
        mainmenu.addChild(new Label("Main Menu"));

        Button mainmenu_singleplayer = mainmenu.addChild(new Button("Play Singleplayer"));
        mainmenu_singleplayer.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {
                if (state.equals("mainmenu")) {
                    guiNode.detachChild(mainmenu);
                    boolean map = loadMap("TestMap", "Models/womanmodel.j3o");
                    if (map == true) {
                        
                    } else {
                        state = "mainmenu";
                        guiNode.attachChild(mainmenu);
                    }
                }
            }
        });

        Button mainmenu_multiplayer = mainmenu.addChild(new Button("Play Multiplayer"));
        mainmenu_multiplayer.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {

            }
        });

        Button mainmenu_settings = mainmenu.addChild(new Button("Settings"));
        mainmenu_settings.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {

            }
        });

        Button mainmenu_exit = mainmenu.addChild(new Button("Exit"));
        mainmenu_exit.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {
                state = "exitdialog";
                guiNode.detachChild(mainmenu);
                guiNode.attachChild(exitdialog);
            }
        });

        // PAUSE MENU
        pausemenu.setLocalTranslation(300, 300, 0);
        pausemenu.addChild(new Label("Pause Menu"));

        Button pausemenu_backtogame = pausemenu.addChild(new Button("Back to game"));
        pausemenu_backtogame.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {
                if (state.equals("ingame_pause")) {
                    guiNode.detachChild(pausemenu);
                    flyCam.setEnabled(true);
                    state = "ingame_pause";
                    getInputManager().setCursorVisible(false);
                }
            }
        });

        Button pausemenu_settings = pausemenu.addChild(new Button("Settings"));
        pausemenu_settings.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {

            }
        });

        Button pausemenu_exit = pausemenu.addChild(new Button("Exit"));
        pausemenu_exit.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {
                if (state.equals("ingame_pause")) {
                    guiNode.detachChild(pausemenu);
                    boolean u = unloadMap();
                    if(u == true){
                    
                    }
                }
            }
        });

        // EXIT DIALOG
        exitdialog.setLocalTranslation(300, 300, 0);
        exitdialog.addChild(new Label("Do you really want to leave this game?"));

        Button exitdialog_yes = exitdialog.addChild(new Button("YES"));
        exitdialog_yes.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {
                if (state.equals("exitdialog")) {
                    stop();
                }
            }
        });

        Button exitdialog_no = exitdialog.addChild(new Button("NO"));
        exitdialog_yes.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {
                if (state.equals("exitdialog")) {
                    guiNode.detachChild(exitdialog);
                    guiNode.attachChild(mainmenu);
                    state = "mainmenu";
                }
            }
        });

        //PROGRESS BAR
        progressbar.setLocalTranslation(300, 300, 0);
        progress = progressbar.addChild(new ProgressBar("Loading.."));
        progress.setProgressPercent(0);
    }

    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Pause") && !keyPressed) {
                if (state.equals("ingame")) {
                    guiNode.attachChild(pausemenu);
                    flyCam.setEnabled(false);
                    state = "ingame_pause";
                    getInputManager().setCursorVisible(true);
                } else if (state.equals("ingame_pause")) {
                    guiNode.detachChild(pausemenu);
                    flyCam.setEnabled(true);
                    state = "ingame";
                    getInputManager().setCursorVisible(false);
                } else if (state.equals("exitdialog")) {
                    guiNode.detachChild(exitdialog);
                    guiNode.attachChild(mainmenu);
                    state = "mainmenu";
                } else if (state.equals("mainmenu")) {
                    guiNode.detachChild(mainmenu);
                    guiNode.attachChild(exitdialog);
                    state = "exitdialog";
                }
            } else if (name.equals("Up") && !keyPressed) {
                if (state.equals("ingame")) {

                }
            } else if (name.equals("Down") && !keyPressed) {
                if (state.equals("ingame")) {

                }
            } else if (name.equals("Left") && !keyPressed) {
                if (state.equals("ingame")) {

                }
            } else if (name.equals("Right") && !keyPressed) {
                if (state.equals("ingame")) {

                }
            }
        }
    };

    public boolean loadMap(String map, String pmodel) {
        guiNode.attachChild(progressbar);
        progress.setProgressPercent(0.0);
        progress.setMessage("Loading Base File...");
        System.out.println();
        JSONObject obj = JSONLoader.main("assets/Maps/" + map + ".json");
        if (obj == null) {
            progress.setMessage("File not found or not valid...");
            progress.setProgressPercent(0.0);
            return false;
        }

        progress.setProgressPercent(0.1);
        progress.setMessage("Validating File...");
        String name = obj.get("name").toString();
        if (name == null) {
            progress.setMessage("File not valid...");
            progress.setProgressPercent(0.0);
            return false;
        }
        String author = obj.get("author").toString();
        if (author == null) {
            progress.setMessage("File not valid...");
            progress.setProgressPercent(0.0);
            return false;
        }
        String terrain = obj.get("terrain").toString();
        if (terrain == null) {
            progress.setMessage("File not valid...");
            progress.setProgressPercent(0.0);
            return false;
        }

        progress.setProgressPercent(0.3);
        progress.setMessage("Loading terrain...");
        mapSpatials.put("terrain_" + map, assetManager.loadModel(terrain));
        if (mapSpatials.get("terrain_" + map) == null) {
            progress.setMessage("Terrain not found or not valid...");
            progress.setProgressPercent(0.0);
            return false;
        }
        progress.setProgressPercent(0.6);
        progress.setMessage("Loading player model...");
        mapSpatials.put("me_playermodel", assetManager.loadModel(pmodel));
        if (mapSpatials.get("me_playermodel") == null) {
            progress.setMessage("Player model not found or not valid...");
            progress.setProgressPercent(0.0);
            return false;
        }

        progress.setProgressPercent(0.8);
        progress.setMessage("Saving actual settings to memory...");
        actualMap = obj;

        cam.normalize();
        
        progress.setProgressPercent(0.9);
        progress.setMessage("Spawning objects...");

        rootNode.attachChild(mapSpatials.get("terrain_" + map));
        rootNode.attachChild(mapSpatials.get("me_playermodel"));

        progress.setProgressPercent(1.0);
        progress.setMessage("All completed. Enjoy game");
        getInputManager().setCursorVisible(false);
        flyCam.setEnabled(true);
        state = "ingame";
        guiNode.detachChild(progressbar);

        return true;
    }

    public boolean unloadMap() {
        guiNode.attachChild(progressbar);
        getInputManager().setCursorVisible(true);
        flyCam.setEnabled(false);

        progress.setProgressPercent(1.0);
        progress.setMessage("Unloading map");
        if (actualMap == null) {
            progress.setProgressPercent(0.0);
            progress.setMessage("Nothing to unload");
            return true;
        }
        
        progress.setProgressPercent(0.7);
        progress.setMessage("Deleting models");
        for(Map.Entry<String, Spatial> entry : mapSpatials.entrySet()){
            Spatial value = entry.getValue();
            rootNode.detachChild(value);
        }
        
        progress.setProgressPercent(0.3);
        progress.setMessage("Clearing memory");
        mapSpatials.clear();
        actualMap.clear();
        
        progress.setProgressPercent(0.0);
        progress.setMessage("Game unloaded");
        
        guiNode.detachChild(progressbar);
        guiNode.attachChild(mainmenu);
        state = "mainmenu";
        
        return true;
    }
}

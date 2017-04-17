package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.Network;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitorAdapter;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.ProgressBar;
import com.simsilica.lemur.TextField;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.style.BaseStyles;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.JSONLoader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author misat11
 */
public class Main extends SimpleApplication implements ClientStateListener {

    public static Main instance;

    public Container mainmenu;
    public Container pausemenu;
    public Container pausemenu_multiplayer;
    public Container settingsmenu;
    public Container exitdialog;
    public Container multiplayermenu;
    public Container chatw;
    public Container progressbar;
    public Container chati;
    public Container statbar;
    public TextField chatii;
    public ProgressBar progress;
    public ProgressBar health;
    public int key_up = KeyInput.KEY_W;
    public int key_down = KeyInput.KEY_S;
    public int key_left = KeyInput.KEY_A;
    public int key_right = KeyInput.KEY_D;
    public int key_enter = KeyInput.KEY_EQUALS;
    public int key_chat = KeyInput.KEY_T;
    public int key_jump = KeyInput.KEY_SPACE;

    public TextField port;
    public TextField ip;
    public TextField name;

    public String state = "loading";
    public JSONObject actualMap;
    public HashMap<String, Spatial> mapSpatials;
    public BulletAppState bulletAppState;
    public ChaseCamera chasecam;
    public boolean left = false, right = false, up = false, down = false;
    public CharacterControl character;
    public Vector3f walkDirection = new Vector3f(0, 0, 0);
    public float airTime = 0;
    public RigidBodyControl terrainPhysicsNode;

    public Client client;

    public boolean chatopen = false;

    public PlayerData my_playerdata;

    public ServerDataManager dataManager;

    public ServerInfoMessage serverInfoMessage;

    public String gameversion = "CrowbarWars Multiplayer 0.1 beta";
    public String gamehashcode = "crbar_multi";
    public int protocol = 1;

    public List<AnimControl> animControls;
    public List<AnimChannel> animChannels;

    public float tpf = 0;

    public static void main(String[] args) {
        Utils.initSerializer();

        Main app = new Main();
        instance = app;
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
        inputManager.addMapping("Enter", new KeyTrigger(key_enter));
        inputManager.addMapping("Jump", new KeyTrigger(key_jump));
        inputManager.addMapping("Chat", new KeyTrigger(key_chat));
        inputManager.addListener(actionListener, "Pause");
        inputManager.addListener(actionListener, "Up");
        inputManager.addListener(actionListener, "Down");
        inputManager.addListener(actionListener, "Left");
        inputManager.addListener(actionListener, "Right");
        inputManager.addListener(actionListener, "Enter");
        inputManager.addListener(actionListener, "Jump");
        inputManager.addListener(actionListener, "Chat");

        getInputManager().setCursorVisible(true);

        flyCam.setEnabled(false);

        GuiGlobals.initialize(this);

        BaseStyles.loadGlassStyle();

        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");

        mainmenu = new Container();
        pausemenu = new Container();
        pausemenu_multiplayer = new Container();
        exitdialog = new Container();
        settingsmenu = new Container();
        multiplayermenu = new Container();
        progressbar = new Container();
        chati = new Container();
        chatw = new Container(new SpringGridLayout(Axis.Y, Axis.X, FillMode.Last, FillMode.Last));
        statbar =  new Container();
        mapSpatials = new HashMap<String, Spatial>();
        bulletAppState = new BulletAppState();

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
                if (state.equals("mainmenu")) {
                    guiNode.detachChild(mainmenu);
                    guiNode.attachChild(multiplayermenu);
                    state = "multiplayerconnectmenu";
                }
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
                    state = "ingame";
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
                    if (u == true) {

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

        //CHAT
        chatw.setLocalTranslation(10, 300, 0);

        chatw.setPreferredSize(new Vector3f(300, 200, 0));

        chati.setLocalTranslation(10, 80, 0);
        chatii = chati.addChild(new TextField(""));
        chati.setPreferredSize(new Vector3f(300, 40, 0));
        Button sendbutton = chati.addChild(new Button("Send"));
        sendbutton.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {
                String text = chatii.getText();
                if (text.equals("") == false) {
                    client.send(new TextMessage(text));
                    chatii.setText("");
                    if (state.equals("inmultigame")) {
                        guiNode.detachChild(chati);
                        chatw.setAlpha(0.4f);
                        chatopen = false;
                    }
                }
            }
        });
        chatw.setAlpha(0.4f);

        //MULTIPLAYER MENU
        multiplayermenu.setLocalTranslation(300, 300, 0);
        multiplayermenu.addChild(new Label("Multiplayer"));
        ip = multiplayermenu.addChild(new TextField(""));
        port = multiplayermenu.addChild(new TextField(Integer.toString(Utils.BASE_PORT)));
        name = multiplayermenu.addChild(new TextField(""));
        Button backtomainfrommulti = multiplayermenu.addChild(new Button("Back"));
        backtomainfrommulti.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {
                if (state.equals("multiplayerconnectmenu")) {
                    guiNode.detachChild(multiplayermenu);
                    guiNode.attachChild(mainmenu);
                    state = "mainmenu";
                }
            }
        });
        Button connect = multiplayermenu.addChild(new Button("Connect"));
        connect.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {
                if (state.equals("multiplayerconnectmenu")) {
                    guiNode.detachChild(multiplayermenu);
                    String i = ip.getText();
                    String n = name.getText();
                    int p = Integer.parseInt(port.getText());
                    initConnection(i, p, n);
                    state = "inmultigame";
                }
            }
        });

        // PAUSE MULTIPLAYER MENU
        pausemenu_multiplayer.setLocalTranslation(300, 300, 0);
        pausemenu_multiplayer.addChild(new Label("Pause Menu"));

        Button pausemenu_multiplayer_backtogame = pausemenu_multiplayer.addChild(new Button("Back to game"));
        pausemenu_multiplayer_backtogame.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {
                if (state.equals("inmultigame_pause")) {
                    chatw.setAlpha(0.4f);
                    guiNode.detachChild(chati);
                    guiNode.detachChild(pausemenu_multiplayer);
                    state = "inmultigame";
                    getInputManager().setCursorVisible(false);
                }
            }
        });

        Button pausemenu_multiplayer_settings = pausemenu_multiplayer.addChild(new Button("Settings"));
        pausemenu_multiplayer_settings.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {

            }
        });

        Button pausemenu_multiplayer_exit = pausemenu_multiplayer.addChild(new Button("Exit"));
        pausemenu_multiplayer_exit.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {
                if (state.equals("inmultigame_pause")) {
                    guiNode.detachChild(pausemenu_multiplayer);
                    client.close();
                    state = "mainmenu";
                    guiNode.attachChild(mainmenu);
                }
            }
        });

        //STAT BAR
        statbar.setLocalTranslation(450, 35, 0);

        statbar.setPreferredSize(new Vector3f(80, 25, 0));
        health = statbar.addChild(new ProgressBar());
        health.setPreferredSize(new Vector3f(80, 25, 0));
        health.setMessage("Health");
        health.setProgressPercent(1);
        
    }

    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Pause") && !keyPressed) {
                if (state.equals("ingame")) {
                    guiNode.attachChild(pausemenu);
                    state = "ingame_pause";
                    getInputManager().setCursorVisible(true);
                } else if (state.equals("inmultigame")) {
                    if (chatopen == true) {
                        chatw.setAlpha(0.4f);
                        guiNode.detachChild(chati);
                        getInputManager().setCursorVisible(false);
                        chatopen = false;
                    } else {
                        guiNode.attachChild(pausemenu_multiplayer);
                        state = "inmultigame_pause";
                        chatw.setAlpha(1f);
                        guiNode.attachChild(chati);
                        getInputManager().setCursorVisible(true);
                    }
                } else if (state.equals("ingame_pause")) {
                    guiNode.detachChild(pausemenu);
                    state = "ingame";
                    getInputManager().setCursorVisible(false);
                } else if (state.equals("inmultigame_pause")) {

                    chatw.setAlpha(0.4f);
                    guiNode.detachChild(chati);
                    guiNode.detachChild(pausemenu_multiplayer);
                    state = "inmultigame";
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
            }
            if (name.equals("Up")) {
                if (state.equals("ingame") || (state.equals("inmultigame") && chatopen == false)) {
                    if (keyPressed) {
                        up = true;
                    } else {
                        up = false;
                    }
                }
            }
            if (name.equals("Down")) {
                if (state.equals("ingame") || (state.equals("inmultigame") && chatopen == false)) {
                    if (keyPressed) {
                        down = true;
                    } else {
                        down = false;
                    }
                }
            }
            if (name.equals("Left")) {
                if (state.equals("ingame") || (state.equals("inmultigame") && chatopen == false)) {
                    if (keyPressed) {
                        left = true;
                    } else {
                        left = false;
                    }
                }
            }
            if (name.equals("Right")) {
                if (state.equals("ingame") || (state.equals("inmultigame") && chatopen == false)) {
                    if (keyPressed) {
                        right = true;
                    } else {
                        right = false;
                    }
                }
            }
            if (name.equals("Jump")) {
                if (state.equals("ingame") || (state.equals("inmultigame") && chatopen == false)) {
                    character.jump();
                }
            }
            if (name.equals("Enter")) {
                if (state.equals("inmultigame") && chatopen == true) {
                    String text = chatii.getText();
                    if (text.equals("") == false) {
                        client.send(new TextMessage(text));
                        chatii.setText("");
                    }
                }
            }
            if (name.equals("Chat")) {
                if (state.equals("inmultigame") && chatopen == false) {
                    chatw.setAlpha(1f);
                    guiNode.attachChild(chati);
                    chatopen = true;
                }
            }
        }
    };

    public boolean loadMap(String map, String pmodel) {
        guiNode.attachChild(progressbar);
        progress.setProgressPercent(0.0);
        progress.setMessage("Loading Base File...");
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
            progress.setMessage("File not valid..");
            progress.setProgressPercent(0.0);
            return false;
        }

        progress.setProgressPercent(0.3);
        progress.setMessage("Loading terrain...");
        mapSpatials.put("terrain", assetManager.loadModel(terrain));
        if (mapSpatials.get("terrain") == null) {
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

        stateManager.attach(bulletAppState);

        progress.setProgressPercent(0.8);
        progress.setMessage("Saving actual settings to memory...");
        actualMap = obj;

        progress.setProgressPercent(0.9);
        progress.setMessage("Spawning objects...");

        boolean isSpecial = false;
        if (obj.get("special") != null) {
            isSpecial = true;
        }

        terrainPhysicsNode = new RigidBodyControl(0f);
        mapSpatials.get("terrain").addControl(terrainPhysicsNode);

        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(0.6f, 2f);
        character = new CharacterControl(capsuleShape, 0.09f);
        rootNode.attachChild(mapSpatials.get("terrain"));
        rootNode.attachChild(mapSpatials.get("me_playermodel"));

        mapSpatials.get("me_playermodel").center();

        mapSpatials.get("me_playermodel").addControl(character);
        bulletAppState.getPhysicsSpace().add(character);
        bulletAppState.getPhysicsSpace().add(terrainPhysicsNode);
        character.warp(new Vector3f(0.0f, 60f, 0.0f));
        character.setGravity(40f);
        character.setJumpSpeed(15f);
        setAnimControls(mapSpatials.get("me_playermodel"));

        if (isSpecial == true) {
            JSONArray specialContent = (JSONArray) obj.get("special");
            Iterator i = specialContent.iterator();

            while (i.hasNext()) {
                try {
                    JSONObject special = (JSONObject) i.next();
                    String special_name = special.get("name").toString();
                    String special_type = special.get("type").toString();
                    float loc_x = Float.parseFloat(special.get("loc_x").toString());
                    float loc_y = Float.parseFloat(special.get("loc_y").toString());
                    float loc_z = Float.parseFloat(special.get("loc_z").toString());
                    Vector3f location = new Vector3f(loc_x, loc_y, loc_z);
                    if (special_type.equals("spatial")) {
                        System.out.println("Selecting and spawning spatial");
                        String modelAsset = special.get("modelAsset").toString();
                        Spatial model = assetManager.loadModel(modelAsset);
                        model.setLocalTranslation(loc_x, loc_y, loc_z);
                        mapSpatials.put("special_" + special_name, model);
                        rootNode.attachChild(mapSpatials.get("special_" + special_name));
                        bulletAppState.getPhysicsSpace().addAll(mapSpatials.get("special_" + special_name));
                    } else if (special_type.equals("geometry")) {
                        String geometry = special.get("geometry").toString();
                        String material = special.get("material").toString();
                        String texture = special.get("texture").toString();
                        String texture_type = special.get("texture_type").toString();
                        float gravity = Float.parseFloat(special.get("gravity").toString());
                        if (geometry.equals("box")) {
                            float geometry_x = Float.parseFloat(special.get("geometry_x").toString());
                            float geometry_y = Float.parseFloat(special.get("geometry_y").toString());
                            float geometry_z = Float.parseFloat(special.get("geometry_z").toString());
                            Box b = new Box(geometry_x, geometry_y, geometry_z);
                            Geometry geom = new Geometry("Box", b);
                            RigidBodyControl geom_physics = new RigidBodyControl(gravity);
                            geom.addControl(geom_physics);
                            Material mat = null;
                            try {
                                mat = new Material(assetManager, material);
                            } catch (Exception e) {
                                System.out.println(e);
                                mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                            }
                            try {
                                mat.setTexture(texture_type, assetManager.loadTexture(texture));
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                            geom.setLocalTranslation(loc_x, loc_y, loc_z);
                            geom.setMaterial(mat);
                            mapSpatials.put("special_" + special_name, geom);
                            rootNode.attachChild(mapSpatials.get("special_" + special_name));
                            bulletAppState.getPhysicsSpace().add(geom_physics);
                        } else if (geometry.equals("sphere")) {
                            float radius = Float.parseFloat(special.get("radius").toString());
                            int radialSamples = Integer.parseInt(special.get("radialSamples").toString());
                            int zSamples = Integer.parseInt(special.get("zSamples").toString());
                            Sphere s = new Sphere(zSamples, radialSamples, radius);
                            Geometry geom = new Geometry("Sphere", s);
                            RigidBodyControl geom_physics = new RigidBodyControl(gravity);
                            geom.addControl(geom_physics);
                            Material mat = null;
                            try {
                                mat = new Material(assetManager, material);
                            } catch (Exception e) {
                                System.out.println(e);
                                mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                            }
                            try {
                                mat.setTexture(texture_type, assetManager.loadTexture(texture));
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                            geom.setLocalTranslation(loc_x, loc_y, loc_z);
                            geom.setMaterial(mat);
                            mapSpatials.put("special_" + special_name, geom);
                            rootNode.attachChild(mapSpatials.get("special_" + special_name));
                            bulletAppState.getPhysicsSpace().add(geom_physics);
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

        }

        chasecam = new ChaseCamera(cam, mapSpatials.get("me_playermodel"), inputManager);

        progress.setProgressPercent(1.0);
        progress.setMessage("All completed. Enjoy game");
        getInputManager().setCursorVisible(false);
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
        for (Map.Entry<String, Spatial> entry : mapSpatials.entrySet()) {
            Spatial value = entry.getValue();
            rootNode.detachChild(value);
        }

        progress.setProgressPercent(0.3);
        progress.setMessage("Clearing memory");
        mapSpatials.clear();
        actualMap.clear();

        character.destroy();

        stateManager.detach(bulletAppState);

        bulletAppState.cleanup();

        progress.setProgressPercent(0.0);
        progress.setMessage("Game unloaded");

        guiNode.detachChild(progressbar);
        guiNode.attachChild(mainmenu);
        state = "mainmenu";

        return true;
    }

    @Override
    public void simpleUpdate(float tpf) {
        this.tpf = tpf;
        if (state.equals("inmultigame") || state.equals("inmultigame_pause")) {
            if (client.isConnected()) {
                client.send(new PlayerDataMessage(my_playerdata));
            }
        }
        if (state.equals("inmultigame") || state.equals("inmultigame_pause")) {
            Vector3f camDir = cam.getDirection().clone();
            Vector3f camLeft = cam.getLeft().clone();
            camDir.y = 0;
            camLeft.y = 0;
            camDir.normalizeLocal();
            camLeft.normalizeLocal();
            walkDirection.set(0, 0, 0);

            if (left) {
                walkDirection.addLocal(camLeft);
            }
            if (right) {
                walkDirection.addLocal(camLeft.negate());
            }
            if (up) {
                walkDirection.addLocal(camDir);
            }
            if (down) {
                walkDirection.addLocal(camDir.negate());
            }

            if (!character.onGround()) {
                airTime += tpf;
            } else {
                airTime = 0;
            }

            if (walkDirection.lengthSquared() == 0) {
                setAnimation("Stand");
            } else {
                character.setViewDirection(walkDirection);
                if (airTime > .3f) {
                    setAnimation("Stand");
                } else {
                    setAnimation("Walk");
                }
            }

            walkDirection.multLocal(25f).multLocal(tpf);
            character.setWalkDirection(walkDirection);
            my_playerdata.setWalkDirection(walkDirection);
            my_playerdata.setLocation(character.getPhysicsLocation());
            my_playerdata.setViewDirection(character.getViewDirection());
        }
        if (state.equals("ingame") || state.equals("ingame_pause")) {
            Vector3f camDir = cam.getDirection().clone();
            Vector3f camLeft = cam.getLeft().clone();
            camDir.y = 0;
            camLeft.y = 0;
            camDir.normalizeLocal();
            camLeft.normalizeLocal();
            walkDirection.set(0, 0, 0);

            if (left) {
                walkDirection.addLocal(camLeft);
            }
            if (right) {
                walkDirection.addLocal(camLeft.negate());
            }
            if (up) {
                walkDirection.addLocal(camDir);
            }
            if (down) {
                walkDirection.addLocal(camDir.negate());
            }

            if (!character.onGround()) {
                airTime += tpf;
            } else {
                airTime = 0;
            }

            if (walkDirection.lengthSquared() == 0) {
                setAnimation("Stand");
            } else {
                character.setViewDirection(walkDirection);
                if (airTime > .3f) {
                    setAnimation("Stand");
                } else {
                    setAnimation("Walk");
                }
            }

            walkDirection.multLocal(25f).multLocal(tpf);
            character.setWalkDirection(walkDirection);
        }
    }

    public void initConnection(String ip, int port, String name) {
        try {
            System.out.println("Connecting to server [" + ip + ":" + port + "] as " + name);
            serverInfoMessage = null;
            dataManager = new ServerDataManager(this, assetManager, bulletAppState, guiFont, cam);
            client = Network.connectToServer(ip, port);
            client.addMessageListener(new ClientListener(client, dataManager, this));
            client.addClientStateListener(this);
            client.start();
            dataManager.setMyId(client.getId());
            my_playerdata = new PlayerData(client.getId(), name, new Vector3f(), new Quaternion(), "Models/womanmodel.j3o", 100);
            client.send(new PlayerDataMessage(my_playerdata));

            System.out.println("Waiting for server information.");

            int timeout = 180;

            while (true) {
                if (serverInfoMessage == null) {
                    if ((System.currentTimeMillis() % 1000) == 0) {
                        if (timeout != 0) {
                            timeout = timeout - 1;
                            System.out.println("Waiting for respond.");
                        } else {
                            client.close();
                            System.out.println("[Invalid Connection]: ServerInfoMessage not send. Disconnecting.");
                            return;
                        }
                    }
                } else {
                    break;
                }
            }

            if (serverInfoMessage.getGameHashCode().equals(gamehashcode) == false) {
                client.close();
                System.out.println("[Invalid Connection]: ServerInfoMessage has invalid gamehashcode. Disconnecting.");
                return;
            }

            if (serverInfoMessage.getProtocolVersion() != protocol) {
                client.close();
                System.out.println("[Invalid Connection]: ServerInfoMessage has invalid protocol version. Disconnecting.");
                return;
            }

            mapSpatials.clear();

            mapSpatials.put("terrain", assetManager.loadModel(serverInfoMessage.getWorldScene()));

            stateManager.attach(bulletAppState);

            rootNode.attachChild(mapSpatials.get("terrain"));

            terrainPhysicsNode = new RigidBodyControl(0f);
            mapSpatials.get("terrain").addControl(terrainPhysicsNode);
            bulletAppState.getPhysicsSpace().addAll(mapSpatials.get("terrain"));

            System.out.println("Misat11 has connected to server \"" + serverInfoMessage.getServerName() + "\" [" + ip + ":" + port + "]");

            chatw.setAlpha(0.4f);
            guiNode.detachChild(multiplayermenu);
            guiNode.attachChild(chatw);
            state = "inmultigame";
            
            guiNode.attachChild(statbar);

            CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(0.6f, 2f);
            character = new CharacterControl(capsuleShape, 0.09f);
            dataManager.getEntityData(client.getId()).addControl(character);
            character.warp(new Vector3f(0.0f, 60f, 0.0f));
            character.setGravity(40f);
            character.setJumpSpeed(15f);
            
            chasecam = new ChaseCamera(cam, dataManager.getEntityData(client.getId()), inputManager);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void clientConnected(Client c) {

    }

    @Override
    public void clientDisconnected(Client c, DisconnectInfo info) {
        if (state.equals("inmultigame") || state.equals("inmultigame_pause")) {
            guiNode.detachChild(chatw);
            guiNode.detachChild(chati);
            for (Map.Entry<String, Spatial> entry : mapSpatials.entrySet()) {
                Spatial value = entry.getValue();
                rootNode.detachChild(value);
            }

            for (int key : dataManager.getPlayerIdList()) {
                rootNode.detachChild(dataManager.getEntityData(key));
                dataManager.removePlayer(key);
            }

            bulletAppState.getPhysicsSpace().removeAll(mapSpatials.get("terrain"));

            guiNode.detachChild(statbar);
            
            stateManager.detach(bulletAppState);
            mapSpatials.clear();
            dataManager = null;
            System.out.println("Connection to [" + ip.getText() + ":" + port.getText() + "] closed. Thank for connection.");
        }
    }

    private void setAnimControls(Spatial spatial) {
        if (spatial == null) {
            if (animControls != null) {
                for (Iterator<AnimControl> it = animControls.iterator(); it.hasNext();) {
                    AnimControl animControl = it.next();
                    animControl.clearChannels();
                }
            }
            animControls = null;
            animChannels = null;
            return;
        }
        SceneGraphVisitorAdapter visitor = new SceneGraphVisitorAdapter() {

            @Override
            public void visit(Geometry geom) {
                super.visit(geom);
                checkForAnimControl(geom);
            }

            @Override
            public void visit(Node geom) {
                super.visit(geom);
                checkForAnimControl(geom);
            }

            private void checkForAnimControl(Spatial geom) {
                AnimControl control = geom.getControl(AnimControl.class);
                if (control == null) {
                    return;
                }
                if (animControls == null) {
                    animControls = new LinkedList<AnimControl>();
                }
                if (animChannels == null) {
                    animChannels = new LinkedList<AnimChannel>();
                }

                animControls.add(control);
                animChannels.add(control.createChannel());
            }
        };
        spatial.depthFirstTraversal(visitor);
    }

    private void setAnimation(String name) {
        if (animChannels != null) {
            for (Iterator<AnimChannel> it = animChannels.iterator(); it.hasNext();) {
                AnimChannel animChannel = it.next();
                if (animChannel.getAnimationName() == null || !animChannel.getAnimationName().equals(name)) {
                    animChannel.setAnim(name);
                    if (animChannel.getControl().getAnim(name) != null) {
                    }
                }
            }
        }
    }

}

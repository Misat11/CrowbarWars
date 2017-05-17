/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core;

import com.jme3.app.LegacyApplication;
import com.jme3.app.state.AppState;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.audio.AudioListenerState;
import com.jme3.bullet.BulletAppState;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.profile.AppStep;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeSystem;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import misat11.core.events.AbstractEvent;
import misat11.core.events.LoadEvent;
import misat11.core.gamelogic.LogicThread;
import misat11.core.json.JSONCreate;
import misat11.core.json.JSONLoader;
import misat11.core.keyboard.AbstractActionOnKey;
import misat11.core.keyboard.ControlSettings;
import misat11.core.menu.AbstractPanel;
import misat11.core.object.AbstractObject;
import misat11.core.server.messages.ModelInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author misat11
 */
public abstract class AbstractCore extends LegacyApplication {

    public float tpf;
    private HashMap<Integer, AbstractObject> objects = new HashMap<Integer, AbstractObject>();
    private HashMap<Integer, AbstractPanel> panels = new HashMap<Integer, AbstractPanel>();
    private HashMap<Integer, AbstractActionOnKey> keyboardActions = new HashMap<Integer, AbstractActionOnKey>();
    public BulletAppState bulletAppState;
    private AbstractEvent event;
    private ChaseCamera chasecam;

    private ControlSettings controlSettings = new ControlSettings();

    private String saveurl;

    private boolean debug = false;

    private Node rootNode = new Node("Root Node");
    private Node guiNode = new Node("Gui Node");
    protected BitmapText fpsText;
    protected BitmapFont guiFont;
    protected boolean showSettings = false;

    private ScheduledExecutorService exec;
    private LogicThread gameLogic;

    protected ModelsManager modelsManager;

    public AbstractCore() {
        this(new AudioListenerState());

    }

    public AbstractCore(AppState... initialStates) {
        super(initialStates);
    }

    @Override
    public void start() {

        Utils.initSerializer();

        boolean loadSettings = false;
        if (settings == null) {
            setSettings(new AppSettings(true));
            loadSettings = true;
        }

        if (showSettings) {
            if (!JmeSystem.showSettingsDialog(settings, loadSettings)) {
                return;
            }
        }

        setSettings(settings);
        super.start();
    }

    /**
     * Retrieves guiNode
     *
     * @return guiNode Node object
     *
     */
    public Node getGuiNode() {
        return guiNode;
    }

    /**
     * Retrieves rootNode
     *
     * @return rootNode Node object
     *
     */
    public Node getRootNode() {
        return rootNode;
    }

    public boolean isShowSettings() {
        return showSettings;
    }

    /**
     * Toggles settings window to display at start-up
     *
     * @param showSettings Sets true/false
     *
     */
    public void setShowSettings(boolean showSettings) {
        this.showSettings = showSettings;
    }

    /**
     * Creates the font that will be set to the guiFont field and subsequently
     * set as the font for the stats text.
     */
    protected BitmapFont loadGuiFont() {
        return assetManager.loadFont("Interface/Fonts/Default.fnt");
    }

    @Override
    public void initialize() {
        super.initialize();

        modelsManager = new ModelsManager();

        guiFont = loadGuiFont();

        guiNode.setQueueBucket(RenderQueue.Bucket.Gui);
        guiNode.setCullHint(Spatial.CullHint.Never);
        viewPort.attachScene(rootNode);
        guiViewPort.attachScene(guiNode);

        GuiGlobals.initialize(this);

        inputManager.setCursorVisible(true);

        BaseStyles.loadGlassStyle();

        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");

        JSONArray downloadedContent = JSONLoader.loadArray(saveurl + "downloaded.data");
        Iterator downloadedIterator = downloadedContent.iterator();

        while (downloadedIterator.hasNext()) {
            JSONObject downloaded = (JSONObject) downloadedIterator.next();
            ModelInfo info = new ModelInfo(downloaded.get("name").toString(), downloaded.get("author").toString(), downloaded.get("downloadUrl").toString(), Integer.parseInt(downloaded.get("version").toString()));
            String url = downloaded.get("savedUrl").toString();
            modelsManager.addModel(info, url);
        }

        assetManager.registerLocator(saveurl, FileLocator.class);

        bulletAppState = new BulletAppState();

        gameLogic = new LogicThread(this);

        exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(gameLogic, 0, 60, TimeUnit.MILLISECONDS);

        stateManager.attach(bulletAppState);

        changeEvent(new LoadEvent(this));

        gameInit();

        writeDebug("Complete init");
    }

    @Override
    public void update() {
        if (prof != null) {
            prof.appStep(AppStep.BeginFrame);
        }
        super.update();

        if (speed == 0 || paused) {
            return;
        }

        float tpf = timer.getTimePerFrame() * speed;
        this.tpf = tpf;

        if (prof != null) {
            prof.appStep(AppStep.StateManagerUpdate);
        }

        stateManager.update(tpf);

        gameUpdate(tpf);
        getEvent().update();

        if (prof != null) {
            prof.appStep(AppStep.SpatialUpdate);
        }

        rootNode.updateLogicalState(tpf);
        guiNode.updateLogicalState(tpf);

        rootNode.updateGeometricState();
        guiNode.updateGeometricState();

        if (prof != null) {
            prof.appStep(AppStep.StateManagerRender);
        }
        stateManager.render(renderManager);

        if (prof != null) {
            prof.appStep(AppStep.RenderFrame);
        }
        renderManager.render(tpf, context.isRenderable());
        gameRender(renderManager);
        stateManager.postRender();

        if (prof != null) {
            prof.appStep(AppStep.EndFrame);
        }
    }

    public AbstractEvent getEvent() {
        return event;
    }

    public void changeEvent(AbstractEvent event) {
        if (this.event != null) {
            writeDebug("Closing actual event: " + this.event.toString());
            this.event.end();

            if (panels.isEmpty() == false) {
                for (Map.Entry<Integer, AbstractPanel> panel : panels.entrySet()) {
                    guiNode.detachChild(panel.getValue().getContainer());
                    writeDebug("Detaching panel: " + panel.getKey());
                }
                panels.clear();
            }

            if (objects.isEmpty() == false) {
                for (Map.Entry<Integer, AbstractObject> object : objects.entrySet()) {
                    rootNode.detachChild(object.getValue().getSpatial());
                    writeDebug("Detaching object: " + object.getKey());
                }
                objects.clear();
            }

            if (keyboardActions.isEmpty() == false) {
                for (Map.Entry<Integer, AbstractActionOnKey> action : keyboardActions.entrySet()) {
                    action.getValue().destroy();
                    writeDebug("Removing key action: " + action.getKey());
                }
                keyboardActions.clear();
            }
        }

        this.event = event;

        writeDebug("Opening new event: " + this.event.toString());

        this.event.start();
    }

    public abstract void gameInit();

    public abstract void gameUpdate(float tpf);

    public int gameRegisterObject(AbstractObject object) {
        objects.put(objects.size() + 1, object);
        objects.get(objects.size()).getSpatial().setName("MISAT11_OBJECT_" + objects.size());
        writeDebug("Registring object with new id: " + objects.size());
        return objects.size();
    }

    public boolean isObjectRegistered(int id) {
        return objects.containsKey(id);
    }

    public AbstractObject getObject(int id) {
        return objects.get(id);
    }

    public void attachObject(int id) {
        if (isObjectRegistered(id)) {
            writeDebug("Attaching object: " + id);
            rootNode.attachChild(objects.get(id).getSpatial());
            objects.get(id).setIsAttached(true);
        }
    }

    public void detachObject(int id) {
        if (isObjectRegistered(id)) {
            writeDebug("Detaching object: " + id);
            rootNode.detachChild(objects.get(id).getSpatial());
            objects.get(id).setIsAttached(false);
        }
    }

    public BulletAppState getBulletAppState() {
        return bulletAppState;
    }

    public BitmapFont getGuiFont() {
        return guiFont;
    }

    public ChaseCamera getChasecam() {
        return chasecam;
    }

    public void setChasecam(int obj_id) {
        chasecam = new ChaseCamera(cam, getObject(obj_id).getSpatial(), inputManager);
    }

    public int gameRegisterPanel(AbstractPanel panel) {
        panel.completeContainer();
        panels.put(panels.size() + 1, panel);
        writeDebug("Registring panel with new id: " + panels.size());
        return panels.size();
    }

    public boolean isPanelRegistered(int id) {
        return panels.containsKey(id);
    }

    public AbstractPanel getPanel(int id) {
        return panels.get(id);
    }

    public void attachPanel(int id) {
        if (isPanelRegistered(id)) {
            writeDebug("Attaching panel: " + id);
            guiNode.attachChild(panels.get(id).getContainer());
            panels.get(id).setIsAttached(true);
        }
    }

    public void detachPanel(int id) {
        if (isPanelRegistered(id)) {
            writeDebug("Detaching panel: " + id);
            guiNode.detachChild(panels.get(id).getContainer());
            panels.get(id).setIsAttached(false);
        }
    }

    public int registerKeyBoardAction(AbstractActionOnKey action) {
        keyboardActions.put(keyboardActions.size() + 1, action);
        writeDebug("Register new KeyBoard Action: " + keyboardActions.size());
        return keyboardActions.size();
    }

    public void constructKeyBoardAction(int id) {
        if (keyboardActions.containsKey(id)) {
            writeDebug("Constructing key action: " + id);
            keyboardActions.get(id).construct();
        }
    }

    public void destroyKeyBoardAction(int id) {
        if (keyboardActions.containsKey(id)) {
            writeDebug("Destroying key action: " + id);
            keyboardActions.get(id).destroy();
        }
    }

    public void putSettings(AppSettings appSettings) {
        settings = appSettings;
    }

    public void putControlSettings(ControlSettings controlSettings) {
        this.controlSettings = controlSettings;
    }

    public ControlSettings getControlSettings() {
        return controlSettings;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void writeDebug(String string) {
        if (isDebug()) {
            System.out.println("DEBUG: " + string);
        }
    }

    public void gameRender(RenderManager rm) {

    }

    @Override
    public void destroy() {
        super.destroy();

        exec.shutdown();
    }

    public LogicThread getGameLogic() {
        return gameLogic;
    }

    public ModelsManager getModelsManager() {
        return modelsManager;
    }

    public String getSaveurl() {
        return saveurl;
    }

    public void setSaveurl(String saveurl) {
        this.saveurl = saveurl;
    }

}

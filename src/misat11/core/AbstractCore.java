/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.font.BitmapFont;
import com.jme3.input.ChaseCamera;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;
import java.util.HashMap;
import java.util.Map;
import misat11.core.events.AbstractEvent;
import misat11.core.events.LoadEvent;
import misat11.core.keyboard.AbstractActionOnKey;
import misat11.core.keyboard.ControlSettings;
import misat11.core.menu.AbstractPanel;
import misat11.core.object.AbstractObject;

/**
 *
 * @author misat11
 */
public abstract class AbstractCore extends SimpleApplication {
    
    public float tpf;
    public HashMap<Integer, AbstractObject> objects = new HashMap<Integer, AbstractObject>();
    public HashMap<Integer, AbstractPanel> panels = new HashMap<Integer, AbstractPanel>();
    public HashMap<Integer, AbstractActionOnKey> keyboardActions = new HashMap<Integer, AbstractActionOnKey>();
    public BulletAppState bulletAppState;
    public AbstractEvent event;
    public ChaseCamera chasecam;
    
    private ControlSettings controlSettings = new ControlSettings();
    
    public boolean debug = false;
    
    public AbstractCore() {
        super();
        
        Utils.initSerializer();
        
        changeEvent(new LoadEvent(this));
        
        setShowSettings(false);
        
    }
    
    @Override
    public void simpleInitApp() {
        GuiGlobals.initialize(this);
        
        inputManager.setCursorVisible(true);
        
        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);
        
        setDisplayStatView(false);
        
        setDisplayFps(false);
        
        flyCam.setEnabled(false);
        
        BaseStyles.loadGlassStyle();
        
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
        
        bulletAppState = new BulletAppState();
        
        stateManager.attach(bulletAppState);
        
        gameInit();
        
        writeDebug("Complete init");
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        this.tpf = tpf;
        gameUpdate(tpf);
        getEvent().update();
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
    
}

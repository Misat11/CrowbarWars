/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.font.BitmapFont; 
import com.jme3.network.serializing.Serializer;
import com.jme3.scene.Spatial;
import misat11.core.events.AbstractEvent;
import misat11.core.gamelogic.LogicThread;
import misat11.core.keyboard.AbstractActionOnKey;
import misat11.core.keyboard.ControlSettings;
import misat11.core.menu.GuiPanel;
import misat11.core.menu.SpatialGui;
import misat11.core.object.AbstractObject;
import misat11.core.server.messages.CameraLookMessage;
import misat11.core.server.messages.ChangeEntityLocationMessage;
import misat11.core.server.messages.ChangeObjectLocationMessage;
import misat11.core.server.messages.ClientHasMessage;
import misat11.core.server.messages.DespawnEntityMessage;
import misat11.core.server.messages.DespawnObjectMessage;
import misat11.core.server.messages.HealthBarUpdateMessage;
import misat11.core.server.messages.ModelInfo;
import misat11.core.server.messages.MoveMessage;
import misat11.core.server.messages.ServerInfoMessage;
import misat11.core.server.messages.TextMessage;
import misat11.core.server.messages.PlayerSettingsMessage;
import misat11.core.server.messages.ServerWantClientHasMessage;
import misat11.core.server.messages.SpawnEntityMessage;
import misat11.core.server.messages.SpawnObjectMessage;
import misat11.core.server.messages.guis.AbstractGuiElement;
import misat11.core.server.messages.guis.CloseGuiMessage;
import misat11.core.server.messages.guis.Gui;
import misat11.core.server.messages.guis.OpenGuiMessage; 
import misat11.core.server.messages.guis.QuadElement;
import misat11.core.server.messages.guis.TextElement;
import misat11.core.server.messages.guis.TextWCounterElement;

/**
 *
 * @author misat11
 */
public class Utils {

    public static final int BASE_PORT = 4444;
    public static final int PROTOCOL = 5;
    public static final String BASE_GAMEHASHCODE = "crbar_multi";
    private static AbstractCore core;
    private static Utils instance;

    //CALLED FROM AbstractCore, NOT USE
    public static void createUtils(AbstractCore main) {
        if (instance == null) {
            instance = new Utils();
            core = main;
        }
    }

    //CALLED FROM AbstractCore, NOT USE
    public static void initSerializer() {
        Serializer.registerClass(TextMessage.class);
        Serializer.registerClass(ServerInfoMessage.class);
        Serializer.registerClass(MoveMessage.class);
        Serializer.registerClass(PlayerSettingsMessage.class);
        Serializer.registerClass(ChangeEntityLocationMessage.class);
        Serializer.registerClass(ChangeObjectLocationMessage.class);
        Serializer.registerClass(SpawnObjectMessage.class);
        Serializer.registerClass(SpawnEntityMessage.class);
        Serializer.registerClass(DespawnObjectMessage.class);
        Serializer.registerClass(DespawnEntityMessage.class);
        Serializer.registerClass(ClientHasMessage.class);
        Serializer.registerClass(CameraLookMessage.class);
        Serializer.registerClass(ServerWantClientHasMessage.class);
        Serializer.registerClass(ModelInfo.class);
        Serializer.registerClass(Gui.class);
        Serializer.registerClass(AbstractGuiElement.class);
        Serializer.registerClass(OpenGuiMessage.class);
        Serializer.registerClass(CloseGuiMessage.class);
        Serializer.registerClass(TextElement.class); 
        Serializer.registerClass(TextWCounterElement.class);
        Serializer.registerClass(QuadElement.class);
        Serializer.registerClass(HealthBarUpdateMessage.class);
    }

    //GET FROM AbstractCore
    public static BitmapFont getBitmapFont() {
        return core.getGuiFont();
    }

    public static AbstractEvent getActualEvent() {
        return core.getEvent();
    }

    public static int registerObject(AbstractObject object) {
        return core.gameRegisterObject(object);
    }

    public static boolean isObjectRegistered(int id) {
        return core.isObjectRegistered(id);
    }

    public static void attachObject(int id) {
        core.attachObject(id);
    }

    public static void detachObject(int id) {
        core.detachObject(id);
    }

    public static int registerPanel(GuiPanel panel) {
        return core.gameRegisterPanel(panel);
    }

    public static boolean isPanelRegistered(int id) {
        return core.isPanelRegistered(id);
    }

    public static void attachPanel(int id) {
        core.attachPanel(id);
    }

    public static void detachPanel(int id) {
        core.detachPanel(id);
    }

    public static int registerKeyBoardAction(AbstractActionOnKey action) {
        return core.registerKeyBoardAction(action);
    }

    public static void constructKeyBoardAction(int id) {
        core.constructKeyBoardAction(id);
    }

    public static void destroyKeyBoardAction(int id) {
        core.destroyKeyBoardAction(id);
    }

    public static AbstractObject getObject(int id) {
        return core.getObject(id);
    }

    public static GuiPanel getPanel(int id) {
        return core.getPanel(id);
    }

    public static String getSaveUrl() {
        return core.getSaveurl();
    }

    public static LogicThread getLogicThread() {
        return core.getGameLogic();
    }

    public static Utils getUtilsInstance() {
        return instance;
    }

    public static AbstractCore getCore() {
        return core;
    }

    public static void changeEvent(AbstractEvent event) {
        core.changeEvent(event);
    }

    public static ControlSettings getControlSettings() {
        return core.getControlSettings();
    }

    public static void setEnabledDebug(boolean value) {
        core.setDebug(value);
    }

    public static boolean isDebugEnabled() {
        return core.isDebug();
    }

    public static void writeIntoDebug(String debugText) {
        core.writeDebug(debugText);
    }
    
    public static AssetManager getAssetManager(){
        return core.getAssetManager();
    }

    //BULLET PHYSIC
    public static BulletAppState getBulletAppState() {
        return core.getBulletAppState();
    }

    public static void addIntoPhysicSpace(Object obj) {
        core.getBulletAppState().getPhysicsSpace().add(obj);
    }

    public static void addAllIntoPhysicSpace(Spatial spatial) {
        core.getBulletAppState().getPhysicsSpace().addAll(spatial);
    }

    public static void removeFromPhysicSpace(Object obj) {
        core.getBulletAppState().getPhysicsSpace().remove(obj);
    }

    public static void removeAllFromPhysicSpace(Spatial spatial) {
        core.getBulletAppState().getPhysicsSpace().removeAll(spatial);
    }

    public static boolean isPhysicEnabled() {
        return core.getBulletAppState().isEnabled();
    }

    public static void setEnabledPhysic(boolean value) {
        core.getBulletAppState().setEnabled(value);
    }

    //RESOLUTION
    public static int getWidth() {
        return core.getSettings().getWidth();
    }

    public static int getHeight() {
        return core.getSettings().getHeight();
    }

    //USE THIS METHOD FOR GUIS
    public static float convertPrecentToLocX(float precent) {
        return (precent / 100) * core.getSettings().getWidth();
    }

    public static float convertPrecentToLocY(float precent) {
        return (precent / 100) * core.getSettings().getHeight();
    }

    public static SpatialGui convertSpatialToGuiPanel(Spatial spatial) {
        return new SpatialGui(spatial);
    }
}

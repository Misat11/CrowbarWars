/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.events;

import misat11.core.camera.AbstractView;
import misat11.core.camera.ThirdPersonView;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.network.Client;
import com.jme3.network.Network;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.Map.Entry;
import misat11.core.AbstractCore;
import misat11.core.Utils;
import misat11.core.json.JSONWrite;
import misat11.core.keyboard.MultiplayerKeys;
import misat11.core.keyboard.MultiplayerMove;
import misat11.core.menu.ChatInput;
import misat11.core.menu.ChatPanel;
import misat11.core.menu.HealthBar;
import misat11.core.menu.MultiplayerPauseMenu;
import misat11.core.object.GravityObject;
import misat11.core.object.SimpleSpatialObject;
import misat11.core.server.client.ClientDataManager;
import misat11.core.server.client.ClientListener;
import misat11.core.server.client.GuiManager;
import misat11.core.server.messages.ModelInfo;
import misat11.core.server.messages.PlayerSettingsMessage;
import misat11.core.server.messages.ServerInfoMessage;
import misat11.core.server.messages.TextMessage;

/**
 *
 * @author misat11
 */
public class ConnectionEvent extends AbstractEvent {

    private String ip;
    private int port;
    private String nickname;
    private int move_id;

    private ClientDataManager dataManager;
    private ClientListener listener;
    private Client client;

    private ServerInfoMessage serverInfoMessage;

    private MultiplayerMove move;

    public ChatPanel chatwindow;
    public int chatwindow_id;

    public ChatInput chatinput;
    public int chatinput_id;

    private int specialkeys_id;

    private boolean chatopen = false;

    private HealthBar healthbar;
    private int healthbar_id;

    private MultiplayerPauseMenu pausemenu;
    private int pausemenu_id;

    private boolean paused = false;

    private PlayerSettingsMessage settingsMessage;

    private AbstractView view;

    private HashMap<String, ModelInfo> modelsInfo;

    private GuiManager guiManager;

    public ConnectionEvent(AbstractCore main, String ip, int port, String nickname) {
        super(main);
        this.ip = ip;
        this.port = port;
        this.nickname = nickname;
    }

    @Override
    public void start() {
        try {
            System.out.println("Connecting to server [" + ip + ":" + port + "] as " + nickname);

            view = new ThirdPersonView(main, main.getCamera(), main.getInputManager());

            client = Network.connectToServer(Utils.BASE_GAMEHASHCODE, Utils.PROTOCOL, ip, port);

            guiManager = new GuiManager();
            dataManager = new ClientDataManager(client, main, this, guiManager);
            listener = new ClientListener(this, client, dataManager);
            client.addMessageListener(listener);
            client.start();
            settingsMessage = new PlayerSettingsMessage(nickname);
            dataManager.setMyId(client.getId());

            healthbar = new HealthBar();
            chatwindow = new ChatPanel();
            chatinput = new ChatInput(this);
            pausemenu = new MultiplayerPauseMenu(main, this);

            healthbar_id = main.gameRegisterPanel(healthbar);
            chatwindow_id = main.gameRegisterPanel(chatwindow);
            chatinput_id = main.gameRegisterPanel(chatinput);
            pausemenu_id = main.gameRegisterPanel(pausemenu);

            main.attachPanel(chatwindow_id);

            chatwindow.getContainer().setAlpha(0.3f);

            MultiplayerKeys multiplayerkeys = new MultiplayerKeys(main, this);
            specialkeys_id = main.registerKeyBoardAction(multiplayerkeys);
            main.constructKeyBoardAction(specialkeys_id);

            if (client.isConnected() == false) {
                end();
                return;
            }

            System.out.println("Waiting for server information.");

            int timeout = 180;

            while (true) {
                if (serverInfoMessage == null) {
                    if ((System.currentTimeMillis() % 1000) == 0) {
                        if (timeout != 0) {
                            timeout = timeout - 1;
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

            modelsInfo = serverInfoMessage.getModelsInfo();

            for (Entry<String, ModelInfo> model : serverInfoMessage.getModelsInfo().entrySet()) {
                if (!main.getModelsManager().containsModel(model.getValue())) {
                    if (model.getValue().getUrl().equals("none")) {
                        client.close();
                        return;
                    } else {
                        URL website = new URL(model.getValue().getUrl());
                        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                        String url = "DownloadedContent/" + model.getValue().getAuthor() + "-" + model.getValue().getName() + "-" + Integer.toString(model.getValue().getVersion()) + ".j3o";
                        FileOutputStream fos = new FileOutputStream(main.getSaveurl() + url);
                        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                        main.getModelsManager().addModel(model.getValue(), url);
                        JSONWrite.writeIntoArray(main.getSaveurl() + "downloaded.data", "{\"name\":\"" + model.getValue().getName() + "\", \"author\":\"" + model.getValue().getAuthor() + "\", \"version\":\"" + Integer.toString(model.getValue().getVersion()) + "\", \"downloadUrl\":\"" + model.getValue().getUrl() + "\", \"savedUrl\":\"" + url + "\"}");
                    }
                }
            }

            client.send(settingsMessage);

            main.attachPanel(healthbar_id);

            move = new MultiplayerMove(main, client);
            move_id = main.registerKeyBoardAction(move);
            main.constructKeyBoardAction(move_id);

            main.getInputManager().setCursorVisible(false);

            int terrain_id = main.gameRegisterObject(new GravityObject(main, new SimpleSpatialObject(main.getAssetManager().loadModel(main.getModelsManager().getUrl(modelsInfo.get(serverInfoMessage.getWorldScene())))), new RigidBodyControl(0f)));
            main.attachObject(terrain_id);
            System.out.println("Connection succesfully, waiting for updates");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void update() {
        view.update(0.02f);
    }

    @Override
    public void end() {
        if (client.isConnected()) {
            client.close();
        }
        main.getInputManager().setCursorVisible(true);
        dataManager.destroy();
        guiManager.removeAllGuis();
        dataManager = null;
        System.out.println("Connection to [" + ip + ":" + port + "] closed. Thank for connection.");
    }

    public void setServerInfoMessage(ServerInfoMessage serverInfoMessage) {
        this.serverInfoMessage = serverInfoMessage;
    }

    public ServerInfoMessage getServerInfoMessage() {
        return this.serverInfoMessage;
    }

    public ClientDataManager getDataManager() {
        return dataManager;
    }

    public ClientListener getListener() {
        return listener;
    }

    public boolean isChatopen() {
        return chatopen;
    }

    public void setChatopen(boolean chatopen) {
        this.chatopen = chatopen;
    }

    public void sendMessage(String msg) {
        if (client.isConnected()) {
            client.send(new TextMessage(msg));
        }
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isChatopenOrPaused() {
        return paused == true || chatopen == true;
    }

    public void pauseOrUnpauseGame() {
        if (paused == false) {
            if (chatopen == false) {
                chatopen = true;
                chatwindow.getContainer().setAlpha(1.0f);
                main.attachPanel(chatinput_id);
            }
            main.attachPanel(pausemenu_id);
            main.getInputManager().setCursorVisible(true);
            paused = true;
        } else {
            chatopen = false;
            chatwindow.getContainer().setAlpha(0.3f);
            main.getInputManager().setCursorVisible(false);
            main.detachPanel(chatinput_id);
            main.detachPanel(pausemenu_id);
            paused = false;
        }
    }

    public AbstractView getView() {
        return view;
    }

    public String getModelUrl(String internalServerName) {
        ModelInfo model = modelsInfo.get(internalServerName);
        return main.getModelsManager().getUrl(model);
    }

    public void updateHealthBar(double percent) {
        healthbar.update(percent);
    }

}

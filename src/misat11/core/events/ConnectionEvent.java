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
import java.io.IOException;
import misat11.core.AbstractCore;
import misat11.core.Utils;
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

            dataManager = new ClientDataManager(client, main, this);
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

            client.send(settingsMessage);

            main.attachPanel(healthbar_id);

            move = new MultiplayerMove(main, client);
            move_id = main.registerKeyBoardAction(move);
            main.constructKeyBoardAction(move_id);

            main.getInputManager().setCursorVisible(false);

            int terrain_id = main.gameRegisterObject(new GravityObject(main, new SimpleSpatialObject(main.getAssetManager().loadModel(serverInfoMessage.getWorldScene())), new RigidBodyControl(0f)));
            main.attachObject(terrain_id);
            System.out.println("Connection succesfully, waiting for updates");
        } catch (IOException ex) {

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

}

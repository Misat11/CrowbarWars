/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.events;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.Network;
import java.io.IOException;
import misat11.core.AbstractCore;
import misat11.core.Utils;
import misat11.core.keyboard.CharacterMove;
import misat11.core.keyboard.MultiplayerKeys;
import misat11.core.menu.ChatInput;
import misat11.core.menu.ChatPanel;
import misat11.core.menu.HealthBar;
import misat11.core.menu.MultiplayerPauseMenu;
import misat11.core.object.GravityObject;
import misat11.core.object.SimpleSpatialObject;
import misat11.core.server.client.ClientDataManager;
import misat11.core.server.client.ClientListener;
import misat11.core.server.messages.PlayerData;
import misat11.core.server.messages.PlayerDataMessage;
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

    private PlayerData my_playerdata;

    private ServerInfoMessage serverInfoMessage;

    private CharacterMove move;

    public ChatPanel chatwindow;
    public int chatwindow_id;

    public ChatInput chatinput;
    public int chatinput_id;

    private int specialkeys_id;

    private boolean chatopen = false;

    private HealthBar healthbar;
    private int healthbar_id;

    private boolean alliscompleted;

    private MultiplayerPauseMenu pausemenu;
    private int pausemenu_id;

    private boolean paused = false;

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

            dataManager = new ClientDataManager(main, this);
            listener = new ClientListener(this, client, dataManager);
            client = Network.connectToServer(ip, port);
            client.addMessageListener(listener);
            client.start();
            my_playerdata = new PlayerData(client.getId(), nickname, new Vector3f(), new Quaternion(), "Models/womanmodel.j3o", 100);
            client.send(new PlayerDataMessage(my_playerdata));
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
            main.attachPanel(healthbar_id);

            chatwindow.getContainer().setAlpha(0.3f);

            MultiplayerKeys multiplayerkeys = new MultiplayerKeys(main, this);
            specialkeys_id = main.registerKeyBoardAction(multiplayerkeys);
            main.constructKeyBoardAction(specialkeys_id);

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

            if (serverInfoMessage.getGameHashCode().equals(Utils.BASE_GAMEHASHCODE) == false) {
                client.close();
                System.out.println("[Invalid Connection]: ServerInfoMessage has invalid gamehashcode. Disconnecting.");
                return;
            }

            if (serverInfoMessage.getProtocolVersion() != Utils.PROTOCOL) {
                client.close();
                System.out.println("[Invalid Connection]: ServerInfoMessage has invalid protocol version. Disconnecting.");
                return;
            }

            int terrain_id = main.gameRegisterObject(new GravityObject(main, new SimpleSpatialObject(main.getAssetManager().loadModel(serverInfoMessage.getWorldScene())), new RigidBodyControl(0f)));
            main.attachObject(terrain_id);
            alliscompleted = true;
            System.out.println("Connection succesfully, waiting for updates");
        } catch (IOException ex) {

        }
    }

    @Override
    public void update() {
        if (alliscompleted == false) {
            return;
        }
        if (client.isConnected()) {
            client.send(new PlayerDataMessage(my_playerdata));
        }
        if (dataManager.isTherePlayerWithId(dataManager.getMyId())) {
            if (move == null) {
                move = new CharacterMove(main, dataManager.getCharacter(dataManager.getMyId()));
                move_id = main.registerKeyBoardAction(move);
                main.constructKeyBoardAction(move_id);
                main.setChasecam(dataManager.getObjectId(dataManager.getMyId()));
            }
            if (move.character != dataManager.getCharacter(dataManager.getMyId())) {
                move.character = dataManager.getCharacter(dataManager.getMyId());
                main.setChasecam(dataManager.getObjectId(dataManager.getMyId()));
            }
            if (move != null) {
                move.setStopped(isChatopenOrPaused());
            }
            healthbar.update(dataManager.getPlayerData(dataManager.getMyId()).getHealth() / 100);
            dataManager.getCharacter(dataManager.getMyId()).update();
            my_playerdata.setLocation(dataManager.getCharacter(dataManager.getMyId()).getControl().getPhysicsLocation());
            my_playerdata.setWalkDirection(dataManager.getCharacter(dataManager.getMyId()).getControl().getWalkDirection());
            my_playerdata.setViewDirection(dataManager.getCharacter(dataManager.getMyId()).getControl().getViewDirection());
        }
    }

    @Override
    public void end() {
        if (client.isConnected()) {
            client.close();
        }
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

    public PlayerData getMyPlayerdata() {
        return my_playerdata;
    }

    public void setMyPlayerdata(PlayerData my_playerdata) {
        this.my_playerdata = my_playerdata;
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
            paused = true;
        } else {
            chatopen = false;
            chatwindow.getContainer().setAlpha(0.3f);
            main.detachPanel(chatinput_id);
            main.detachPanel(pausemenu_id);
            paused = false;
        }
    }

}

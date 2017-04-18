/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import misat11.core.server.messages.JoinLeaveMessage;
import misat11.core.server.messages.ServerInfoMessage;
import misat11.core.server.messages.PlayerListMessage;
import misat11.core.server.messages.TextMessage;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.scene.Node;
import com.simsilica.lemur.Label;
import java.util.HashMap;
import java.util.concurrent.Callable;

/**
 *
 * @author misat11
 */
public class ClientListener implements MessageListener<Client> {

    private Client client;
    private ServerDataManager dataManager;
    private Main main;
    private HashMap<Integer, Node> list = new HashMap<Integer, Node>();
    private int first_in_list = 1;

    public ClientListener(Client client, ServerDataManager dataManager, Main main) {
        this.client = client;
        this.dataManager = dataManager;
        this.main = main;
    }

    @Override
    public void messageReceived(Client source, Message m) {
        if (m instanceof TextMessage) {
            TextMessage tx = (TextMessage) m;
            final String msg = tx.getMessage();
            System.out.println(msg);
            main.enqueue(new Callable() {
                @Override
                public Object call() throws Exception {
                    Label label_msg = new Label(msg);
                    label_msg.setMaxWidth(300);
                    if (list.size() > 10) {
                        main.chatw.removeChild(list.get(first_in_list));
                        first_in_list = first_in_list + 1;
                    }
                    Node child = main.chatw.addChild(label_msg);
                    list.put(list.size() + 1, child);
                    return null;
                }

            });
        } else if (m instanceof PlayerListMessage) {
            PlayerListMessage lm = (PlayerListMessage) m;
            dataManager.refreshPlayerList(lm.getPlayerList());
        } else if (m instanceof ServerInfoMessage) {
            Main.instance.serverInfoMessage = (ServerInfoMessage) m;
        } else if (m instanceof JoinLeaveMessage) {
            JoinLeaveMessage jlm = (JoinLeaveMessage) m;
            if (jlm.getIfJoinOrLeave()) {

            } else {
                dataManager.removePlayer(jlm.getClientId());
            }
        }
    }
}

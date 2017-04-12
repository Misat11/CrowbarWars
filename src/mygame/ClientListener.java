/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 *
 * @author misat11
 */
public class ClientListener implements MessageListener<Client>{
    private Client client;
    private ServerDataManager dataManager;
    
    public ClientListener(Client client, ServerDataManager dataManager){
        this.client = client;
        this.dataManager = dataManager;
    }

    @Override
    public void messageReceived(Client source, Message m) {
        if (m instanceof TextMessage){
            TextMessage tx = (TextMessage) m;
            String msg = tx.getMessage();
            System.out.println(msg);
            Main.addToChat(msg);
        } else if (m instanceof PlayerListMessage) {
            PlayerListMessage lm = (PlayerListMessage) m;
            dataManager.refreshPlayerList(lm.getPlayerList());
        } else if (m instanceof ServerInfoMessage){
            Main.instance.serverInfoMessage = (ServerInfoMessage) m;
        }
    }
}

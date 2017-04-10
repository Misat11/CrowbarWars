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
    
    public ClientListener(Client client){
        this.client = client;
    }

    @Override
    public void messageReceived(Client source, Message m) {
        if (m instanceof TextMessage){
            TextMessage tx = (TextMessage) m;
            String msg = tx.getMessage();
            System.out.println(msg);
            Main.addToChat(msg);
        }
    }
}

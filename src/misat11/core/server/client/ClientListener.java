/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server.client;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.simsilica.lemur.Label;
import java.util.HashMap;
import java.util.concurrent.Callable;
import misat11.core.events.ConnectionEvent;
import misat11.core.server.messages.CameraLookMessage;
import misat11.core.server.messages.ChangeEntityLocationMessage;
import misat11.core.server.messages.ChangeObjectLocationMessage;
import misat11.core.server.messages.DespawnEntityMessage;
import misat11.core.server.messages.DespawnObjectMessage;
import misat11.core.server.messages.HealthBarUpdateMessage;
import misat11.core.server.messages.ServerInfoMessage;
import misat11.core.server.messages.ServerWantClientHasMessage;
import misat11.core.server.messages.SpawnEntityMessage;
import misat11.core.server.messages.SpawnObjectMessage;
import misat11.core.server.messages.TextMessage;
import misat11.core.server.messages.guis.CloseGuiMessage;
import misat11.core.server.messages.guis.OpenGuiMessage;

/**
 *
 * @author misat11
 */
public class ClientListener implements MessageListener<Client> {
    
    private Client client;
    private ClientDataManager dataManager;
    private ConnectionEvent connection;
    
    private int first_in_list = 1;
    private HashMap<Integer, Label> list = new HashMap<Integer, Label>();
    
    public ClientListener(ConnectionEvent connection, Client client, ClientDataManager dataManager) {
        this.client = client;
        this.dataManager = dataManager;
        this.connection = connection;
    }
    
    @Override
    public void messageReceived(Client source, Message m) {
        if (m instanceof TextMessage) {
            TextMessage tx = (TextMessage) m;
            final String msg = tx.getMessage();
            System.out.println(msg);
            connection.main.enqueue(new Callable() {
                @Override
                public Object call() throws Exception {
                    Label label_msg = new Label(msg);
                    label_msg.setMaxWidth(300);
                    if (list.size() > 10) {
                        connection.chatwindow.getContainer().removeChild(list.get(first_in_list));
                        first_in_list = first_in_list + 1;
                    }
                    Label child = connection.chatwindow.getContainer().addChild(label_msg);
                    list.put(list.size() + 1, child);
                    return null;
                }
                
            });
        } else if (m instanceof ServerInfoMessage) {
            connection.setServerInfoMessage((ServerInfoMessage) m);
        } else if (m instanceof SpawnEntityMessage) {
            dataManager.spawnEntity((SpawnEntityMessage) m);
        } else if (m instanceof SpawnObjectMessage) {
            dataManager.spawnObject((SpawnObjectMessage) m);
        } else if (m instanceof DespawnEntityMessage) {
            dataManager.despawnEntity((DespawnEntityMessage) m);
        } else if (m instanceof DespawnObjectMessage) {
            dataManager.despawnObject((DespawnObjectMessage) m);
        } else if (m instanceof ChangeEntityLocationMessage) {
            dataManager.teleportEntity((ChangeEntityLocationMessage) m);
        } else if (m instanceof ChangeObjectLocationMessage) {
            dataManager.teleportObject((ChangeObjectLocationMessage) m);
        } else if (m instanceof CameraLookMessage) {
            dataManager.look(((CameraLookMessage) m).getLocation());
        } else if (m instanceof ServerWantClientHasMessage) {
            dataManager.sendClientHasMessage();
        } else if (m instanceof OpenGuiMessage) {
            dataManager.addGui((OpenGuiMessage) m);
        } else if (m instanceof CloseGuiMessage) {
            dataManager.removeGui((CloseGuiMessage) m);
        } else if (m instanceof HealthBarUpdateMessage){
            dataManager.updateHealthBar(((HealthBarUpdateMessage) m).getPercent());
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import java.util.ArrayList;

/**
 *
 * @author misat11
 */
@Serializable
public class ClientHasMessage extends AbstractMessage {
    private ArrayList<Integer> object_ids = new ArrayList<Integer>();
    private ArrayList<Integer> entities_ids = new ArrayList<Integer>();
    
    public ClientHasMessage(){}
    
    public ClientHasMessage(ArrayList<Integer> object_ids, ArrayList<Integer> entities_ids){
        this.object_ids = object_ids;
        this.entities_ids = entities_ids;
    }

    public ArrayList<Integer> getObject_ids() {
        return object_ids;
    }

    public void addObjectId(int id) {
        object_ids.add(id);
    }

    public void removeObjectId(int id) {
        object_ids.remove(id);
    }

    public ArrayList<Integer> getEntities_ids() {
        return entities_ids;
    }

    public void addEntityId(int id) {
        entities_ids.add(id);
    }

    public void removeEntityId(int id) {
        entities_ids.remove(id);
    }
}

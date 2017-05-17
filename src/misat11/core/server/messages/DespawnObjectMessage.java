/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author misat11
 */
@Serializable
public class DespawnObjectMessage extends AbstractMessage {

    private int id;

    public DespawnObjectMessage() {
    }

    public DespawnObjectMessage(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

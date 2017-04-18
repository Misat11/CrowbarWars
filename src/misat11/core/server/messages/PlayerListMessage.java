/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import java.util.HashMap;

/**
 *
 * @author misat11
 */
@Serializable
public class PlayerListMessage extends AbstractMessage {
    private HashMap players;
    
    public PlayerListMessage() {}
    
    public PlayerListMessage(HashMap players) {
        this.players = players;
    }
    
    public HashMap getPlayerList() {
        return players;
    }
    
}

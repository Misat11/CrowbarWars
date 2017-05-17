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
public class PlayerSettingsMessage extends AbstractMessage {
    private String name;
    
    public PlayerSettingsMessage() {}
    
    public PlayerSettingsMessage(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
}

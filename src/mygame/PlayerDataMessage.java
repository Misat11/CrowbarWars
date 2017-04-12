/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author misat11
 */
@Serializable
public class PlayerDataMessage extends AbstractMessage {
    private PlayerData data;
    
    public PlayerDataMessage() {}
    
    public PlayerDataMessage(PlayerData data){
        this.data = data;
    }
    
    public PlayerData getPlayerData(){
        return data;
    }
}

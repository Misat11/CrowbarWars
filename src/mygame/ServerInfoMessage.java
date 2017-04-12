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
public class ServerInfoMessage extends AbstractMessage {
    private String worldscene;
    private String servername;
    private int protocol;
    private String gamehashcode;
    
    public ServerInfoMessage() {}
    
    public ServerInfoMessage(String worldscene, String servername, int protocol, String gamehashcode) {
        this.worldscene = worldscene;
        this.servername = servername;
        this.protocol = protocol;
        this.gamehashcode = gamehashcode;
    }
    
    public String getWorldScene() {
        return worldscene;
    }
    
    public String getServerName() {
        return servername;
    }
    
    public String getGameHashCode() {
        return gamehashcode;
    }
    
    public int getProtocolVersion() {
        return protocol;
    }
}

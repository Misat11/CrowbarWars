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
public class ServerInfoMessage extends AbstractMessage {
    private String worldscene;
    private String servername;
    
    public ServerInfoMessage() {}
    
    public ServerInfoMessage(String worldscene, String servername) {
        this.worldscene = worldscene;
        this.servername = servername;
    }
    
    public String getWorldScene() {
        return worldscene;
    }
    
    public String getServerName() {
        return servername;
    }
}

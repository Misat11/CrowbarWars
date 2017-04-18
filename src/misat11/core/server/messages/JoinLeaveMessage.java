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
public class JoinLeaveMessage extends AbstractMessage {
    private boolean join;
    private int clientid;
    
    public JoinLeaveMessage() {}
    
    public JoinLeaveMessage(int clientid, boolean join){
        this.join = join;
        this.clientid = clientid;
    }
    
    public boolean getIfJoinOrLeave(){
        return this.join;
    }
    
    public int getClientId(){
        return this.clientid;
    }
}

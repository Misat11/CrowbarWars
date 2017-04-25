/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server.messages;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author misat11
 */
@Serializable
public class PlayerDataMessage extends AbstractMessage {
    private Vector3f location;
    private Quaternion rotation;
    private Vector3f walkDirection;
    private Vector3f viewDirection;
    private String name;
    private String modelAsset;
    private int health;
    
    public PlayerDataMessage() {}
    
    public PlayerDataMessage(PlayerData data){
        this.name = data.getName();
        this.modelAsset = data.getModelAsset();
        this.location = data.getLocation();
        this.health = data.getHealth();
        this.rotation = data.getRotation();
        this.walkDirection = data.getWalkDirection();
        this.viewDirection = data.getViewDirection();
    }
    
    public String getName(){
        return this.name;
    }
    
    public String getModelAsset(){
        return this.modelAsset;
    }
    
    public int getHealth(){
        return this.health;
    }
    
    public Vector3f getLocation(){
        return this.location;
    }
    
    public Quaternion getRotation(){
        return this.rotation;
    }
    
    public Vector3f getWalkDirection(){
        return this.walkDirection;
    }
    
    public Vector3f getViewDirection(){
        return this.viewDirection;
    }
            
            
}

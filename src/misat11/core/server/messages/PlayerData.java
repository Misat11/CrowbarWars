/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server.messages;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author misat11
 */
@Serializable
public class PlayerData {
    private int id;
    private String playername;
    
    private Vector3f walkDirection;
    private Vector3f viewDirection;
    private Vector3f location;
    private Quaternion rotation;
    
    private String modelAsset;
    private int health = 100;
    
    public PlayerData() {}
    
    public PlayerData(int i, String name, Vector3f loc, Quaternion rot, String model, int h){
        this.id = i;
        this.playername = name;
        this.location = loc;
        this.rotation = rot;
        this.modelAsset = model;
        this.health = h;
        this.walkDirection = new Vector3f();
        this.viewDirection = new Vector3f();
    }
    
    public int getId(){
        return this.id;
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public String getName(){
        return this.playername;
    }
    
    public void setName(String name){
       this.playername = name;
    }
    
    public Vector3f getLocation(){
        return this.location;
    }
    
    public void setLocation(Vector3f l){
        this.location = l;
    }
    
    public Quaternion getRotation(){
        return this.rotation;
    }
    
    public void setRotation(Quaternion q){
        this.rotation = q;
    }
    
    public String getModelAsset(){
        return this.modelAsset;
    }
    
    public void setModelAsset(String newModelAsset){
        this.modelAsset = newModelAsset;
    }
    
    public int getHealth(){
        return this.health;
    }
    
    public void setHealth(int h){
        this.health = h;
    }
    
    public Vector3f getWalkDirection(){
        return this.walkDirection;
    }
    
    public void setWalkDirection(Vector3f walk){
        this.walkDirection = walk;
    }
    
    public Vector3f getViewDirection(){
        return this.viewDirection;
    }
    
    public void setViewDirection(Vector3f view){
        this.viewDirection = view;
    }
}

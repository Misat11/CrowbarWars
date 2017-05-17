/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server.messages;

import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author misat11
 */
@Serializable
public class SpawnEntityMessage extends AbstractMessage {

    private int id = -1;
    private String assetUrl = "";
    private Vector3f location = null;
    private float mass = 0f;
    private String headText = "";
    
    public SpawnEntityMessage() {

    }

    /**
     * Create SpawnEntityMessage for spawn entity (Player, Bot). 
     *
     */
    public SpawnEntityMessage(int id, String assetUrl, Vector3f location, float mass, String headText) {
        this.id = id;
        this.assetUrl = assetUrl;
        this.location = location;
        this.mass = mass;
        this.headText = headText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAssetUrl() {
        return assetUrl;
    }

    public void setAssetUrl(String assetUrl) {
        this.assetUrl = assetUrl;
    }

    public Vector3f getLocation() {
        return location;
    }

    public void setLocation(Vector3f location) {
        this.location = location;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public String getHeadText() {
        return headText;
    }

    public void setHeadText(String headText) {
        this.headText = headText;
    }
    
    
}

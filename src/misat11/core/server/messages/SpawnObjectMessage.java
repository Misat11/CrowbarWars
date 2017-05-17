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
public class SpawnObjectMessage extends AbstractMessage {

    private int id = -1;
    private String assetUrl = "";
    private Vector3f location = null;
    private Quaternion rotation = null;
    private float mass = 0f;

    public SpawnObjectMessage() {

    }

    
    /**
     * Create SpawnObjectMessage for spawn Object (j3o) that was need in client.
     *
     */
    public SpawnObjectMessage(int id, String assetUrl, Vector3f location, Quaternion rotation, float mass) {
        this.id = id;
        this.assetUrl = assetUrl;
        this.location = location;
        this.rotation = rotation;
        this.mass = mass;
    }

    public int getId() {
        return id;
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

    public Quaternion getRotation() {
        return rotation;
    }

    public void setRotation(Quaternion rotation) {
        this.rotation = rotation;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }
}

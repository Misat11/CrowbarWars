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
public class ChangeEntityLocationMessage extends AbstractMessage {
    
    private int id;
    private Vector3f location = new Vector3f();
    private float airTime = 0f;
    private String actualanim = "Stand";
    private Vector3f view = new Vector3f();
    
    public ChangeEntityLocationMessage() {

    }

    public ChangeEntityLocationMessage(int id, Vector3f location, Vector3f view) {
        this.id = id;
        this.location = location;
        this.view = view;
    }

    public ChangeEntityLocationMessage(int id, Vector3f location, Vector3f view, String actualanim) {
        this.id = id;
        this.location = location;
        this.view = view;
        this.actualanim = actualanim;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Vector3f getLocation() {
        return location;
    }

    public void setLocation(Vector3f location) {
        this.location = location;
    }

    public float getAirTime() {
        return airTime;
    }

    public void setAirTime(float airTime) {
        this.airTime = airTime;
    }

    public String getActualanim() {
        return actualanim;
    }

    public void setActualanim(String actualanim) {
        this.actualanim = actualanim;
    }

    public Vector3f getView() {
        return view;
    }

    public void setView(Vector3f view) {
        this.view = view;
    }
    
    
}

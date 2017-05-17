/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.camera;

import com.jme3.input.InputManager;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import misat11.core.AbstractCore;

/**
 *
 * @author misat11
 */
public abstract class AbstractView implements AnalogListener {

    protected Camera camera;
    protected Vector3f location;
    protected Quaternion rotation;
    protected AbstractCore main;
    protected InputManager inputManager;

    protected AbstractView(AbstractCore main, Camera camera, InputManager inputManager) {
        this.camera = camera;
        this.main = main;
        this.inputManager = inputManager;
    }

    public abstract void update(float tpf);

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

    protected class ViewInput {

        public final static String DOWN = "CAMERA_VIEW_DOWN";
        public final static String UP = "CAMERA_VIEW_UP";
        public final static String ZOOMIN = "CAMERA_VIEW_ZOOM_IN";
        public final static String ZOOMOUT = "CAMERA_VIEW_ZOOM_OUT";
        public final static String MOVELEFT = "CAMERA_VIEW_MOVE_LEFT";
        public final static String MOVERIGHT = "CAMERA_VIEW_MOVE_RIGHT";
        public final static String TOGGLEROTATE = "CAMERA_VIEW_TOOGLE_ROTATE";
    }
}

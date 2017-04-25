/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.object;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.concurrent.Callable;
import misat11.core.AbstractCore;

/**
 *
 * @author misat11
 */
public class GravityObject extends AbstractObject {

    private Spatial spatial;
    private Vector3f phys_location;
    private RigidBodyControl control;

    public GravityObject(AbstractCore main, AbstractObject obj, RigidBodyControl control) {
        setMain(main);
        spatial = obj.getSpatial();
        this.control = control;
        spatial.addControl(control);
        getMain().enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                getBulletAppState().getPhysicsSpace().addAll(spatial);
                return null;
            }

        });
    }

    @Override
    public Spatial getSpatial() {
        return this.spatial;
    }

    @Override
    public void updateSpatial() {
        getMain().enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                spatial.setLocalTranslation(getLocation());
                spatial.setLocalRotation(getRotation());
                control.setPhysicsLocation(phys_location);
                return null;
            }

        });
    }

}

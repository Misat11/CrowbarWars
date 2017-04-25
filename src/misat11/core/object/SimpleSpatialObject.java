/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.object;

import com.jme3.scene.Spatial;
import java.util.concurrent.Callable;

/**
 *
 * @author misat11
 */
public class SimpleSpatialObject extends AbstractObject {

    private Spatial spatial;

    public SimpleSpatialObject(Spatial spatial) {
        this.spatial = spatial;
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
                return null;
            }

        });
    }
}

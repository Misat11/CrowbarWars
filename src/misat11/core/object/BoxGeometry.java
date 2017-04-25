/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.object;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 *
 * @author misat11
 */
public class BoxGeometry extends AbstractGeometry {

    public BoxGeometry(Vector3f size, Vector3f location, Quaternion rotation, String materialAdress, String textureAdress, String textureType) {
        float x = size.getX();
        float y = size.getY();
        float z = size.getZ();
        Box b = new Box(x, y, z);
        Geometry geom = new Geometry("Box", b);
        setMaterialAdress(materialAdress);
        setTextureAdress(textureAdress);
        setTextureType(textureType);
        setLocation(location);
        setRotation(rotation);
        parseSpatial(geom);
    }
}

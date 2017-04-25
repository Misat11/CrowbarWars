/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.object;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;

/**
 *
 * @author misat11
 */
public class SphereGeometry extends AbstractGeometry {

    public SphereGeometry(float radius, int radialSamples, int zSamples, Vector3f location, Quaternion rotation, String materialAdress, String textureAdress, String textureType) {
        Sphere s = new Sphere(zSamples, radialSamples, radius);
        Geometry geom = new Geometry("Sphere", s);
        setMaterialAdress(materialAdress);
        setTextureAdress(textureAdress);
        setTextureType(textureType);
        setLocation(location);
        setRotation(rotation);
        parseSpatial(geom);
    }
}

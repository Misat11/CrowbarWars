/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.object;

import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import java.util.concurrent.Callable;

/**
 *
 * @author misat11
 */
public abstract class AbstractGeometry extends AbstractObject {

    private String material = "Common/MatDefs/Misc/Unshaded.j3md";
    private String texture = null;
    private String texture_type = "ColorMap";
    private Spatial spatial;

    public Spatial parseSpatial(Geometry geometry) {
        Material mat = getMaterial();
        mat.setTexture(texture_type, getTexture());
        geometry.setLocalTranslation(getLocation());
        geometry.setLocalRotation(getRotation());
        geometry.setMaterial(mat);
        return geometry;
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

    public String getMaterialAdress() {
        return this.material;
    }

    public void setMaterialAdress(String material) {
        this.material = material;
    }

    public String getTextureAdress() {
        return this.texture;
    }

    public void setTextureAdress(String texture) {
        this.texture = texture;
    }

    public String getTextureType() {
        return this.texture_type;
    }

    public void setTextureType(String texture_type) {
        this.texture_type = texture_type;
    }

    public Material getMaterial() {
        Material mat = new Material(getAssetManager(), material);
        return mat;
    }

    public Texture getTexture() {
        Texture tex = getAssetManager().loadTexture(texture);
        return tex;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.object;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import misat11.core.AbstractCore;

/**
 *
 * @author misat11
 */
public abstract class AbstractObject {

    private boolean isAttached = false;
    private AbstractCore main;
    private AssetManager assetManager;
    private BulletAppState bulletAppState;
    private Vector3f location;
    private Quaternion rotation;

    public void setMain(AbstractCore main) {
        this.main = main;
        setAssetManager(main.getAssetManager());
        setBulletAppState(main.getBulletAppState());
    }
    
    public AbstractCore getMain() {
        return this.main;
    }
    
    public void setLocation(Vector3f location) {
        this.location = location;
    }

    public Vector3f getLocation() {
        return this.location;
    }

    public void setRotation(Quaternion rotation) {
        this.rotation = rotation;
    }

    public Quaternion getRotation() {
        return this.rotation;
    }

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public AssetManager getAssetManager() {
        return this.assetManager;
    }

    public void setBulletAppState(BulletAppState bulletAppState) {
        this.bulletAppState = bulletAppState;
    }

    public BulletAppState getBulletAppState() {
        return this.bulletAppState;
    }

    public boolean getIsAttached() {
        return isAttached;
    }

    public void setIsAttached(boolean v) {
        isAttached = v;
    }

    public abstract Spatial getSpatial();

    public abstract void updateSpatial();
    
}

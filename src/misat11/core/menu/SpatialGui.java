/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.menu;

import com.jme3.scene.Spatial;

/**
 *
 * @author misat11
 */
public class SpatialGui implements GuiPanel {

    private boolean isAttached = false;
    private Spatial spatial;

    public SpatialGui(Spatial spatial) {
        this.spatial = spatial;
    }

    @Override
    public void completeContainer() {
    }

    @Override
    public Spatial getContainer() {
        return spatial;
    }

    @Override
    public void setIsAttached(boolean v) {
        isAttached = v;
    }

    @Override
    public boolean getIsAttached() {
        return isAttached;
    }
}

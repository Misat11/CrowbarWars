/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server.messages.guis;

import com.jme3.network.serializing.Serializable;

/**
 *
 * @author misat11
 */
@Serializable()
public abstract class AbstractGuiElement {

    protected float loc_x = 0f;
    protected float loc_y = 0f;
    protected float alpha = 1f;
    protected float size_x = 0f;
    protected float size_y = 0f;

    protected AbstractGuiElement() {
    }
    
    protected AbstractGuiElement(float loc_x, float loc_y, float size_x, float size_y, float alpha) {
        this.size_x = size_x;
        this.size_y = size_y;
        this.alpha = alpha;
        this.loc_x = loc_x;
        this.loc_y = loc_y;
    }

    public float getLoc_x() {
        return loc_x;
    }

    public float getLoc_y() {
        return loc_y;
    }

    public float getAlpha() {
        return alpha;
    }

    public float getSize_x() {
        return size_x;
    }

    public float getSize_y() {
        return size_y;
    }
    
}

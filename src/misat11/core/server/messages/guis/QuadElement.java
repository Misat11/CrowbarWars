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
@Serializable
public class QuadElement extends AbstractGuiElement {

    private float red = 0f;
    private float green = 0f;
    private float blue = 0f;

    public QuadElement() {
    }

    public QuadElement(float loc_x, float loc_y, float size_x, float size_y, float alpha) {
        super(loc_x, loc_y, size_x, size_y, alpha);
    }

    public QuadElement(float loc_x, float loc_y, float size_x, float size_y, float alpha, float red, float green, float blue) {
        super(loc_x, loc_y, size_x, size_y, alpha);
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public float getRed() {
        return red;
    }

    public void setRed(float red) {
        this.red = red;
    }

    public float getGreen() {
        return green;
    }

    public void setGreen(float green) {
        this.green = green;
    }

    public float getBlue() {
        return blue;
    }

    public void setBlue(float blue) {
        this.blue = blue;
    }

}

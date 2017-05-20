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
public class TextElement extends AbstractGuiElement {

    protected String text = "";
    protected float red = 1f;
    protected float green = 1f;
    protected float blue = 1f;

    public TextElement() {
    }

    public TextElement(String text, float loc_x, float loc_y, float size_x, float size_y, float alpha) {
        super(loc_x, loc_y, size_x, size_y, alpha);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setRGB(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public void setRGBA(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public float getRed() {
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
    }
}

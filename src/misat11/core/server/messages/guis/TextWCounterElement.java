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
public class TextWCounterElement extends TextElement {

    protected int MILLISECONDS = 0;
    protected int SECONDS = 1;
    protected int MINUTES = 2;

    protected String counterInText = "{counter}";
    protected int inTime = SECONDS;
    protected int startIn = 15;
    protected int endIn = 0;
    protected int actualIn = startIn;

    public TextWCounterElement() {

    }

    public TextWCounterElement(String text, float loc_x, float loc_y, float size_x, float size_y, float alpha, int startIn) {
        super(text, loc_x, loc_y, size_x, size_y, alpha);
        this.startIn = startIn;
    }

    public TextWCounterElement(String text, float loc_x, float loc_y, float size_x, float size_y, float alpha, int startIn, int endIn) {
        super(text, loc_x, loc_y, size_x, size_y, alpha);
        this.startIn = startIn;
        this.endIn = endIn;
    }

    public TextWCounterElement(String text, float loc_x, float loc_y, float size_x, float size_y, float alpha, String counterReplaceText, int startIn, int endIn) {
        super(text, loc_x, loc_y, size_x, size_y, alpha);
        this.startIn = startIn;
        this.endIn = endIn;
        this.counterInText = counterReplaceText;
    }

    public String getCounterInText() {
        return counterInText;
    }

    public int getInTime() {
        return inTime;
    }

    public int getStartIn() {
        return startIn;
    }

    public int getEndIn() {
        return endIn;
    }

    public int getActualIn() {
        return actualIn;
    }
    
    public void setInTime(int i){
        if(i == SECONDS || i == MINUTES || i == MILLISECONDS){
            inTime = i;
        }
    }
}

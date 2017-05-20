/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server.messages.guis;

import com.jme3.network.serializing.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author misat11
 */
@Serializable
public class Gui {

    protected int guiId;
    protected Set<AbstractGuiElement> elements = new HashSet<AbstractGuiElement>();

    public Gui() {
    }

    public Gui(int guiId) {
        this.guiId = guiId;
    }

    public Gui(int guiId, Set<AbstractGuiElement> elements) {
        this.guiId = guiId;
        this.elements = elements;
    }
    
    public int getGuiId() {
        return guiId;
    }

    public void setGuiId(int guiId) {
        this.guiId = guiId;
    }
    
    public void addElement(AbstractGuiElement element) {
        if(!elements.contains(element)){
            elements.add(element);
        }
    }
    
    public void removeElement(AbstractGuiElement element) {
        if(elements.contains(element)){
            elements.remove(element);
        }
    }
    
    public Set<AbstractGuiElement> getElements(){
        return elements;
    }
}

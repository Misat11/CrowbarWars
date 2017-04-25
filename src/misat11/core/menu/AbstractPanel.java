/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.menu;

import com.simsilica.lemur.Container;

/**
 *
 * @author misat11
 */
public abstract class AbstractPanel {
    public Container container;
    public boolean isAttached;
    
    public abstract void completeContainer();
    
    public Container getContainer(){
        return this.container;
    }
    
    public void setContainer(Container container) {
        this.container = container;
    }
    
    public void setIsAttached(boolean v){
        this.isAttached = v;
    }
    
    public boolean getIsAttached(){
        return isAttached;
    }
}

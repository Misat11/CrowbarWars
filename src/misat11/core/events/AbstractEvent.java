/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.events;

import misat11.core.AbstractCore;

/**
 *
 * @author misat11
 */
public abstract class AbstractEvent {
    public AbstractCore main;
    
    public AbstractEvent(AbstractCore main){
        this.main = main;
    }
    
    public abstract void start();
    
    public abstract void update();
    
    public abstract void end();
}

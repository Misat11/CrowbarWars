/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.keyboard;

import com.jme3.input.controls.ActionListener;
import misat11.core.AbstractCore;

/**
 *
 * @author misat11
 */
public abstract class AbstractActionOnKey implements ActionListener {
    
    public AbstractCore main;
    
    public AbstractActionOnKey(AbstractCore main) {
        this.main = main;
    }

    public abstract void construct();
    
    public abstract void destroy();
}

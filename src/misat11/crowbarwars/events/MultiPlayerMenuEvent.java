/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.crowbarwars.events;

import misat11.core.AbstractCore;
import misat11.core.events.AbstractEvent;
import misat11.crowbarwars.panels.MultiPlayerMenu;

/**
 *
 * @author misat11
 */
public class MultiPlayerMenuEvent extends AbstractEvent {

    public MultiPlayerMenuEvent(AbstractCore main) {
        super(main);
    }

    @Override
    public void start() {
        int id = main.gameRegisterPanel(new MultiPlayerMenu(main));
        main.attachPanel(id);
    }

    @Override
    public void update() {
        
    }

    @Override
    public void end() {
        
    }
    
}

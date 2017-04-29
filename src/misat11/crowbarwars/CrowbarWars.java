/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.crowbarwars;

import misat11.core.AbstractCore;
import misat11.crowbarwars.events.MainMenuEvent;

/**
 *
 * @author misat11
 */
public class CrowbarWars extends AbstractCore {
    
    @Override
    public void gameInit() {
        changeEvent(new MainMenuEvent(this));
    }

    @Override
    public void gameUpdate(float tpf) {
        
    }
    
}

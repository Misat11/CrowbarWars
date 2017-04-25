/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.crowbarwars.keyboard;

import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import misat11.core.AbstractCore;
import misat11.core.keyboard.AbstractActionOnKey;
import misat11.crowbarwars.events.SingleplayerEvent;

/**
 *
 * @author misat11
 */
public class SinglePlayerPauseAction extends AbstractActionOnKey {

    public SingleplayerEvent event;
    
    public SinglePlayerPauseAction(AbstractCore main, SingleplayerEvent event) {
        super(main);
        this.event = event;
    }

    @Override
    public void construct() {
        main.getInputManager().addMapping("SINGLEPLAYER_PAUSE", new KeyTrigger(KeyInput.KEY_ESCAPE));
        main.getInputManager().addListener(this, "SINGLEPLAYER_PAUSE");
    }

    @Override
    public void destroy() {
        main.getInputManager().removeListener(this);
        main.getInputManager().deleteMapping("SINGLEPLAYER_PAUSE");
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if(name.equals("SINGLEPLAYER_PAUSE") && !isPressed){
            event.pause();
        }
    }
    
}

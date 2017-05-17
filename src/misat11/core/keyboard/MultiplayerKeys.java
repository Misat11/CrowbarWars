/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.keyboard;

import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import misat11.core.AbstractCore;
import misat11.core.events.ConnectionEvent;

/**
 *
 * @author misat11
 */
public class MultiplayerKeys extends AbstractActionOnKey {

    public ConnectionEvent event;

    public MultiplayerKeys(AbstractCore main, ConnectionEvent event) {
        super(main);
        this.event = event;
    }

    @Override
    public void construct() {
        main.getInputManager().addMapping("MULTIPLAYER_CHAT", new KeyTrigger(KeyInput.KEY_T));
        main.getInputManager().addMapping("MULTIPLAYER_ESCAPE", new KeyTrigger(KeyInput.KEY_ESCAPE));
        main.getInputManager().addListener(this, "MULTIPLAYER_CHAT", "MULTIPLAYER_ESCAPE");
    }

    @Override
    public void destroy() {
        main.getInputManager().removeListener(this);
        main.getInputManager().deleteMapping("MULTIPLAYER_CHAT");
        main.getInputManager().deleteMapping("MULTIPLAYER_ESCAPE");
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("MULTIPLAYER_CHAT") && !isPressed) {
            if (event.chatwindow.getContainer().getAlpha() == 0.3f) {
                event.chatwindow.getContainer().setAlpha(1.0f);
                main.attachPanel(event.chatinput_id);
                event.setChatopen(true);
                main.getInputManager().setCursorVisible(true);
            }
        } else if (name.equals("MULTIPLAYER_ESCAPE") && !isPressed) {
            if (event.isChatopen() == true && event.isPaused() == false) {
                event.chatwindow.getContainer().setAlpha(0.3f);
                main.detachPanel(event.chatinput_id);
                event.setChatopen(false);
                main.getInputManager().setCursorVisible(false);
            } else {
                event.pauseOrUnpauseGame();
            }
        }
    }

}

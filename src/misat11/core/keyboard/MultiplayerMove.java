/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.keyboard;

import com.jme3.input.controls.KeyTrigger;
import com.jme3.network.Client;
import misat11.core.AbstractCore;
import misat11.core.server.messages.MoveMessage;

/**
 *
 * @author misat11
 */
public class MultiplayerMove extends AbstractActionOnKey {

    public Client client;

    public boolean isStopped = false;

    public MultiplayerMove(AbstractCore main, Client client) {
        super(main);
        this.client = client;
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (isStopped == false && client.isConnected()) {
            MoveMessage msg = new MoveMessage();
            msg.setId(client.getId());
            msg.setDirCam(main.getCamera().getDirection().clone());
            msg.setLeftCam(main.getCamera().getLeft().clone());
            if (name.equals("CharacterMove_LEFT") && isPressed) {
                msg.setLeft(true);
            }
            if (name.equals("CharacterMove_RIGHT") && isPressed) {
                msg.setRight(true);
            }
            if (name.equals("CharacterMove_UP") && isPressed) {
                msg.setUp(true);
            }
            if (name.equals("CharacterMove_DOWN") && isPressed) {
                msg.setDown(true);
            }
            if (name.equals("CharacterMove_JUMP") && isPressed) {
                msg.setJump(true);
            }
            if (name.equals("CharacterMove_ATTACK") && isPressed) {
                //TODO
            }
            client.send(msg);
        }
    }

    @Override
    public void construct() {
        main.getInputManager().addMapping("CharacterMove_LEFT", new KeyTrigger(main.getControlSettings().getLEFT()));
        main.getInputManager().addMapping("CharacterMove_RIGHT", new KeyTrigger(main.getControlSettings().getRIGHT()));
        main.getInputManager().addMapping("CharacterMove_UP", new KeyTrigger(main.getControlSettings().getUP()));
        main.getInputManager().addMapping("CharacterMove_DOWN", new KeyTrigger(main.getControlSettings().getDOWN()));
        main.getInputManager().addMapping("CharacterMove_JUMP", new KeyTrigger(main.getControlSettings().getJUMP()));
        main.getInputManager().addMapping("CharacterMove_ATTACK", new KeyTrigger(main.getControlSettings().getATTACK()));
        main.getInputManager().addListener(this, "CharacterMove_LEFT", "CharacterMove_RIGHT", "CharacterMove_UP", "CharacterMove_DOWN", "CharacterMove_JUMP", "CharacterMove_ATTACK");
    }

    @Override
    public void destroy() {
        main.getInputManager().removeListener(this);
        main.getInputManager().deleteMapping("CharacterMove_LEFT");
        main.getInputManager().deleteMapping("CharacterMove_RIGHT");
        main.getInputManager().deleteMapping("CharacterMove_UP");
        main.getInputManager().deleteMapping("CharacterMove_DOWN");
        main.getInputManager().deleteMapping("CharacterMove_JUMP");
        main.getInputManager().deleteMapping("CharacterMove_ATTACK");
    }

    public void setStopped(boolean v) {
        this.isStopped = v;
    }

    public boolean isStopped() {
        return isStopped;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.keyboard;

import com.jme3.input.controls.KeyTrigger;
import java.util.concurrent.Callable;
import misat11.core.AbstractCore;
import misat11.core.character.AbstractCharacter;

/**
 *
 * @author misat11
 */
public class CharacterMove extends AbstractActionOnKey {

    public AbstractCharacter character;

    public boolean isStopped = false;

    public CharacterMove(AbstractCore main, AbstractCharacter character) {
        super(main);
        this.character = character;
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (isStopped == false) {
            if (name.equals("CharacterMove_LEFT")) {
                character.setLeft(isPressed);
            }
            if (name.equals("CharacterMove_RIGHT")) {
                character.setRight(isPressed);
            }
            if (name.equals("CharacterMove_UP")) {
                character.setUp(isPressed);
            }
            if (name.equals("CharacterMove_DOWN")) {
                character.setDown(isPressed);
            }
            if (name.equals("CharacterMove_JUMP")) {
                main.enqueue(new Callable() {
                    @Override
                    public Object call() throws Exception {
                        character.control.jump();
                        return null;
                    }
                });
            }
            if (name.equals("CharacterMove_ATTACK")) {
                //TODO
            }
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

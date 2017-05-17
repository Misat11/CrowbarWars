/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.gamelogic.states;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import misat11.core.character.AbstractCharacter;

/**
 *
 * @author misat11
 */
public class MovementAppState extends AbstractAppState {

    private AbstractCharacter character;
    private AppStateManager stateManager;
    private Application app;

    public MovementAppState() {

    }

    public MovementAppState(AbstractCharacter character) {
        this.character = character;
    }

    public void setCharacter(AbstractCharacter character) {
        this.character = character;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.app = app;
        this.stateManager = stateManager;
        super.initialize(stateManager, app);
    }

    @Override
    public void update(float tpf) {
        if (character != null) {
            character.update(tpf);
        }
    }
}

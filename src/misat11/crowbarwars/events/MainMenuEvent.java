/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.crowbarwars.events;

import misat11.core.AbstractCore;
import misat11.core.events.AbstractEvent;
import misat11.crowbarwars.panels.MainMenu;

/**
 *
 * @author misat11
 */
public class MainMenuEvent extends AbstractEvent {

    public MainMenuEvent(AbstractCore main) {
        super(main);
    }

    @Override
    public void start() {
        int id = main.gameRegisterPanel(new MainMenu(main));
        main.attachPanel(id);
    }

    @Override
    public void update() {

    }

    @Override
    public void end() {

    }

}

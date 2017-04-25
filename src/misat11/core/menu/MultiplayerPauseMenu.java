/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.menu;

import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import misat11.core.AbstractCore;
import misat11.core.events.ConnectionEvent;
import misat11.crowbarwars.events.MainMenuEvent;

/**
 *
 * @author misat11
 */
public class MultiplayerPauseMenu extends AbstractPanel {

    public AbstractCore main;
    public ConnectionEvent event;

    public MultiplayerPauseMenu(AbstractCore main, ConnectionEvent event) {
        this.main = main;
        this.event = event;
    }

    @Override
    public void completeContainer() {
        container = new Container();
        container.setLocalTranslation(300, 300, 0);
        container.addChild(new Label("Pausemenu"));
        Button backtogame = container.addChild(new Button("Back to game"));
        backtogame.addClickCommands(new Command() {
            @Override
            public void execute(Object source) {
                event.pauseOrUnpauseGame();
            }
        });

        Button settings = container.addChild(new Button("Settings"));
        settings.addClickCommands(new Command() {
            @Override
            public void execute(Object source) {

            }
        });

        Button exit = container.addChild(new Button("Exit"));
        exit.addClickCommands(new Command() {
            @Override
            public void execute(Object source) {
                main.changeEvent(new MainMenuEvent(main));
            }
        });
    }
}

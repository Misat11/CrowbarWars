/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.crowbarwars.panels;

import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import misat11.core.AbstractCore;
import misat11.core.menu.AbstractPanel;
import misat11.crowbarwars.events.MainMenuEvent;
import misat11.crowbarwars.events.SingleplayerEvent;

/**
 *
 * @author misat11
 */
public class SinglePlayerPause extends AbstractPanel {

    public AbstractCore main;
    public SingleplayerEvent event;
    
    public SinglePlayerPause(final AbstractCore main, final SingleplayerEvent event){
        this.main = main;
        this.event = event;
        container = new Container();
        container.setLocalTranslation(300, 300, 0);
        container.addChild(new Label("Pause Menu"));

        Button pausemenu_backtogame = container.addChild(new Button("Back to game"));
        pausemenu_backtogame.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {
                event.pause();
            }
        });

        Button pausemenu_settings = container.addChild(new Button("Settings"));
        pausemenu_settings.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {

            }
        });

        Button pausemenu_exit = container.addChild(new Button("Exit"));
        pausemenu_exit.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {
                main.changeEvent(new MainMenuEvent(main));
            }
        });
    }
    
    @Override
    public void completeContainer() {
    }
    
}

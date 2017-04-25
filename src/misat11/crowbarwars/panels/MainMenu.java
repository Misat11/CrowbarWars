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
import java.util.concurrent.Callable;
import misat11.core.AbstractCore;
import misat11.core.json.JSONLoader;
import misat11.core.menu.AbstractPanel;
import misat11.crowbarwars.events.ExitDialogEvent;
import misat11.crowbarwars.events.MultiPlayerMenuEvent;
import misat11.crowbarwars.events.SingleplayerEvent;
import org.json.simple.JSONObject;

/**
 *
 * @author misat11
 */
public class MainMenu extends AbstractPanel {

    public MainMenu(final AbstractCore main) {
        container = new Container();
        container.setLocalTranslation(300, 250, 0);
        container.addChild(new Label("Main menu"));
        Button singleplayer = container.addChild(new Button("Play Singleplayer"));
        singleplayer.addClickCommands(new Command() {
            @Override
            public void execute(Object source) {
                JSONObject json = JSONLoader.main("assets/Maps/TestMap.json");
                main.changeEvent(new SingleplayerEvent(main, json, "Models/womanmodel.j3o"));

            }
        });
        Button multiplayer = container.addChild(new Button("Play Multiplayer"));
        multiplayer.addClickCommands(new Command() {
            @Override
            public void execute(Object source) {
                main.changeEvent(new MultiPlayerMenuEvent(main));
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
                main.changeEvent(new ExitDialogEvent(main));
            }
        });
    }

    @Override
    public void completeContainer() {
    }

}

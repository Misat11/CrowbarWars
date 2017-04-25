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
import com.simsilica.lemur.TextField;
import misat11.core.AbstractCore;
import misat11.core.Utils;
import misat11.core.events.ConnectionEvent;
import misat11.core.menu.AbstractPanel;
import misat11.crowbarwars.events.MainMenuEvent;

/**
 *
 * @author misat11
 */
public class MultiPlayerMenu extends AbstractPanel {

    public AbstractCore main;
    public TextField ip;
    public TextField port;
    public TextField name;
    
    public MultiPlayerMenu(AbstractCore main){
        this.main = main;
    }
    
    @Override
    public void completeContainer() {
        container = new Container();
        container.setLocalTranslation(300, 300, 0);
        container.addChild(new Label("Multiplayer"));
        ip = container.addChild(new TextField(""));
        port = container.addChild(new TextField(Integer.toString(Utils.BASE_PORT)));
        name = container.addChild(new TextField(""));
        Button backtomainfrommulti = container.addChild(new Button("Back"));
        backtomainfrommulti.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {
                main.changeEvent(new MainMenuEvent(main));
            }
        });
        Button connect = container.addChild(new Button("Connect"));
        connect.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {
                main.changeEvent(new ConnectionEvent(main, ip.getText(), Integer.parseInt(port.getText()), name.getText()));
            }
        });
    }
    
}

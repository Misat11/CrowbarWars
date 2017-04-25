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
import misat11.core.menu.AbstractPanel;
import misat11.crowbarwars.events.MainMenuEvent;

/**
 *
 * @author misat11
 */
public class ExitDialog extends AbstractPanel {

    public ExitDialog(final AbstractCore main) {
        container = new Container();
        container.setLocalTranslation(300, 250, 0);
        container.addChild(new Label("Do you really want to leave this game?"));
        Button yes = container.addChild(new Button("YES"));
        yes.addClickCommands(new Command() {
            @Override
            public void execute(Object source) {
                main.stop();
                System.exit(0);
            }
        });
        Button no = container.addChild(new Button("NO"));
        no.addClickCommands(new Command() {
            @Override
            public void execute(Object source) {
                main.enqueue(new Callable() {
                    @Override
                    public Object call() throws Exception {
                        main.changeEvent(new MainMenuEvent(main));
                        return null;
                    }
                });
            }
        });
    }

    @Override
    public void completeContainer() {

    }

}

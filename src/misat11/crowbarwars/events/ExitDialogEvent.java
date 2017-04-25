/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.crowbarwars.events;

import java.util.concurrent.Callable;
import misat11.core.AbstractCore;
import misat11.core.events.AbstractEvent;
import misat11.crowbarwars.panels.ExitDialog;

/**
 *
 * @author misat11
 */
public class ExitDialogEvent extends AbstractEvent {

    public ExitDialogEvent(AbstractCore main) {
        super(main);
    }

    @Override
    public void start() {
        main.enqueue(new Callable(){
            @Override
            public Object call() throws Exception {
                int id = main.gameRegisterPanel(new ExitDialog(main));
                main.attachPanel(id);
                return null;
            }
        });
    }

    @Override
    public void update() {
        
    }

    @Override
    public void end() {
        
    }
    
}

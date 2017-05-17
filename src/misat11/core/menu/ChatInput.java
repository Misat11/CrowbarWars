/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.menu;

import com.jme3.math.Vector3f;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.TextField;
import misat11.core.events.ConnectionEvent;

/**
 *
 * @author misat11
 */
public class ChatInput extends AbstractPanel {

    public float size_x = 300;
    public float size_y = 40;
    public float location_x = 10;
    public float location_y = 80;
    public TextField input;
    public ConnectionEvent event;

    public ChatInput(ConnectionEvent event) {
        this.event = event;
    }

    public ChatInput(ConnectionEvent event, float size_x, float size_y, float location_x, float location_y) {
        this.event = event;
        this.size_x = size_x;
        this.size_y = size_y;
        this.location_x = location_x;
        this.location_y = location_y;
    }

    @Override
    public void completeContainer() {
        container = new Container();
        container.setLocalTranslation(location_x, location_y, 0);
        container.setPreferredSize(new Vector3f(size_x, size_y, 0));
        input = container.addChild(new TextField(""));
        Button sendButton = container.addChild(new Button("Send"));
        sendButton.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {
                send();
            }
        });
    }

    public void send() {
        if (input.getText().equals("") == false) {
            event.sendMessage(input.getText());
            input.setText("");
            event.chatwindow.getContainer().setAlpha(0.3f);
            event.main.detachPanel(event.chatinput_id);
            event.setChatopen(false);
            event.main.getInputManager().setCursorVisible(false);
        }
    }

}

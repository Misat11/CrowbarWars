/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.simsilica.lemur.Label;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 *
 * @author misat11
 */
public class Messages {

    public HashMap<Label, Boolean> messages;

    public Messages() {
        messages = new HashMap<Label, Boolean>();
    }

    public List<Label> getNotShowed() {
        List<Label> msg = new ArrayList<Label>();
        if (messages != null) {
            for (Label key : messages.keySet()) {
                if (messages.get(key).booleanValue() == false) {
                    msg.add(key);
                    messages.put(key, true);
                }
            }
        }
        return msg;
    }

    public void addMessage(Label l) {
        messages.put(l, false);
    }
}

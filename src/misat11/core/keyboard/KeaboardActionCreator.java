/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.keyboard;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import java.util.HashMap;
import java.util.Map;
import misat11.core.AbstractCore;

/**
 *
 * @author misat11
 */
public class KeaboardActionCreator extends AbstractActionOnKey {

    public HashMap<String, Integer> keys;
    public ActionListener action;
    
    public KeaboardActionCreator(AbstractCore main, ActionListener action, String keyName, int key) {
        super(main);
        this.keys.put(keyName, key);
        this.action = action;
    }
    public KeaboardActionCreator(AbstractCore main, ActionListener action, HashMap<String, Integer> keys) {
        super(main);
        this.keys.putAll(keys);
        this.action = action;
    }

    @Override
    public void construct() {
        for(Map.Entry<String, Integer> entry : keys.entrySet()){
            main.getInputManager().addMapping(entry.getKey(), new KeyTrigger(entry.getValue()));
        }
        main.getInputManager().addListener(action, keys.keySet().toArray(new String[keys.size()]));
    }

    @Override
    public void destroy() {
        main.getInputManager().removeListener(action);
        for(Map.Entry<String, Integer> entry : keys.entrySet()){
            main.getInputManager().deleteMapping(entry.getKey());
        }
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        
    }
    
}

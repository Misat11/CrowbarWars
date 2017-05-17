/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.camera;

import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import misat11.core.AbstractCore;

/**
 *
 * @author misat11
 */
public class FirstPersonView extends AbstractView {

    public FirstPersonView(AbstractCore main, Camera camera, InputManager inputManager) {
        super(main, camera, inputManager);
    }

    @Override
    public void update(float tpf) {
        camera.setLocation(location);
        camera.setRotation(rotation);
    }

    @Override
    public void onAnalog(String name, float value, float tpf) { 
        
    }
    
}

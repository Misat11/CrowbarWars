package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Spatial;

/**
 *
 * @author august Basic FPS style character control using BetterCharacterControl
 */
public class Main extends SimpleApplication
        {


    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        Spatial PlayerModel = assetManager.loadModel("Models/womanmodel.j3o");
        Spatial World = assetManager.loadModel("Scenes/Region0/main.j3o");
        
        rootNode.attachChild(World);
        rootNode.attachChild(PlayerModel);
    }
}

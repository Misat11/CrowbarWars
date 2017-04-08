package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.style.BaseStyles;

/**
 *
 * @author august Basic FPS style character control using BetterCharacterControl
 */
public class Main extends SimpleApplication {

    public String state = "loading";
    public Container mainmenu;
    public Spatial World;
    public Spatial PlayerModel;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        state = "mainmenu";

        World = assetManager.loadModel("Scenes/Region0/main.j3o");
        PlayerModel = assetManager.loadModel("Models/womanmodel.j3o");

        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);
        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_ESCAPE));
        inputManager.addListener(actionListener,"Pause");

        flyCam.setEnabled(false);

        GuiGlobals.initialize(this);

        BaseStyles.loadGlassStyle();

        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");

        mainmenu = new Container();
        guiNode.attachChild(mainmenu);

        mainmenu.setLocalTranslation(300, 300, 0);

        mainmenu.addChild(new Label("Main Menu"));
        Button tutorial = mainmenu.addChild(new Button("Play Tutorial"));
        tutorial.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {
                flyCam.setEnabled(true);
                rootNode.attachChild(PlayerModel);
                guiNode.detachChild(mainmenu);
                state = "ingame";

            }
        });
        Button multiplayer = mainmenu.addChild(new Button("Play Multiplayer"));
        multiplayer.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {

            }
        });
        Button settings = mainmenu.addChild(new Button("Settings"));
        settings.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {

            }
        });
        Button exit = mainmenu.addChild(new Button("Exit"));
        exit.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {
                stop();
            }
        });

        rootNode.attachChild(World);
    }

    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Pause") && !keyPressed) {
                if (state.equals("ingame")) {
                    guiNode.attachChild(mainmenu);
                    rootNode.detachChild(PlayerModel);
                    flyCam.setEnabled(false);
                    state = "mainmenu";
                }
            }
        }
    };
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.object;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.concurrent.Callable;
import misat11.core.AbstractCore;

/**
 *
 * @author misat11
 */
public class HeadText extends AbstractObject {

    public BitmapText text;

    public HeadText(AbstractCore main, BitmapFont font, Vector3f location, String text_string, Vector3f add) {
        setMain(main);
        text = new BitmapText(font, false);
        text.setText(text_string);
        text.setSize(0.3f);
        text.setLocalTranslation(location.add(add));
    }

    @Override
    public Spatial getSpatial() {
        return text;
    }

    @Override
    public void updateSpatial() {

    }

    public void updateLocation(final Vector3f location, final Vector3f add) {
        getMain().enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                text.setLocalTranslation(location.add(add));
                return null;
            }
        });

    }
    
    public void lookAt(final Vector3f location){
        getMain().enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                text.lookAt(location, Vector3f.UNIT_Y);
                return null;
            }
        });
    }

}

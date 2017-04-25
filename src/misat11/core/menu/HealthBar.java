/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.menu;

import com.jme3.math.Vector3f;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.ProgressBar;

/**
 *
 * @author misat11
 */
public class HealthBar extends AbstractPanel {

    private ProgressBar health;
    
    @Override
    public void completeContainer() {
        container = new Container();
        container.setLocalTranslation(450, 35, 0);

        container.setPreferredSize(new Vector3f(80, 25, 0));
        health = container.addChild(new ProgressBar());
        health.setPreferredSize(new Vector3f(80, 25, 0));
        health.setMessage("Health");
        health.setProgressPercent(1);
    }
    
    public void update(double progress){
        health.setProgressPercent(progress);
    }
    
}

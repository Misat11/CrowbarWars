/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.menu;

import com.jme3.math.Vector3f;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.component.SpringGridLayout;

/**
 *
 * @author misat11
 */
public class ChatPanel extends AbstractPanel {
    public float size_x = 300;
    public float size_y = 200;
    public float location_x = 10;
    public float location_y = 300;
    
    public ChatPanel() {}
    
    public ChatPanel(float size_x, float size_y, float location_x, float location_y) {
        this.size_x = size_x;
        this.size_y = size_y;
        this.location_x = location_x;
        this.location_y = location_y;
    }

    @Override
    public void completeContainer() {
        container = new Container(new SpringGridLayout(Axis.Y, Axis.X, FillMode.Last, FillMode.Last));
        container.setLocalTranslation(location_x, location_y, 0);
        container.setPreferredSize(new Vector3f(size_x, size_y, 0));
    }
}

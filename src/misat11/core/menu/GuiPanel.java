package misat11.core.menu;

import com.jme3.scene.Spatial;

/**
 *
 * @author misat11
 */
public interface GuiPanel {
    public void completeContainer();
    
    public Spatial getContainer();
    
    public void setIsAttached(boolean v);
    
    public boolean getIsAttached();
}

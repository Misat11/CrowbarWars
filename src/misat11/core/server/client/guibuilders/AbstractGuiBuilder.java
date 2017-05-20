/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server.client.guibuilders;

import com.jme3.scene.Node;

/**
 *
 * @author misat11
 */
public abstract class AbstractGuiBuilder {

    public abstract void build(Node node);
    
    public void destroy(){
    
    }
}

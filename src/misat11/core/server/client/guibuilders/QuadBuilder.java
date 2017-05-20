/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server.client.guibuilders;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import misat11.core.Utils; 
import misat11.core.server.messages.guis.QuadElement;

/**
 *
 * @author misat11
 */
public class QuadBuilder extends AbstractGuiBuilder {
    
    private QuadElement element; 
    
    public QuadBuilder(QuadElement element) {
        this.element = element;
    }
    
    @Override
    public void build(Node node) {
        Quad quad = new Quad(Utils.convertPrecentToLocX(element.getSize_x()), Utils.convertPrecentToLocY(element.getSize_y()));
        Geometry geom = new Geometry("Quad", quad);
        Material mat = new Material(Utils.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", new ColorRGBA(element.getRed(), element.getGreen(), element.getBlue(), element.getAlpha()));
        geom.setMaterial(mat);
        geom.setLocalTranslation(Utils.convertPrecentToLocX(element.getLoc_x()), Utils.convertPrecentToLocY(element.getLoc_y()), 0);
        node.attachChild(geom);
    }
    
}

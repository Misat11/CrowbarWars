/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server.client.guibuilders;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.Rectangle;
import com.jme3.math.ColorRGBA;
import misat11.core.Utils;
import misat11.core.menu.SpatialGui;
import misat11.core.server.messages.guis.TextElement;

/**
 *
 * @author misat11
 */
public class TextBuilder extends AbstractGuiBuilder {

    private TextElement element;
    private BitmapText text;
    private int id_in_main;

    public TextBuilder(TextElement element) {
        this.element = element;
    }

    @Override
    public void build() {
        text = new BitmapText(Utils.getBitmapFont(), false);
        text.setText(element.getText());
        text.setAlpha(element.getAlpha());
        text.setColor(new ColorRGBA(element.getRed(), element.getGreen(), element.getBlue(), element.getAlpha()));
        text.setBox(new Rectangle(
                Utils.convertPrecentToLocX(element.getLoc_x()),
                Utils.convertPrecentToLocY(element.getLoc_y()),
                Utils.convertPrecentToLocX(element.getSize_x()),
                Utils.convertPrecentToLocY(element.getSize_y())));
        text.setAlignment(BitmapFont.Align.Center);
        SpatialGui gui = Utils.convertSpatialToGuiPanel(text);
        id_in_main = Utils.registerPanel(gui);
        Utils.attachPanel(id_in_main);
        builded = true;
    }

    @Override
    public void destroy() {
        Utils.detachPanel(id_in_main);
        builded = false;
    }

}

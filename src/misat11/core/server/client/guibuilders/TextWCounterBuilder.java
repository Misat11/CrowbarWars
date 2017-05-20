/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server.client.guibuilders;

import com.jme3.font.BitmapText;
import com.jme3.font.Rectangle;
import com.jme3.math.ColorRGBA;
import java.util.concurrent.TimeUnit;
import misat11.core.Utils;
import misat11.core.menu.SpatialGui;
import misat11.core.server.messages.guis.TextWCounterElement;

/**
 *
 * @author misat11
 */
public class TextWCounterBuilder extends AbstractGuiBuilder {

    private int MILLISECONDS = 0;
    private int SECONDS = 1;
    private int MINUTES = 2;

    private int actualIn = -1;

    private TextWCounterElement element;
    private Counter counter;
    private BitmapText text;
    private int id_in_main;
    private boolean running = false;

    public TextWCounterBuilder(TextWCounterElement element) {
        this.element = element;
    }

    @Override
    public void build() {
        counter = new Counter();
        actualIn = element.getActualIn();
        String newstring = element.getText().replace(element.getCounterInText(), Integer.toString(actualIn));
        text = new BitmapText(Utils.getBitmapFont(), false);
        text.setText(newstring);
        text.setAlpha(element.getAlpha());
        text.setColor(new ColorRGBA(element.getRed(), element.getGreen(), element.getBlue(), element.getAlpha()));
        text.setBox(new Rectangle(
                Utils.convertPrecentToLocX(element.getLoc_x()),
                Utils.convertPrecentToLocY(element.getLoc_y()),
                Utils.convertPrecentToLocX(element.getSize_x()),
                Utils.convertPrecentToLocY(element.getSize_y())));
        SpatialGui gui = Utils.convertSpatialToGuiPanel(text);
        id_in_main = Utils.registerPanel(gui);
        Utils.attachPanel(id_in_main);
        running = true;
        counter.start();
        builded = true;
    }

    @Override
    public void destroy() {
        running = false;
        Utils.detachPanel(id_in_main);
        builded = false;
    }

    protected class Counter extends Thread {

        @Override
        public void run() {
            while (running) {
                try {
                    if (actualIn == -1 || actualIn > element.getStartIn()) {
                        actualIn = element.getStartIn();
                    }
                    if (element.getInTime() == MILLISECONDS) {
                        TimeUnit.MILLISECONDS.sleep(1);
                    } else if (element.getInTime() == SECONDS) {
                        TimeUnit.SECONDS.sleep(1);
                    } else if (element.getInTime() == MINUTES) {
                        TimeUnit.MINUTES.sleep(1);
                    } else {
                        running = false;
                        return;
                    }
                    actualIn = actualIn - 1;
                    text.setText(element.getText().replace(element.getCounterInText(), Integer.toString(actualIn)));
                    if (actualIn <= element.getEndIn()) {
                        running = false;
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    running = false;
                    return;
                }
            }
        }
    }

}

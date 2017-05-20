package misat11.core.server.client;

import com.jme3.scene.Node;
import java.util.HashMap;
import java.util.Map.Entry;
import misat11.core.Utils;
import misat11.core.menu.SpatialGui;
import misat11.core.server.client.guibuilders.AbstractGuiBuilder;
import misat11.core.server.client.guibuilders.QuadBuilder;
import misat11.core.server.client.guibuilders.TextBuilder;
import misat11.core.server.client.guibuilders.TextWCounterBuilder;
import misat11.core.server.messages.guis.AbstractGuiElement;
import misat11.core.server.messages.guis.CloseGuiMessage;
import misat11.core.server.messages.guis.Gui;
import misat11.core.server.messages.guis.OpenGuiMessage;
import misat11.core.server.messages.guis.QuadElement;
import misat11.core.server.messages.guis.TextElement;
import misat11.core.server.messages.guis.TextWCounterElement;

/**
 *
 * @author misat11
 */
public class GuiManager {

    private HashMap<Integer, G> guis = new HashMap<Integer, G>();

    private class G {

        private Gui gui;
        private HashMap<AbstractGuiElement, AbstractGuiBuilder> builders = new HashMap<AbstractGuiElement, AbstractGuiBuilder>();
        private boolean builded = false;
        private int id_in_main;

        public G(Gui gui) {
            this.gui = gui;
        }

        public void build() {
            if (!builded) {
                Node node = new Node("MULTIPLAYER-GUI " + Integer.toString(gui.getGuiId()));
                for (AbstractGuiElement element : gui.getElements()) {
                    if (element instanceof TextWCounterElement) {
                        builders.put(element, new TextWCounterBuilder((TextWCounterElement) element));
                        builders.get(element).build(node);
                    } else if (element instanceof TextElement) {
                        builders.put(element, new TextBuilder((TextElement) element));
                        builders.get(element).build(node);
                    } else if (element instanceof QuadElement) {
                        builders.put(element, new QuadBuilder((QuadElement) element));
                        builders.get(element).build(node);
                    }
                }
                id_in_main = Utils.registerPanel(new SpatialGui(node));
                Utils.attachPanel(id_in_main);
                builded = true;
            }
        }

        public void destroy() {
            if (builded) {
                Utils.detachPanel(id_in_main);
                for (Entry<AbstractGuiElement, AbstractGuiBuilder> entry : builders.entrySet()) {
                    entry.getValue().destroy();
                }
            }
        }
    }

    public GuiManager() {

    }

    public void addGui(OpenGuiMessage m) {
        if (!guis.containsKey(m.getGui().getGuiId())) {
            guis.put(m.getGui().getGuiId(), new G(m.getGui()));
            guis.get(m.getGui().getGuiId()).build();
        }
    }

    public void removeGui(CloseGuiMessage m) {
        if (m.getId() == -1) {
            removeAllGuis();
        } else if (guis.containsKey(m.getId())) {
            guis.get(m.getId()).destroy();
            guis.remove(m.getId());
        }
    }

    public void removeAllGuis() {
        for (Entry<Integer, G> entry : guis.entrySet()) {
            entry.getValue().destroy();
            guis.remove(entry.getKey());
        }
    }
}

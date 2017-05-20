package misat11.core.server.client;

import java.util.HashMap;
import java.util.Map.Entry;
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

        public G(Gui gui) {
            this.gui = gui;
        }

        public void build() {
            if (!builded) {
                for (AbstractGuiElement element : gui.getElements()) {
                    if (element instanceof TextWCounterElement) {
                        builders.put(element, new TextWCounterBuilder((TextWCounterElement) element));
                        builders.get(element).build();
                    } else if (element instanceof TextElement) {
                        builders.put(element, new TextBuilder((TextElement) element));
                        builders.get(element).build();
                    } else if (element instanceof QuadElement) {
                        builders.put(element, new QuadBuilder((QuadElement) element));
                        builders.get(element).build();
                    }
                }
            }
        }

        public void destroy() {
            if (builded) {
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

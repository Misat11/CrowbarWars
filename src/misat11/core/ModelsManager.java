/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core;

import java.util.HashSet;
import java.util.Set;
import misat11.core.server.messages.ModelInfo;

/**
 *
 * @author misat11
 */
public class ModelsManager {

    protected class Model {

        public ModelInfo info;
        public String url;

        public Model(ModelInfo info, String url) {
            this.info = info;
            this.url = url;
        }

        public ModelInfo getInfo() {
            return info;
        }

        public String getUrl() {
            return url;
        }

        public boolean isSame(ModelInfo info) {
            return info.getName().equals(this.info.getName()) && info.getAuthor().equals(this.info.getAuthor()) && info.getVersion() == this.info.getVersion();
        }

    }

    private Set<Model> models = new HashSet<Model>();

    public ModelsManager() {
        models.add(new Model(new ModelInfo("WomanModel1", "Misat11", "https://github.com/Misat11/CrowbarWars/blob/master/assets/Models/womanmodel.j3o?raw=true", 1), "Models/womanmodel.j3o"));
        models.add(new Model(new ModelInfo("TestMap", "Misat11", "https://github.com/Misat11/CrowbarWars/blob/master/assets/Scenes/Region0/main.j3o?raw=true", 1), "Scenes/Region0/main.j3o"));
    }

    public boolean containsModel(ModelInfo model) {
        for (Object o : models.toArray()) {
            Model m = (Model) o;
            if (m.isSame(model)) {
                return true;
            }
        }
        return false;
    }

    public String getUrl(ModelInfo model) {
        for (Object o : models.toArray()) {
            Model m = (Model) o;
            if (m.isSame(model)) {
                return m.getUrl();
            }
        }
        return null;
    }

    public void addModel(ModelInfo model, String url) {
        models.add(new Model(model, url));
    }
    
    public ModelInfo getModelInfo(String url){
        for(Object o : models.toArray()){
            Model m = (Model) o;
            if(m.getUrl().equals(url)){
                return m.getInfo();
            }
        }
        return null;
    }
}

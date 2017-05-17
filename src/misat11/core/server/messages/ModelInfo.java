package misat11.core.server.messages;

import com.jme3.network.serializing.Serializable;

/**
 * @author misat11
 */
@Serializable
public class ModelInfo {

    private String name;
    private String url;
    private String author;
    private int version;

    public ModelInfo() {
    }

    public ModelInfo(String name, String author, String url, int version) {
        this.name = name;
        this.url = url;
        this.author = author;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public int getVersion() {
        return version;
    }

    public String getAuthor() {
        return author;
    }
}

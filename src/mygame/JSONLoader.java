package mygame;
 
import java.io.FileReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JSONLoader {
 
    @SuppressWarnings("unchecked")
    public static JSONObject main(String file) {
        JSONParser parser = new JSONParser();
 
        try {
 
            Object obj = parser.parse(new FileReader(file));
 
            JSONObject jsonObject = (JSONObject) obj;
 
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
}
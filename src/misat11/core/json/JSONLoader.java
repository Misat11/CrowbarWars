package misat11.core.json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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

    public static JSONObject fromJar(String path) {
        try {
            JSONParser parser = new JSONParser();
            InputStream is = JSONObject.class.getClass().getResourceAsStream(path);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            isr.close();
            is.close();
            return (JSONObject) parser.parse(sb.toString());
        } catch (IOException ex) {
            Logger.getLogger(JSONLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(JSONLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}

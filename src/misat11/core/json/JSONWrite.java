/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.json;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;

/**
 *
 * @author misat11
 */
public class JSONWrite {

    @SuppressWarnings("unchecked")
    public static void main(String filename, HashMap<String, String> keys_values) {
        BufferedWriter writer = null;
        try {
            File file = new File(filename);
            JSONObject json = JSONLoader.main(filename);
            for (Map.Entry<String, String> entry : keys_values.entrySet()) {
                json.put(entry.getKey(), entry.getValue());
            }
            writer = new BufferedWriter(new FileWriter(file, false));
            writer.write(json.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public static void main(String filename, String key, String value) {
        BufferedWriter writer = null;
        try {
            File file = new File(filename);
            JSONObject json = JSONLoader.main(filename);
            json.put(key, value);
            writer = new BufferedWriter(new FileWriter(file, false));
            writer.write(json.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static void main(String filename, String string) {
        BufferedWriter writer = null;
        try {
            File file = new File(filename);
            writer = new BufferedWriter(new FileWriter(file, false));
            writer.write(string);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
    }
}

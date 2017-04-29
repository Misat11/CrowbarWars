/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.language;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import misat11.core.json.JSONLoader;
import org.json.simple.JSONObject;

/**
 *
 * @author misat11
 */
public class LanguageManager {

    private HashMap<String, String> lang = new HashMap<String, String>();
    private LinkedHashMap<String, String> lang_codes = new LinkedHashMap<String, String>();

    public LanguageManager() {
        lang_codes.put("EN", "English");
        lang_codes.put("CZ", "Čeština");
    }

    public void addLanguageData(String json_filename) {
        JSONObject obj = JSONLoader.fromJar(json_filename);
        for (Object key : obj.keySet()) {
            lang.put(key.toString(), obj.get(key).toString());
        }
    }

    public void addLanguageData(HashMap<String, String> strings) {
        for (Map.Entry<String, String> string : strings.entrySet()) {
            lang.put(string.getKey(), string.getValue());
        }
    }
    
    public void addLanguageData(String key, String value){
        lang.put(key, value);
    } 

    public String get(String lang_String) {
        if (lang.containsKey(lang_String)) {
            return lang.get(lang_String);
        } else {
            return lang_String;
        }
    }

    public String get(String english, String lang_String) {
        if (lang.containsKey(lang_String)) {
            return lang.get(lang_String);
        } else {
            return english;
        }
    }

    public HashMap<String, String> getLangCodes() {
        return lang_codes;
    }

    public void addLangCode(String code, String name) {
        lang_codes.put(code, name);
    }
    
    
}

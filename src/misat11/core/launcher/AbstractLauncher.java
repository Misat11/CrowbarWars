/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.launcher;

import com.jme3.system.AppSettings;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import misat11.core.AbstractCore;
import misat11.core.json.JSONCreate;
import misat11.core.json.JSONLoader;
import misat11.core.json.JSONWrite;
import misat11.core.keyboard.CharacterBaseKeys;
import misat11.core.keyboard.ControlSettings;
import misat11.core.language.LanguageManager;
import misat11.core.resolution.R1024x768;
import misat11.core.resolution.R1200x720;
import misat11.core.resolution.R1200x768;
import misat11.core.resolution.R1360x768;
import misat11.core.resolution.R1366x768;
import misat11.core.resolution.R1920x1080;
import misat11.core.resolution.R640x480;
import misat11.core.resolution.R800x600;
import misat11.core.resolution.Resolution;
import org.json.simple.JSONObject;

/**
 *
 * @author misat11
 */
public abstract class AbstractLauncher {

    public AbstractCore gameCore;
    public LauncherWindow window;
    public AppSettings settings = new AppSettings(true);
    public JSONObject json;
    public JSONObject old_json;
    public LanguageManager langManager;
    public ControlSettings controlSettings = new ControlSettings();

    private boolean isLauncherOpened = false;
    private boolean startByButton = false;
    private boolean restart_request = false;
    private JPanel container = new JPanel();
    private HashMap<String, JPanel> panels = new HashMap<String, JPanel>();
    private LinkedHashMap<String, Resolution> valid_resolutions = new LinkedHashMap<String, Resolution>();

    public AbstractLauncher() {
        preInit();
        initWindow();
        init();
        postInit();
        constructWindow();
        update();
    }

    public void update() {
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(AbstractLauncher.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (isLauncherOpened && startByButton) {
                start();
                return;
            } else if (restart_request) {
                destroyWindow();
                load();
                return;
            }
        }
    }

    public abstract void init();

    public abstract String getSaveDirectoryName();

    public abstract void load();

    public void save(String file, HashMap<String, Object> write) {
        String userhome = System.getProperty("user.home");
        String fileurl = userhome + "/" + getSaveDirectoryName() + "/" + file;
        JSONWrite.main(fileurl, write);
    }

    public void reloadLauncher() {
        restart_request = true;
    }

    public void start() {
        destroyWindow();
        gameCore.putSettings(settings);
        gameCore.putControlSettings(controlSettings);
        gameCore.start();
    }

    public void startByButton() {
        startByButton = true;
    }

    public JPanel getPanel(String string) {
        return panels.get(string);
    }

    public void addPanel(String string, JPanel panel) {
        this.panels.put(string, panel);
    }

    private void initWindow() {
        window = new LauncherWindow();
    }

    private void constructWindow() {
        window.setVisible(true);
        isLauncherOpened = true;
    }

    private void destroyWindow() {
        window.setVisible(false);
        isLauncherOpened = false;
    }

    private void preInit() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        valid_resolutions.put("640x480", new R640x480());
        if (width >= 800 && height >= 600) {
            valid_resolutions.put("800x600", new R800x600());
        }
        if (width >= 1024 && height >= 768) {
            valid_resolutions.put("1024x768", new R1024x768());
        }
        if (width >= 1200 && height >= 720) {
            valid_resolutions.put("1200x720", new R1200x720());
        }
        if (width >= 1200 && height >= 768) {
            valid_resolutions.put("1200x768", new R1200x768());
        }
        if (width >= 1360 && height >= 768) {
            valid_resolutions.put("1360x768", new R1360x768());
        }
        if (width >= 1366 && height >= 768) {
            valid_resolutions.put("1366x768", new R1366x768());
        }
        if (width >= 1920 && height >= 1080) {
            valid_resolutions.put("1920x1080", new R1920x1080());
        }

        String userhome = System.getProperty("user.home");
        String fileurl = userhome + "/" + getSaveDirectoryName() + "/init.data";
        JSONCreate.main(fileurl);
        old_json = JSONLoader.main(fileurl);
        HashMap<String, Object> values = new HashMap<String, Object>();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY HH:mm:ss");
        values.put("lastseen", sdf.format(cal.getTime()));
        if (old_json.containsKey("nickname") == false) {
            values.put("nickname", "");
        }
        if (old_json.containsKey("width") == false) {
            values.put("width", "800");
        } else if (Integer.parseInt(old_json.get("width").toString()) > width || valid_resolutions.containsKey(old_json.get("width").toString() + "x" + old_json.get("height").toString()) == false) {
            values.put("width", "800");
        }
        if (old_json.containsKey("height") == false) {
            values.put("height", "600");
        } else if (Integer.parseInt(old_json.get("height").toString()) > height || valid_resolutions.containsKey(old_json.get("width").toString() + "x" + old_json.get("height").toString()) == false) {
            values.put("height", "600");
        }
        if (old_json.containsKey("language") == false) {
            values.put("language", "EN");
        }
        if (old_json.containsKey("fullscreen") == false) {
            values.put("fullscreen", "false");
        } else if (!old_json.get("fullscreen").equals("true") && !old_json.get("fullscreen").equals("false")) {
            values.put("fullscreen", "false");
        }
        if (old_json.containsKey("gamma_correction") == false) {
            values.put("gamma_correction", "false");
        } else if (!old_json.get("gamma_correction").equals("true") && !old_json.get("gamma_correction").equals("false")) {
            values.put("gamma_correction", "false");
        }
        if (old_json.containsKey("swap_buffers") == false) {
            values.put("swap_buffers", "true");
        } else if (!old_json.get("swap_buffers").equals("true") && !old_json.get("swap_buffers").equals("false")) {
            values.put("swap_buffers", "true");
        }
        if (old_json.containsKey("fps") == false) {
            values.put("fps", "60");
        }
        if (old_json.containsKey("key_up") == false) {
            values.put("key_up", Integer.toString(CharacterBaseKeys.UP));
        }
        if (old_json.containsKey("key_down") == false) {
            values.put("key_down", Integer.toString(CharacterBaseKeys.DOWN));
        }
        if (old_json.containsKey("key_left") == false) {
            values.put("key_left", Integer.toString(CharacterBaseKeys.LEFT));
        }
        if (old_json.containsKey("key_right") == false) {
            values.put("key_right", Integer.toString(CharacterBaseKeys.RIGHT));
        }
        if (old_json.containsKey("key_jump") == false) {
            values.put("key_jump", Integer.toString(CharacterBaseKeys.JUMP));
        }
        if (old_json.containsKey("key_attack") == false) {
            values.put("key_attack", Integer.toString(CharacterBaseKeys.ATTACK));
        }
        JSONWrite.main(fileurl, values);
        json = JSONLoader.main(fileurl);
        settings.setWidth(Integer.parseInt(json.get("width").toString()));
        settings.setHeight(Integer.parseInt(json.get("height").toString()));
        settings.setGammaCorrection(Boolean.parseBoolean(json.get("gamma_correction").toString()));
        settings.setFullscreen(Boolean.parseBoolean(json.get("fullscreen").toString()));
        settings.setSwapBuffers(Boolean.parseBoolean(json.get("swap_buffers").toString()));
        settings.setFrameRate(Integer.parseInt(json.get("fps").toString()));
        controlSettings.setUP(Integer.parseInt(json.get("key_up").toString()));
        controlSettings.setDOWN(Integer.parseInt(json.get("key_down").toString()));
        controlSettings.setLEFT(Integer.parseInt(json.get("key_left").toString()));
        controlSettings.setRIGHT(Integer.parseInt(json.get("key_right").toString()));
        controlSettings.setJUMP(Integer.parseInt(json.get("key_jump").toString()));
        controlSettings.setATTACK(Integer.parseInt(json.get("key_attack").toString()));
        langManager = new LanguageManager();
        langManager.addLanguageData("/misat11/core/language/baseen.lang");
        if (!json.get("language").equals("EN")) {
            try {
                langManager.addLanguageData("/misat11/core/language/base" + json.get("language").toString().toLowerCase() + ".lang");
            } catch (Exception ex) {

            }
        }

    }

    public Resolution getValidResolution(String string) {
        return valid_resolutions.get(string);
    }

    public String[] getValidResolutions() {
        return valid_resolutions.keySet().toArray(new String[valid_resolutions.size()]);
    }

    public int getIdResolution(String string) {
        String[] array = valid_resolutions.keySet().toArray(new String[valid_resolutions.size()]);
        int id = -1;
        for (String s : array) {
            id++;
            if (s.equals(string)) {
                return id;
            }
        }
        return 0;
    }

    private void postInit() {
        JSONCreate.main(System.getProperty("user.home") + "/" + getSaveDirectoryName() + "/" + "downloaded.data", "[]");
        
        File downloadedcontent = new File(System.getProperty("user.home") + "/" + getSaveDirectoryName() + "/" + "DownloadedContent");
        downloadedcontent.mkdir();

        File mods = new File(System.getProperty("user.home") + "/" + getSaveDirectoryName() + "/" + "Mods");
        mods.mkdir();

        gameCore.setSaveurl(System.getProperty("user.home") + "/" + getSaveDirectoryName() + "/");
    }

    public class LauncherWindow extends JFrame {

        public LauncherWindow() {
            super(langManager.get("Game Launcher", "base.launcher.title"));
            setSize(640, 480);
            setResizable(false);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
        }
    }

    public String[] getValidLanguages() {
        return langManager.getLangCodes().values().toArray(new String[langManager.getLangCodes().size()]);
    }

    public int getIdLanguage(String string) {
        String v = langManager.getLangCodes().get(string);
        String[] array = langManager.getLangCodes().values().toArray(new String[langManager.getLangCodes().size()]);
        int id = -1;
        for (String s : array) {
            id++;
            if (s.equals(v)) {
                return id;
            }
        }
        return 0;
    }

    public void changeLanguageCodeByName(String locale) {
        for (Map.Entry<String, String> code : langManager.getLangCodes().entrySet()) {
            if (code.getValue().equals(locale)) {
                HashMap<String, Object> newsave = new HashMap<String, Object>();
                newsave.put("language", code.getKey());
                save("init.data", newsave);
                return;
            }
        }
    }
}

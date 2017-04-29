/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.crowbarwars.launcher;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import misat11.core.launcher.AbstractLauncher;
import misat11.crowbarwars.CrowbarWars;

/**
 *
 * @author misat11
 */
public class CrowbarWarsLauncher extends AbstractLauncher {

    public static void main(String[] args) {
        new CrowbarWarsLauncher();
    }

    @Override
    public void init() {
        gameCore = new CrowbarWars();
        
        
        gameCore.setDebug(true);
        
        addPanel("mainmenu", new JPanel(null));
        window.add(getPanel("mainmenu"));
        JLabel mainmenu_text1 = new JLabel("CrowbarWars", SwingConstants.CENTER);
        mainmenu_text1.setBounds(0, 30, 640, 20);
        getPanel("mainmenu").add(mainmenu_text1);
        JLabel mainmenu_text2 = new JLabel("Alpha  v0.1", SwingConstants.CENTER);
        mainmenu_text2.setBounds(0, 60, 640, 20);
        getPanel("mainmenu").add(mainmenu_text2);
        JButton mainmenu_start = new JButton(langManager.get("Start game", "base.launcher.start"));
        mainmenu_start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startByButton();
            }
        });
        mainmenu_start.setBounds(250, 90, 140, 20);
        getPanel("mainmenu").add(mainmenu_start);
        JButton mainmenu_settings = new JButton(langManager.get("Settings", "base.launcher.settings"));
        mainmenu_settings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.remove(getPanel("mainmenu"));
                window.add(getPanel("settings"));
                window.repaint();
                window.revalidate();
            }

        });
        mainmenu_settings.setBounds(250, 115, 140, 20);
        getPanel("mainmenu").add(mainmenu_settings);
        JButton mainmenu_exit = new JButton(langManager.get("Exit", "base.launcher.exit"));
        mainmenu_exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }

        });
        mainmenu_exit.setBounds(250, 140, 140, 20);
        getPanel("mainmenu").add(mainmenu_exit);

        addPanel("settings", new JPanel(null));
        JLabel settings_text1 = new JLabel(langManager.get("Settings", "base.launcher.settings"), SwingConstants.CENTER);
        settings_text1.setBounds(0, 30, 640, 20);
        getPanel("settings").add(settings_text1);
        JButton settings_video = new JButton(langManager.get("Video Settings", "base.launcher.video.title"));
        settings_video.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.remove(getPanel("settings"));
                window.add(getPanel("settings_video"));
                window.repaint();
                window.revalidate();
            }

        });
        settings_video.setBounds(250, 70, 140, 20);
        getPanel("settings").add(settings_video);
        JButton settings_controls = new JButton(langManager.get("Controls", "base.launcher.controls.title"));
        settings_controls.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }

        });
        settings_controls.setBounds(250, 95, 140, 20);
        getPanel("settings").add(settings_controls);
        JButton settings_lang = new JButton(langManager.get("Language", "base.launcher.language.title"));
        settings_lang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.remove(getPanel("settings"));
                window.add(getPanel("settings_language"));
                window.repaint();
                window.revalidate();
            }

        });
        settings_lang.setBounds(250, 120, 140, 20);
        getPanel("settings").add(settings_lang);
        JButton settings_exit = new JButton(langManager.get("Back", "base.launcher.back"));
        settings_exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.remove(getPanel("settings"));
                window.add(getPanel("mainmenu"));
                window.repaint();
                window.revalidate();
            }

        });
        settings_exit.setBounds(250, 145, 140, 20);
        getPanel("settings").add(settings_exit);

        addPanel("settings_video", new JPanel(null));
        JLabel settings_video_text1 = new JLabel(langManager.get("Video Settings", "base.launcher.video.title"), SwingConstants.CENTER);
        settings_video_text1.setBounds(0, 30, 640, 20);
        getPanel("settings_video").add(settings_video_text1);
        JLabel settings_video_resolution = new JLabel(langManager.get("Resolution", "base.launcher.video.screen_resolution") + ":", SwingConstants.CENTER);
        settings_video_resolution.setBounds(0, 60, 640, 20);
        getPanel("settings_video").add(settings_video_resolution);
        JComboBox settings_video_resolutionlist = new JComboBox(getValidResolutions());
        settings_video_resolutionlist.setSelectedIndex(getIdResolution(settings.getWidth() + "x" + settings.getHeight()));
        settings_video_resolutionlist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox) e.getSource();
                String selected = (String) cb.getSelectedItem();
                settings.setWidth(getValidResolution(selected).getWidth());
                settings.setHeight(getValidResolution(selected).getHeight());
                HashMap<String, String> newsave = new HashMap<String, String>();
                newsave.put("height", Integer.toString(getValidResolution(selected).getHeight()));
                newsave.put("width", Integer.toString(getValidResolution(selected).getWidth()));
                save("init.data", newsave);
            }
        });
        settings_video_resolutionlist.setBounds(250, 85, 140, 20);
        getPanel("settings_video").add(settings_video_resolutionlist);
        JCheckBox fullscreen = new JCheckBox(langManager.get("FullScreen", "base.launcher.video.fullscreen"));
        fullscreen.setBounds(250, 110, 140, 20);
        fullscreen.setSelected(settings.isFullscreen());
        fullscreen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBox cb = (JCheckBox) e.getSource();
                boolean selected = cb.isSelected();
                settings.setFullscreen(selected);
                HashMap<String, String> newsave = new HashMap<String, String>();
                newsave.put("fullscreen", Boolean.toString(selected));
                save("init.data", newsave);

            }
        });
        getPanel("settings_video").add(fullscreen);
        JCheckBox gamma_correction = new JCheckBox(langManager.get("Gamma Correction", "base.launcher.video.gamma_correction"));
        gamma_correction.setBounds(250, 135, 140, 20);
        gamma_correction.setSelected(settings.isGammaCorrection());
        gamma_correction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBox cb = (JCheckBox) e.getSource();
                boolean selected = cb.isSelected();
                settings.setGammaCorrection(selected);
                HashMap<String, String> newsave = new HashMap<String, String>();
                newsave.put("gamma_correction", Boolean.toString(selected));
                save("init.data", newsave);

            }
        });
        getPanel("settings_video").add(gamma_correction);
        JCheckBox swap_buffers = new JCheckBox(langManager.get("Swap Buffers", "base.launcher.video.swap_buffers"));
        swap_buffers.setBounds(250, 160, 140, 20);
        swap_buffers.setSelected(settings.isSwapBuffers());
        swap_buffers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBox cb = (JCheckBox) e.getSource();
                boolean selected = cb.isSelected();
                settings.setSwapBuffers(selected);
                HashMap<String, String> newsave = new HashMap<String, String>();
                newsave.put("swap_buffers", Boolean.toString(selected));
                save("init.data", newsave);

            }
        });
        getPanel("settings_video").add(swap_buffers);
        final JLabel fps_name = new JLabel(langManager.get("Max FPS", "base.launcher.video.fps") + "(" + settings.getFrameRate() + "):", SwingConstants.CENTER);
        fps_name.setBounds(0, 185, 640, 20);
        getPanel("settings_video").add(fps_name);
        JSlider fps = new JSlider(JSlider.HORIZONTAL, 0, 120, 60);
        if (settings.getFrameRate() == -1) {
            fps.setValue(0);
        } else {
            fps.setValue(settings.getFrameRate());
        }
        fps.setBounds(200, 210, 240, 20);
        fps.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider cb = (JSlider) e.getSource();
                int number = cb.getValue();
                if (number <= 0) {
                    number = -1;
                }
                fps_name.setText(langManager.get("Max FPS", "base.launcher.video.fps") + "(" + number + "):");
                settings.setFrameRate(number);
                HashMap<String, String> newsave = new HashMap<String, String>();
                newsave.put("fps", Integer.toString(number));
                save("init.data", newsave);
            }
        });
        getPanel("settings_video").add(fps);
        JButton settings_video_exit = new JButton(langManager.get("Back", "base.launcher.back"));
        settings_video_exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.remove(getPanel("settings_video"));
                window.add(getPanel("settings"));
                window.repaint();
                window.revalidate();
            }

        });
        settings_video_exit.setBounds(250, 235, 140, 20);
        getPanel("settings_video").add(settings_video_exit);
        if (getValidResolution("1920x1080") != null) {
            JLabel fullscreen_warning = new JLabel(langManager.get("Do not use the FullScreen at 1920x1080 resolution.", "base.launcher.video.fullscreen_warning"), SwingConstants.CENTER);
            fullscreen_warning.setBounds(0, 280, 640, 20);
            getPanel("settings_video").add(fullscreen_warning);
        }

        addPanel("settings_language", new JPanel(null));
        JLabel settings_language_title = new JLabel(langManager.get("Language", "base.launcher.language.title"), SwingConstants.CENTER);
        settings_language_title.setBounds(0, 30, 640, 20);
        getPanel("settings_language").add(settings_language_title);
        final JComboBox settings_language = new JComboBox(getValidLanguages());
        settings_language.setSelectedIndex(getIdLanguage(json.get("language").toString()));
        settings_language.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });
        settings_language.setBounds(250, 60, 140, 20);
        getPanel("settings_language").add(settings_language);
        JButton settings_language_save = new JButton(langManager.get("Save", "base.launcher.language.save"));
        settings_language_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = (String) settings_language.getSelectedItem();
                changeLanguageCodeByName(selected);
                reloadLauncher();
            }

        });
        settings_language_save.setBounds(250, 85, 140, 20);
        getPanel("settings_language").add(settings_language_save);
        JButton settings_language_exit = new JButton(langManager.get("Back", "base.launcher.back"));
        settings_language_exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.remove(getPanel("settings_language"));
                window.add(getPanel("settings"));
                window.repaint();
                window.revalidate();
            }

        });
        settings_language_exit.setBounds(250, 110, 140, 20);
        getPanel("settings_language").add(settings_language_exit);
    }

    @Override
    public String getSaveDirectoryName() {
        return "CrowbarWars";
    }

    @Override
    public void load() {
        main(new String[0]);
    }

}

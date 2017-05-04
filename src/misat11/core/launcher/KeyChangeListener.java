/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.launcher;

import com.jme3.input.KeyInput;
import com.jme3.input.awt.AwtKeyInput;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import misat11.core.json.JSONWrite;
import misat11.core.keyboard.ControlSettings;

/**
 *
 * @author misat11
 */
public class KeyChangeListener implements KeyListener {

    private ControlSettings settings;
    private String tochange;
    private String fileurl;
    private boolean isbutton = false;
    private JButton button;

    public KeyChangeListener(ControlSettings settings, String tochange, String fileurl) {
        this.settings = settings;
        this.tochange = tochange;
        this.fileurl = fileurl;
    }

    public KeyChangeListener(JButton button, ControlSettings settings, String tochange, String fileurl) {
        this.settings = settings;
        this.tochange = tochange;
        this.fileurl = fileurl;
        this.isbutton = true;
        this.button = button;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = AwtKeyInput.convertAwtKey(e.getKeyCode());
        if (tochange.equals("UP") && code != KeyInput.KEY_ESCAPE) {
            settings.setUP(code);
            JSONWrite.main(fileurl, "key_up", Integer.toString(code));
            if (isbutton) {
                button.removeKeyListener(this);
                button.setText(Integer.toString(code));
            }
        } else if (tochange.equals("DOWN") && code != KeyInput.KEY_ESCAPE) {
            settings.setDOWN(code);
            JSONWrite.main(fileurl, "key_down", Integer.toString(code));
            if (isbutton) {
                button.removeKeyListener(this);
                button.setText(Integer.toString(code));
            }
        } else if (tochange.equals("LEFT") && code != KeyInput.KEY_ESCAPE) {
            settings.setLEFT(code);
            JSONWrite.main(fileurl, "key_left", Integer.toString(code));
            if (isbutton) {
                button.removeKeyListener(this);
                button.setText(Integer.toString(code));
            }
        } else if (tochange.equals("RIGHT") && code != KeyInput.KEY_ESCAPE) {
            settings.setRIGHT(code);
            JSONWrite.main(fileurl, "key_right", Integer.toString(code));
            if (isbutton) {
                button.removeKeyListener(this);
                button.setText(Integer.toString(code));
            }
        } else if (tochange.equals("JUMP") && code != KeyInput.KEY_ESCAPE) {
            settings.setJUMP(code);
            JSONWrite.main(fileurl, "key_jump", Integer.toString(code));
            if (isbutton) {
                button.removeKeyListener(this);
                button.setText(Integer.toString(code));
            }
        } else if (tochange.equals("ATTACK") && code != KeyInput.KEY_ESCAPE) {
            settings.setATTACK(code);
            JSONWrite.main(fileurl, "key_attack", Integer.toString(code));
            if (isbutton) {
                button.removeKeyListener(this);
                button.setText(Integer.toString(code));
            }
        }
    }

}

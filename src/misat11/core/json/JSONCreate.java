/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.json;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import misat11.core.launcher.AbstractLauncher;

/**
 *
 * @author misat11
 */
public class JSONCreate {

    @SuppressWarnings("unchecked")
    public static void main(String file) {
        String userhome = System.getProperty("user.home");
        File f = new File(file);
        if ((f.exists() && !f.isDirectory()) == false) {
            try {
                f.getParentFile().mkdirs();
                f.createNewFile();
                JSONWrite.main(file, "{}");
            } catch (IOException ex) {
                Logger.getLogger(AbstractLauncher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public static void main(String file, String base) {
        String userhome = System.getProperty("user.home");
        File f = new File(file);
        if ((f.exists() && !f.isDirectory()) == false) {
            try {
                f.getParentFile().mkdirs();
                f.createNewFile();
                JSONWrite.main(file, base);
            } catch (IOException ex) {
                Logger.getLogger(AbstractLauncher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

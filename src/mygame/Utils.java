/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.network.serializing.Serializer;

/**
 *
 * @author misat11
 */
public class Utils {
    public static final int BASE_PORT = 4444;
    
    public static void initSerializer(){
        Serializer.registerClass(TextMessage.class);
    }
}

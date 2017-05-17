/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core;

import com.jme3.network.serializing.Serializer;
import misat11.core.server.messages.CameraLookMessage;
import misat11.core.server.messages.ChangeEntityLocationMessage;
import misat11.core.server.messages.ChangeObjectLocationMessage;
import misat11.core.server.messages.ClientHasMessage;
import misat11.core.server.messages.DespawnEntityMessage;
import misat11.core.server.messages.DespawnObjectMessage;
import misat11.core.server.messages.MoveMessage;
import misat11.core.server.messages.ServerInfoMessage;
import misat11.core.server.messages.TextMessage;
import misat11.core.server.messages.PlayerSettingsMessage;
import misat11.core.server.messages.ServerWantClientHasMessage;
import misat11.core.server.messages.SpawnEntityMessage;
import misat11.core.server.messages.SpawnObjectMessage;

/**
 *
 * @author misat11
 */
public class Utils {
    public static final int BASE_PORT = 4444;
    public static final int PROTOCOL = 3;
    public static final String BASE_GAMEHASHCODE = "crbar_multi";
    
    public static void initSerializer(){
        Serializer.registerClass(TextMessage.class);
        Serializer.registerClass(ServerInfoMessage.class);
        Serializer.registerClass(MoveMessage.class);
        Serializer.registerClass(PlayerSettingsMessage.class);
        Serializer.registerClass(ChangeEntityLocationMessage.class);
        Serializer.registerClass(ChangeObjectLocationMessage.class);
        Serializer.registerClass(SpawnObjectMessage.class);
        Serializer.registerClass(SpawnEntityMessage.class);
        Serializer.registerClass(DespawnObjectMessage.class);
        Serializer.registerClass(DespawnEntityMessage.class);
        Serializer.registerClass(ClientHasMessage.class);
        Serializer.registerClass(CameraLookMessage.class);
        Serializer.registerClass(ServerWantClientHasMessage.class);
    }
}

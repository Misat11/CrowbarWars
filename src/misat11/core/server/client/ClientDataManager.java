/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server.client;

import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import misat11.core.AbstractCore;
import misat11.core.character.AbstractCharacter;
import misat11.core.character.SimpleCharacter;
import misat11.core.events.ConnectionEvent;
import misat11.core.object.AbstractObject;
import misat11.core.object.HeadText;
import misat11.core.object.SimpleSpatialObject;
import misat11.core.server.messages.PlayerData;

/**
 *
 * @author misat11
 */
public class ClientDataManager {

    private AbstractCore main;
    private ConnectionEvent connection;
    private HashMap<Integer, PlayerData> player = new HashMap<Integer, PlayerData>();
    private HashMap<Integer, Integer> playerObjects = new HashMap<Integer, Integer>();
    private HashMap<Integer, AbstractCharacter> characters = new HashMap<Integer, AbstractCharacter>();
    private HashMap<Integer, Float> airTimes = new HashMap<Integer, Float>();
    private HashMap<Integer, Integer> headtext = new HashMap<Integer, Integer>();
    private int myId;

    public ClientDataManager(AbstractCore main, ConnectionEvent connection) {
        this.main = main;
        this.connection = connection;
    }

    public int getMyId() {
        return myId;
    }

    public void setMyId(int myId) {
        this.myId = myId;
    }

    public int getObjectId(int id) {
        return playerObjects.get(id);
    }

    public boolean isTherePlayerWithId(int id) {
        return player.containsKey(id) && playerObjects.containsKey(id) && characters.containsKey(id);
    }

    public HashMap getPlayerList() {
        return player;
    }

    public boolean isThereCharacterWithId(int id) {
        return characters.containsKey(id);
    }

    public AbstractCharacter getCharacter(int id) {
        return characters.get(id);
    }

    public void addCharacter(int id, AbstractCharacter character) {
        characters.put(id, character);
    }

    public Set<Integer> getPlayerIdList() {
        return player.keySet();
    }

    public PlayerData getPlayerData(int id) {
        return player.get(id);
    }

    public void refreshPlayerList(HashMap<Integer, PlayerData> player) {
        Set<Integer> oldPlayers = this.player.keySet();
        this.player.putAll(player);
        for (int old : oldPlayers) {
            if (player.containsKey(old) == false) {
                removePlayer(old);
            }
        }
        updateAllPlayerEntities();
    }

    private void removePlayer(int id) {
        if (playerObjects.containsKey(id)) {
            final int objid = playerObjects.get(id);
            if (characters.containsKey(id)) {
                main.getObject(objid).getSpatial().removeControl(characters.get(id).getControl());
                characters.remove(id);
            }
            main.enqueue(new Callable() {
                @Override
                public Object call() throws Exception {
                    main.getBulletAppState().getPhysicsSpace().removeAll(main.objects.get(objid).getSpatial());
                    return null;
                }
            });
            main.detachObject(objid);
            playerObjects.remove(id);
        }
        if (headtext.containsKey(id)) {
            main.detachObject(headtext.get(id));
            headtext.remove(id);
        }
        if (player.containsKey(id)) {
            player.remove(id);
        }
    }

    private void updateAllPlayerEntities() {
        Set<Map.Entry<Integer, PlayerData>> set = player.entrySet();
        Iterator<Map.Entry<Integer, PlayerData>> it = set.iterator();

        while (it.hasNext()) {
            Map.Entry<Integer, PlayerData> e = it.next();
            updatePlayerEntity(e.getKey(), e.getValue());
        }
    }

    private void updatePlayerEntity(final int id, final PlayerData data) {
        main.enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                if (playerObjects.containsKey(id) == false) {
                    final int obj_id = main.gameRegisterObject(new SimpleSpatialObject(main.getAssetManager().loadModel(data.getModelAsset())));
                    main.attachObject(obj_id);
                    playerObjects.put(id, obj_id);
                }
                if (characters.containsKey(id) == false) {
                    characters.put(id, new SimpleCharacter(main, playerObjects.get(id)));
                }
                if (headtext.containsKey(id) == false) {
                    BoundingBox box
                            = (BoundingBox) main.getObject(playerObjects.get(id)).getSpatial().getWorldBound();
                    Vector3f extent = box.getExtent(null);
                    Vector3f height = new Vector3f(0, extent.y + 1, 0);
                    headtext.put(id,
                            main.gameRegisterObject(new HeadText(main, main.getGuiFont(),
                                    main.getObject(playerObjects.get(id)).getSpatial().getLocalTranslation(),
                                    player.get(id).getName(), height)));
                    main.attachObject(headtext.get(id));
                }
                AbstractObject obj = main.getObject(playerObjects.get(id));

                if (myId != id) {

                    characters.get(id).setWalkDirection(data.getWalkDirection());
                    characters.get(id).warp(data.getLocation());
                    characters.get(id).setViewDirection(data.getViewDirection());

                    Vector3f walkDirection = characters.get(id).getControl().getWalkDirection();
                    Vector3f viewDirection = characters.get(id).getControl().getWalkDirection();
                    if (airTimes.containsKey(id) == false) {
                        airTimes.put(id, 0f);
                    }
                    if (!characters.get(id).getControl().onGround()) {
                        airTimes.put(id, airTimes.get(id) + main.tpf);
                    } else {
                        airTimes.put(id, 0f);
                    }
                    if (walkDirection.lengthSquared() == 0) {
                        characters.get(id).runAnimation("Stand");
                    } else {
                        characters.get(id).setViewDirection(walkDirection);
                        if (airTimes.get(id) > .3f) {
                            characters.get(id).runAnimation("Stand");
                        } else {
                            characters.get(id).runAnimation("Walk");
                        }
                    }
                }
                BoundingBox box = (BoundingBox) main.getObject(playerObjects.get(id)).getSpatial().getWorldBound();
                Vector3f extent = box.getExtent(null);
                Vector3f height = new Vector3f(0, extent.y + 1, 0);
                HeadText head_text = (HeadText) main.getObject(headtext.get(id));
                head_text.updateLocation(obj.getSpatial().getLocalTranslation(),
                        height);
                head_text.lookAt(main.getCamera().getLocation());
                return null;
            }

        });

    }
}

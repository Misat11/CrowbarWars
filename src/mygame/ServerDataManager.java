/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 *
 * @author misat11
 */
public class ServerDataManager {

    private HashMap<Integer, PlayerData> player = new HashMap<Integer, PlayerData>();
    private HashMap<Integer, Spatial> entities = new HashMap<Integer, Spatial>();
    private AssetManager assetManager;
    private int myId;
    private BulletAppState bulletAppState;
    private Main main;

    public ServerDataManager(Main main, AssetManager assetManager, BulletAppState bulletAppState) {
        this.assetManager = assetManager;
        this.main = main;
        this.bulletAppState = bulletAppState;
    }

    public void addOrRefreshplayer(int id, PlayerData data) {
        player.put(id, data);
    }

    public void removePlayer(final int id) {
        if (player.containsKey(id)) {
            player.remove(id);
        }
        if (entities.containsKey(id)) {
            main.enqueue(new Callable() {
                @Override
                public Object call() throws Exception {
                    main.getRootNode().detachChild(entities.get(id));
                    bulletAppState.getPhysicsSpace().remove(entities.get(id));
                    entities.remove(id);
                    return null;
                }

            });
        }
    }

    public boolean isTherePlayerWithId(int id) {
        return player.containsKey(id);
    }

    public HashMap getPlayerList() {
        return player;
    }

    public HashMap getEntityList() {
        return entities;
    }

    public PlayerData getPlayerData(int id) {
        return player.get(id);
    }

    public Spatial getEntityData(int id) {
        return entities.get(id);
    }

    public void refreshPlayerList(HashMap players) {
        this.player.putAll(players);
        updateAllPlayerEntities();
    }

    private void updateAllPlayerEntities() {
        Set<Entry<Integer, PlayerData>> set = player.entrySet();
        Iterator<Entry<Integer, PlayerData>> it = set.iterator();

        while (it.hasNext()) {
            Entry<Integer, PlayerData> e = it.next();
            updataPlayerEntity(e.getKey(), e.getValue());
        }
    }

    private void updataPlayerEntity(final int id, final PlayerData data) {

        if (myId != id) {
            if (!entities.containsKey(id)) {
                loadAndAddPlayerToRootNode(id, data);
                return;
            }
            main.enqueue(new Callable() {
                @Override
                public Object call() throws Exception {
                    entities.get(id).getControl(CharacterControl.class).setWalkDirection(data.getWalkDirection());
                    entities.get(id).getControl(CharacterControl.class).warp(data.getLocation());
                    entities.get(id).setLocalRotation(data.getRotation());
                    return null;
                }
            });
        }

    }

    public void setMyId(int id) {
        this.myId = id;
    }

    public int getMyId() {
        return myId;
    }

    private void loadAndAddPlayerToRootNode(final int id, PlayerData data) {
        entities.put(id, loadSpatial(data.getLocation(), data.getRotation(), data.getModelAsset()));
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(0.6f, 1.8f);
        CharacterControl control = new CharacterControl(capsuleShape, 0.01f);
        entities.get(id).addControl(control);
        control.setGravity(10f);
        control.setJumpSpeed(20f);
        control.warp(data.getLocation());

        main.enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                main.getRootNode().attachChild(entities.get(id));
                bulletAppState.getPhysicsSpace().addAll(entities.get(id));
                return null;
            }
        });
    }

    private Spatial loadSpatial(Vector3f location, Quaternion rotation, String modelAsset) {
        Spatial spatial = assetManager.loadModel(modelAsset);
        spatial.setLocalTranslation(location);
        spatial.setLocalRotation(rotation);
        return spatial;
    }
}

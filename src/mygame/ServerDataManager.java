/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitorAdapter;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.ProgressBar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import mygame.Main;
import mygame.PlayerData;

/**
 *
 * @author misat11
 */
public class ServerDataManager {

    private HashMap<Integer, PlayerData> player = new HashMap<Integer, PlayerData>();
    private HashMap<Integer, Spatial> entities = new HashMap<Integer, Spatial>();
    private HashMap<Integer, BitmapText> headtext = new HashMap<Integer, BitmapText>();
    private HashMap<Integer, List<AnimControl>> animControls = new HashMap<Integer, List<AnimControl>>();
    private HashMap<Integer, List<AnimChannel>> animChannels = new HashMap<Integer, List<AnimChannel>>();
    private HashMap<Integer, Float> airTimes = new HashMap<Integer, Float>();
    private ProgressBar healthbar;
    private AssetManager assetManager;
    private int myId;
    private BulletAppState bulletAppState;
    private Main main;
    private BitmapFont guiFont;
    private Camera cam;

    public ServerDataManager(Main main, AssetManager assetManager, BulletAppState bulletAppState, BitmapFont guiFont, Camera cam) {
        this.assetManager = assetManager;
        this.main = main;
        this.bulletAppState = bulletAppState;
        this.guiFont = guiFont;
        this.cam = cam;
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
                    entities.get(id).removeControl(CharacterControl.class);
                    bulletAppState.getPhysicsSpace().removeAll(entities.get(id));
                    main.getRootNode().detachChild(entities.get(id));
                    entities.remove(id);
                    return null;
                }
            });
        }
        if (headtext.containsKey(id)) {
            main.enqueue(new Callable() {
                @Override
                public Object call() throws Exception {
                    main.getRootNode().detachChild(headtext.get(id));
                    headtext.remove(id);
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

    public Set<Integer> getPlayerIdList() {
        return player.keySet();
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
        Set<Integer> oldPlayers = this.player.keySet();
        this.player.putAll(players);
        for (int old : oldPlayers) {
            if (this.player.containsKey(old) == false) {
                removePlayer(old);
            }
        }
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

        if (!entities.containsKey(id)) {
            loadAndAddPlayerToRootNode(id, data);
            return;
        }
        main.enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                entities.get(id).getControl(CharacterControl.class).setWalkDirection(data.getWalkDirection());
                entities.get(id).getControl(CharacterControl.class).warp(data.getLocation());
                entities.get(id).getControl(CharacterControl.class).setViewDirection(data.getViewDirection());
                entities.get(id).setLocalRotation(data.getRotation());

                if (myId != id) {
                    Vector3f walkDirection = entities.get(id).getControl(CharacterControl.class).getWalkDirection();
                    Vector3f viewDirection = entities.get(id).getControl(CharacterControl.class).getWalkDirection();
                    if (airTimes.containsKey(id) == false) {
                        airTimes.put(id, 0f);
                    }
                    if (!entities.get(id).getControl(CharacterControl.class).onGround()) {
                        airTimes.put(id, airTimes.get(id) + main.tpf);
                    } else {
                        airTimes.put(id, 0f);
                    }
                    if (walkDirection.lengthSquared() == 0) {
                        setAnimation(id, "Stand");
                    } else {
                        entities.get(id).getControl(CharacterControl.class).setViewDirection(walkDirection);
                        if (airTimes.get(id) > .3f) {
                            setAnimation(id, "Stand");
                        } else {
                            setAnimation(id, "Walk");
                        }
                    }
                }

                BoundingBox box = (BoundingBox) entities.get(id).getWorldBound();
                Vector3f extent = box.getExtent(null);
                BoundingBox box2 = (BoundingBox) headtext.get(id).getWorldBound();
                float x = (box2.getXExtent() / 2) * (-1);
                Vector3f height = new Vector3f(x, extent.y + 1, 0);
                headtext.get(id).setLocalTranslation(entities.get(id).getLocalTranslation().add(height));
                headtext.get(id).lookAt(cam.getLocation(), Vector3f.UNIT_Y);

                if(myId == id){
                    main.health.setProgressPercent(data.getHealth()/100);
                }
                
                return null;
            }
        });

    }

    public void setMyId(int id) {
        this.myId = id;
    }

    public int getMyId() {
        return myId;
    }

    private void loadAndAddPlayerToRootNode(final int id, PlayerData data) {
        entities.put(id, loadSpatial(data.getLocation(), data.getRotation(), data.getModelAsset()));
        BoundingBox box = (BoundingBox) entities.get(id).getWorldBound();
        Vector3f extent = box.getExtent(null);
        Vector3f height = new Vector3f(0, extent.y + 1, 0);
        headtext.put(id, loadText(data.getLocation(), data.getName(), height));

        if (myId != id) {
            setAnimControls(id, entities.get(id));
            CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(0.6f, 1.8f);
            CharacterControl control = new CharacterControl(capsuleShape, 0.09f);
            entities.get(id).addControl(control);
            control.setGravity(40f);
            control.setJumpSpeed(15f);
            control.warp(data.getLocation());
        }

        main.enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                main.getRootNode().attachChild(entities.get(id));
                main.getRootNode().attachChild(headtext.get(id));
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

    private BitmapText loadText(Vector3f location, String text_string, Vector3f add) {
        BitmapText text = new BitmapText(guiFont, false);
        text.setText(text_string);
        text.setSize(0.3f);
        BoundingBox box = (BoundingBox) text.getWorldBound();
        float x = (box.getXExtent() / 2) * (-1);
        text.setLocalTranslation(location.add(add.add(new Vector3f(x, 0, 0))));
        return text;
    }

    private void setAnimControls(final int id, Spatial spatial) {
        if (spatial == null) {
            if (animControls.get(id) != null) {
                for (Iterator<AnimControl> it = animControls.get(id).iterator(); it.hasNext();) {
                    AnimControl animControl = it.next();
                    animControl.clearChannels();
                }
            }
            animControls.put(id, null);
            animChannels.put(id, null);
            return;
        }
        SceneGraphVisitorAdapter visitor = new SceneGraphVisitorAdapter() {

            @Override
            public void visit(Geometry geom) {
                super.visit(geom);
                checkForAnimControl(geom);
            }

            @Override
            public void visit(Node geom) {
                super.visit(geom);
                checkForAnimControl(geom);
            }

            private void checkForAnimControl(Spatial geom) {
                AnimControl control = geom.getControl(AnimControl.class);
                if (control == null) {
                    return;
                }
                if (animControls.get(id) == null) {
                    animControls.put(id, new LinkedList<AnimControl>());
                }
                if (animChannels.get(id) == null) {
                    animChannels.put(id, new LinkedList<AnimChannel>());
                }

                animControls.get(id).add(control);
                animChannels.get(id).add(control.createChannel());
            }
        };
        spatial.depthFirstTraversal(visitor);
    }

    private void setAnimation(int id, String name) {
        if (animChannels.get(id) != null) {
            for (Iterator<AnimChannel> it = animChannels.get(id).iterator(); it.hasNext();) {
                AnimChannel animChannel = it.next();
                if (animChannel.getAnimationName() == null || !animChannel.getAnimationName().equals(name)) {
                    animChannel.setAnim(name);
                    if (animChannel.getControl().getAnim(name) != null) {
                    }
                }
            }
        }
    }
}

package misat11.core.server.client;

import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.HashMap;
import misat11.core.AbstractCore;
import misat11.core.character.AbstractCharacter;
import misat11.core.character.MultiplayerCharacter;
import misat11.core.events.ConnectionEvent;
import misat11.core.object.HeadText;
import misat11.core.object.SimpleSpatialObject;
import misat11.core.server.messages.ChangeEntityLocationMessage;
import misat11.core.server.messages.ChangeObjectLocationMessage;
import misat11.core.server.messages.ClientHasMessage;
import misat11.core.server.messages.DespawnEntityMessage;
import misat11.core.server.messages.DespawnObjectMessage;
import misat11.core.server.messages.SpawnEntityMessage;
import misat11.core.server.messages.SpawnObjectMessage;

/**
 *
 * @author misat11
 */
public class ClientDataManager {
    
    private AbstractCore main;
    private ConnectionEvent connection;
    private Client client;
    private HashMap<Integer, Integer> objects = new HashMap<Integer, Integer>();
    private HashMap<Integer, AbstractCharacter> entities = new HashMap<Integer, AbstractCharacter>();
    private HashMap<Integer, Integer> headtext = new HashMap<Integer, Integer>();
    private int myId;
    
    public ClientDataManager(Client client, AbstractCore main, ConnectionEvent connection) {
        this.client = client;
        this.main = main;
        this.connection = connection;
        connection.getView().setLocation(new Vector3f(0f, 60f, 0f));
    }
    
    public int getMyId() {
        return myId;
    }
    
    public void setMyId(int myId) {
        this.myId = myId;
    }
    
    public void spawnEntity(SpawnEntityMessage msg) {
        if (!entities.containsKey(msg.getId())) {
            Spatial spatial = main.getAssetManager().loadModel(msg.getAssetUrl());
            SimpleSpatialObject obj = new SimpleSpatialObject(spatial);
            int objid = main.gameRegisterObject(obj);
            main.attachObject(objid);
            MultiplayerCharacter character = new MultiplayerCharacter(main, objid);
            character.warp(msg.getLocation());
            entities.put(msg.getId(), character);
            
            BoundingBox box
                    = (BoundingBox) main.getObject(entities.get(msg.getId()).getObjectId()).getSpatial().getWorldBound();
            Vector3f extent = box.getExtent(null);
            Vector3f height = new Vector3f(0, extent.y + 1, 0);
            headtext.put(msg.getId(),
                    main.gameRegisterObject(new HeadText(main, main.getGuiFont(),
                            main.getObject(entities.get(msg.getId()).getObjectId()).getSpatial().getLocalTranslation(),
                            msg.getHeadText(), height)));
            main.attachObject(headtext.get(msg.getId()));
        }
    }
    
    public void despawnEntity(DespawnEntityMessage msg) {
        if (entities.containsKey(msg.getId())) {
            main.getObject(entities.get(msg.getId()).getObjectId()).getSpatial().removeControl(entities.get(msg.getId()).getControl());
            main.detachObject(entities.get(msg.getId()).getObjectId());
            main.getBulletAppState().getPhysicsSpace().remove(entities.get(msg.getId()).getControl());
            entities.remove(msg.getId());
        }
    }
    
    public void teleportEntity(ChangeEntityLocationMessage msg) {
        if (entities.containsKey(msg.getId())) {
            entities.get(msg.getId()).warp(msg.getLocation());
            entities.get(msg.getId()).runAnimation(msg.getActualanim());
            entities.get(msg.getId()).setViewDirection(msg.getView());
            
            BoundingBox box = (BoundingBox) main.getObject(entities.get(msg.getId()).getObjectId()).getSpatial().getWorldBound();
            Vector3f extent = box.getExtent(null);
            Vector3f height = new Vector3f(0, extent.y + 1, 0);
            HeadText head_text = (HeadText) main.getObject(headtext.get(msg.getId()));
            head_text.updateLocation(main.getObject(entities.get(msg.getId()).getObjectId()).getSpatial().getLocalTranslation(),
                    height);
            head_text.lookAt(main.getCamera().getLocation());
        }
    }
    
    public void spawnObject(SpawnObjectMessage msg) {
        if (!objects.containsKey(msg.getId())) {
            if (msg.isWithspatial()) {
                SimpleSpatialObject obj = new SimpleSpatialObject(msg.getSpatial());
                obj.getSpatial().addControl(new RigidBodyControl(msg.getMass()));
                int objid = main.gameRegisterObject(obj);
                objects.put(msg.getId(), objid);
                main.attachObject(objid);
            } else {
                SimpleSpatialObject obj = new SimpleSpatialObject(main.getAssetManager().loadModel(msg.getAssetUrl()));
                int objid = main.gameRegisterObject(obj);
                objects.put(msg.getId(), objid);
                main.attachObject(objid);
            }
        }
    }
    
    public void despawnObject(DespawnObjectMessage msg) {
        if (objects.containsKey(msg.getId())) {
            main.detachObject(msg.getId());
            objects.remove(msg.getId());
        }
    }
    
    public void teleportObject(ChangeObjectLocationMessage msg) {
        if (objects.containsKey(msg.getId())) {
            main.getObject(objects.get(msg.getId())).getSpatial().getControl(RigidBodyControl.class).setPhysicsLocation(msg.getLocation());
            main.getObject(objects.get(msg.getId())).getSpatial().getControl(RigidBodyControl.class).setPhysicsRotation(msg.getRotation());
        }
    }
    
    public void look(Vector3f location) {
        connection.getView().setLocation(location);
    }
    
    public void sendClientHasMessage() {
        client.send(new ClientHasMessage(getObjectIdList(), getEntityIdList()));
    }
    
    private ArrayList<Integer> getEntityIdList() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (Integer key : entities.keySet()) {
            list.add(key);
        }
        return list;
    }
    
    private ArrayList<Integer> getObjectIdList() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (Integer key : objects.keySet()) {
            list.add(key);
        }
        return list;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.crowbarwars.events;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import java.util.Iterator;
import misat11.core.AbstractCore;
import misat11.core.character.SimpleCharacter;
import misat11.core.events.AbstractEvent;
import misat11.core.keyboard.CharacterMove;
import misat11.core.object.BoxGeometry;
import misat11.core.object.GravityObject;
import misat11.core.object.SimpleSpatialObject;
import misat11.core.object.SphereGeometry;
import misat11.crowbarwars.panels.SinglePlayerPause;
import misat11.crowbarwars.keyboard.SinglePlayerPauseAction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author misat11
 */
public class SingleplayerEvent extends AbstractEvent {

    public JSONObject map;
    public String playerAsset;
    public int terrain_id;
    public int player_id;
    public SimpleCharacter control;
    public CharacterMove move;
    public int move_id;
    public int pausemenu_id;
    public int escapeaction_id;
    public boolean paused = false;

    public SingleplayerEvent(AbstractCore main, JSONObject map, String playerAsset) {
        super(main);
        this.map = map;
        this.playerAsset = playerAsset;
    }

    @Override
    public void start() {
        main.writeDebug("Loading map...");
        pausemenu_id = main.gameRegisterPanel(new SinglePlayerPause(main, this));
        escapeaction_id = main.registerKeyBoardAction(new SinglePlayerPauseAction(main, this));
        main.constructKeyBoardAction(escapeaction_id);
        String name = map.get("name").toString();
        String author = map.get("author").toString();
        String terrainAsset = map.get("terrain").toString();
        GravityObject terrain = new GravityObject(main, new SimpleSpatialObject(main.getAssetManager().loadModel(terrainAsset)), new RigidBodyControl(0.0f));
        terrain_id = main.gameRegisterObject(terrain);
        main.attachObject(terrain_id);
        SimpleSpatialObject player = new SimpleSpatialObject(main.getAssetManager().loadModel(playerAsset));
        player_id = main.gameRegisterObject(player);
        main.attachObject(player_id);

        control = new SimpleCharacter(main, player_id);
        main.setChasecam(player_id);
        move = new CharacterMove(main, control);
        move_id = main.registerKeyBoardAction(move);
        main.constructKeyBoardAction(move_id);
        boolean isSpecial = false;
        if (map.get("special") != null) {
            isSpecial = true;
        }

        if (isSpecial == true) {
            JSONArray specialContent = (JSONArray) map.get("special");
            Iterator i = specialContent.iterator();

            while (i.hasNext()) {
                try {
                    JSONObject special = (JSONObject) i.next();
                    String special_name = special.get("name").toString();
                    String special_type = special.get("type").toString();
                    float loc_x = Float.parseFloat(special.get("loc_x").toString());
                    float loc_y = Float.parseFloat(special.get("loc_y").toString());
                    float loc_z = Float.parseFloat(special.get("loc_z").toString());
                    Quaternion rotation = new Quaternion();
                    Vector3f location = new Vector3f(loc_x, loc_y, loc_z);
                    if (special_type.equals("spatial")) {
                        float gravity = Float.parseFloat(special.get("gravity").toString());
                        String modelAsset = special.get("modelAsset").toString();
                        GravityObject obj = new GravityObject(main, new SimpleSpatialObject(main.getAssetManager().loadModel(modelAsset)), new RigidBodyControl(gravity));
                        int obj_id = main.gameRegisterObject(obj);
                        main.attachObject(obj_id);
                    } else if (special_type.equals("geometry")) {
                        String geometry = special.get("geometry").toString();
                        String material = special.get("material").toString();
                        String texture = special.get("texture").toString();
                        String texture_type = special.get("texture_type").toString();
                        float gravity = Float.parseFloat(special.get("gravity").toString());
                        if (geometry.equals("box")) {
                            float geometry_x = Float.parseFloat(special.get("geometry_x").toString());
                            float geometry_y = Float.parseFloat(special.get("geometry_y").toString());
                            float geometry_z = Float.parseFloat(special.get("geometry_z").toString());
                            Vector3f size = new Vector3f(geometry_x, geometry_y, geometry_z);
                            BoxGeometry geom = new BoxGeometry(size, location, rotation, material, texture, texture_type);
                            RigidBodyControl grav = new RigidBodyControl(gravity);
                            GravityObject obj = new GravityObject(main, geom, grav);
                            int obj_id = main.gameRegisterObject(obj);
                            main.attachObject(obj_id);
                        } else if (geometry.equals("sphere")) {
                            float radius = Float.parseFloat(special.get("radius").toString());
                            int radialSamples = Integer.parseInt(special.get("radialSamples").toString());
                            int zSamples = Integer.parseInt(special.get("zSamples").toString());
                            SphereGeometry geom = new SphereGeometry(radius, radialSamples, zSamples, location, rotation, material, texture, texture_type);
                            RigidBodyControl grav = new RigidBodyControl(gravity);
                            GravityObject obj = new GravityObject(main, geom, grav);
                            int obj_id = main.gameRegisterObject(obj);
                            main.attachObject(obj_id);
                        }
                    }
                } catch (Exception e) {

                }
            }
        }

    }

    @Override
    public void update() {
        control.update();
    }

    @Override
    public void end() {
        
    }

    public void pause() {
        if (paused == false) {
            main.attachPanel(pausemenu_id);
            main.destroyKeyBoardAction(move_id);
            paused = true;
        } else if (paused == true) {
            main.detachPanel(pausemenu_id);
            main.constructKeyBoardAction(move_id);
            paused = false;
        }
    }
}

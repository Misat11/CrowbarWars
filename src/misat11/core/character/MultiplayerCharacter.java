/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.character;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import misat11.core.AbstractCore;

/**
 *
 * @author misat11
 */
public class MultiplayerCharacter extends AbstractCharacter {

    public MultiplayerCharacter(AbstractCore main, int objectId) {
        super(main, objectId);
        CapsuleCollisionShape collisionShape = new CapsuleCollisionShape(0.6f, 2f);
        control = new CharacterControl(collisionShape, 0.09f);
        main.getObject(this.objectId).getSpatial().addControl(control);
    }

    @Override
    public void update(float tpf) {
        
    }

}

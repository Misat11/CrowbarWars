/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.character;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import java.util.concurrent.Callable;
import misat11.core.AbstractCore;
import misat11.core.object.AbstractObject;

/**
 *
 * @author misat11
 */
public class SimpleCharacter extends AbstractCharacter {

    public SimpleCharacter(final AbstractCore main, int objectId) {
        super(main, objectId);
        CapsuleCollisionShape collisionShape = new CapsuleCollisionShape(0.6f, 2f);
        control = new CharacterControl(collisionShape, 0.09f);
        final AbstractObject obj = main.getObject(this.objectId);
        obj.getSpatial().addControl(control);
        main.enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                main.getBulletAppState().getPhysicsSpace().add(control);
                return null;
            }
        });

        control.setGravity(40f);
        control.setJumpSpeed(15f);
        control.warp(new Vector3f(0.0f, 60f, 0.0f));
    }

    public SimpleCharacter(final AbstractCore main, int objectId, float gravity) {
        super(main, objectId);
        CapsuleCollisionShape collisionShape = new CapsuleCollisionShape(0.6f, 2f);
        control = new CharacterControl(collisionShape, 0.09f);
        final AbstractObject obj = main.getObject(this.objectId);
        obj.getSpatial().addControl(control);
        main.enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                main.getBulletAppState().getPhysicsSpace().add(control);
                return null;
            }
        });
        control.setGravity(gravity);
        control.setJumpSpeed(15f);
        control.warp(new Vector3f(0.0f, 60f, 0.0f));
    }

    public SimpleCharacter(final AbstractCore main, int objectId, float gravity, CapsuleCollisionShape collisionShape) {
        super(main, objectId);
        control = new CharacterControl(collisionShape, 0.09f);
        final AbstractObject obj = main.getObject(this.objectId);
        obj.getSpatial().addControl(control);
        main.enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                main.getBulletAppState().getPhysicsSpace().add(control);
                return null;
            }
        });
        control.setGravity(gravity);
        control.setJumpSpeed(15f);
        control.warp(new Vector3f(0.0f, 60f, 0.0f));
    }

    public SimpleCharacter(final AbstractCore main, int objectId, float gravity, CapsuleCollisionShape collisionShape, float stepHeight) {
        super(main, objectId);
        control = new CharacterControl(collisionShape, stepHeight);
        final AbstractObject obj = main.getObject(this.objectId);
        obj.getSpatial().addControl(control);
        main.enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                main.getBulletAppState().getPhysicsSpace().add(control);
                return null;
            }
        });
        control.setGravity(gravity);
        control.setJumpSpeed(15f);
        control.warp(new Vector3f(0.0f, 60f, 0.0f));
    }

    @Override
    public void update() {
            Vector3f camDir = main.getCamera().getDirection().clone();
            Vector3f camLeft = main.getCamera().getLeft().clone();
            camDir.y = 0;
            camLeft.y = 0;
            camDir.normalizeLocal();
            camLeft.normalizeLocal();
            Vector3f walkDirection = new Vector3f(0, 0, 0);

            if (left) {
                walkDirection.addLocal(camLeft);
            }
            if (right) {
                walkDirection.addLocal(camLeft.negate());
            }
            if (up) {
                walkDirection.addLocal(camDir);
            }
            if (down) {
                walkDirection.addLocal(camDir.negate());
            }

            if (!control.onGround()) {
                airTime += main.tpf;
            } else {
                airTime = 0;
            }

            if (walkDirection.lengthSquared() == 0) {
                runAnimation("Stand");
            } else {
                control.setViewDirection(walkDirection);
                if (airTime > .3f) {
                    runAnimation("Stand");
                } else {
                    runAnimation("Walk");
                }
            }

            walkDirection.multLocal(25f).multLocal(main.tpf);
            control.setWalkDirection(walkDirection);
    }

}

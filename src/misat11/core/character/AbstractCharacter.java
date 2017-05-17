/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.character;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import java.util.concurrent.Callable;
import misat11.core.AbstractCore;
import misat11.core.animation.AnimationUtil;

/**
 *
 * @author misat11
 */
public abstract class AbstractCharacter {

    public CharacterControl control;
    public AbstractCore main;
    public AnimationUtil animation;
    public int objectId;
    public float airTime = 0;
    public boolean left = false;
    public boolean right = false;
    public boolean up = false;
    public boolean down = false;

    public AbstractCharacter(AbstractCore main, int objectId) {
        this.main = main;
        this.objectId = objectId;
    }

    public CharacterControl getControl() {
        return control;
    }

    public abstract void update(float tpf);

    public void setControl(CharacterControl control) {
        this.control = control;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public void runAnimation(final String name) {
        if (animation == null) {
            animation = new AnimationUtil(main, main.getObject(objectId));
        }
        main.enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                animation.setAnimation(name);
                return null;
            }
        });
    }

    public void warp(final Vector3f loc) {
        main.enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                control.warp(loc);
                return null;
            }
        });
    }

    public void setWalkDirection(final Vector3f walk) {
        main.enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                control.setWalkDirection(walk);
                return null;
            }
        });
    }

    public void setViewDirection(final Vector3f view) {
        main.enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                control.setViewDirection(view);
                return null;
            }
        });
    }

    public void setGravity(final float gravity) {
        main.enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                control.setGravity(gravity);
                return null;
            }
        });
    }

    public void setJumpSpeed(final float jumpSpeed) {
        main.enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                control.setJumpSpeed(jumpSpeed);
                return null;
            }
        });
    }

    public void setFallSpeed(final float fallSpeed) {
        main.enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                control.setFallSpeed(fallSpeed);
                return null;
            }
        });
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

}

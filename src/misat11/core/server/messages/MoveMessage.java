/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server.messages;

import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.jme3.renderer.Camera;

/**
 *
 * @author misat11
 */
@Serializable
public class MoveMessage extends AbstractMessage {

    private boolean left = false;
    private boolean right = false;
    private boolean up = false;
    private boolean down = false;
    private boolean jump = false;
    private int id = -1;
    private Vector3f leftCam;
    private Vector3f dirCam;

    public MoveMessage() {

    }

    public MoveMessage(int id, boolean left, boolean right, boolean up, boolean down, boolean jump, Camera camera) {
        this.id = id;
        this.left = left;
        this.right = right;
        this.up = up;
        this.down = down;
        this.jump = jump;
        this.leftCam = camera.getDirection().clone();
        this.dirCam = camera.getLeft().clone();
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isJump() {
        return jump;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public boolean isMoveValid() {
        return !((leftCam == null) || (dirCam == null) || (up && down) || (up && left && right) || (down && left && right) || (left && right) || (up && down && left && right) );
    }

    public Vector3f getLeftCam() {
        return leftCam;
    }

    public void setLeftCam(Vector3f leftCam) {
        this.leftCam = leftCam;
    }

    public Vector3f getDirCam() {
        return dirCam;
    }

    public void setDirCam(Vector3f dirCam) {
        this.dirCam = dirCam;
    }
    
    
}

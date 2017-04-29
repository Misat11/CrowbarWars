/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.keyboard;

/**
 *
 * @author misat11
 */
public class ControlSettings {

    private int LEFT;
    private int RIGHT;
    private int UP;
    private int DOWN;
    private int JUMP;
    private int ATTACK;

    public ControlSettings() {
        UP = CharacterBaseKeys.UP;
        DOWN = CharacterBaseKeys.DOWN;
        LEFT = CharacterBaseKeys.LEFT;
        RIGHT = CharacterBaseKeys.RIGHT;
        JUMP = CharacterBaseKeys.JUMP;
        ATTACK = CharacterBaseKeys.ATTACK;
    }

    public ControlSettings(int up, int down, int left, int right, int jump, int attack) {
        UP = up;
        DOWN = down;
        LEFT = left;
        RIGHT = right;
        JUMP = jump;
        ATTACK = attack;
    }

    public int getLEFT() {
        return LEFT;
    }

    public void setLEFT(int LEFT) {
        this.LEFT = LEFT;
    }

    public int getRIGHT() {
        return RIGHT;
    }

    public void setRIGHT(int RIGHT) {
        this.RIGHT = RIGHT;
    }

    public int getUP() {
        return UP;
    }

    public void setUP(int UP) {
        this.UP = UP;
    }

    public int getDOWN() {
        return DOWN;
    }

    public void setDOWN(int DOWN) {
        this.DOWN = DOWN;
    }

    public int getJUMP() {
        return JUMP;
    }

    public void setJUMP(int JUMP) {
        this.JUMP = JUMP;
    }

    public int getATTACK() {
        return ATTACK;
    }

    public void setATTACK(int ATTACK) {
        this.ATTACK = ATTACK;
    }

}

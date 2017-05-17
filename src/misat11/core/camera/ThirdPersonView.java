/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.camera;

import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import misat11.core.AbstractCore;

/**
 *
 * @author misat11
 */
public class ThirdPersonView extends AbstractView {

    protected float minVerticalRotation = 0.00f;
    protected float maxVerticalRotation = FastMath.PI / 2;
    protected float minDistance = 1.0f;
    protected float maxDistance = 40.0f;
    protected float distance = 20;
    protected float rotationSpeed = 1.0f;
    protected float rotationF = 0;
    protected float trailingRotationInertia = 0.05f;
    protected float zoomSensitivity = 2f;
    protected float rotationSensitivity = 5f;
    protected float chasingSensitivity = 5f;
    protected float trailingSensitivity = 0.5f;
    protected float vRotation = FastMath.PI / 6;
    protected boolean smoothMotion = false;
    protected boolean trailingEnabled = true;
    protected float rotationLerpFactor = 0;
    protected float trailingLerpFactor = 0;
    protected boolean rotating = false;
    protected boolean vRotating = false;
    protected float targetRotation = rotationF;
    protected Vector3f initialUpVec = new Vector3f();
    protected float targetVRotation = vRotation;
    protected float vRotationLerpFactor = 0;
    protected float targetDistance = distance;
    protected float distanceLerpFactor = 0;
    protected boolean zooming = false;
    protected boolean trailing = false;
    protected boolean chasing = false;
    protected boolean veryCloseRotation = true;
    protected boolean canRotate = true;
    protected float offsetDistance = 0.002f;
    protected Vector3f prevPos = new Vector3f();
    protected boolean targetMoves = false;
    protected final Vector3f targetDir = new Vector3f();
    protected float previousTargetRotation;
    protected final Vector3f pos = new Vector3f();
    protected Vector3f targetLocation = new Vector3f(0, 0, 0);
    protected boolean dragToRotate = true;
    protected Vector3f lookAtOffset = new Vector3f(0, 0, 0);
    protected boolean leftClickRotate = true;
    protected boolean rightClickRotate = true;
    protected Vector3f temp = new Vector3f(0, 0, 0);
    protected boolean invertYaxis = false;
    protected boolean invertXaxis = false;
    protected boolean zoomin;

    public ThirdPersonView(AbstractCore main, Camera camera, InputManager inputManager) {
        super(main, camera, inputManager);
        registerWithInput();
    }

    @Override
    public void update(float tpf) {
        targetLocation.set(location).addLocal(lookAtOffset);
        if (smoothMotion) {

            targetDir.set(targetLocation).subtractLocal(prevPos);
            float dist = targetDir.length();

            if (offsetDistance < dist) {
                chasing = true;
                if (trailingEnabled) {
                    trailing = true;
                }
                targetMoves = true;
            } else {
                if (targetMoves && !canRotate) {
                    if (targetRotation - rotationF > trailingRotationInertia) {
                        targetRotation = rotationF + trailingRotationInertia;
                    } else if (targetRotation - rotationF < -trailingRotationInertia) {
                        targetRotation = rotationF - trailingRotationInertia;
                    }
                }
                targetMoves = false;
            }

            if (canRotate) {
                trailingLerpFactor = 0;
                trailing = false;
            }

            if (trailingEnabled && trailing) {
                if (targetMoves) {
                    Vector3f a = targetDir.negate().normalizeLocal();
                    Vector3f b = Vector3f.UNIT_X;
                    a.y = 0;
                    if (targetDir.z > 0) {
                        targetRotation = FastMath.TWO_PI - FastMath.acos(a.dot(b));
                    } else {
                        targetRotation = FastMath.acos(a.dot(b));
                    }
                    if (targetRotation - rotationF > FastMath.PI || targetRotation - rotationF < -FastMath.PI) {
                        targetRotation -= FastMath.TWO_PI;
                    }

                    if (targetRotation != previousTargetRotation && FastMath.abs(targetRotation - previousTargetRotation) > FastMath.PI / 8) {
                        trailingLerpFactor = 0;
                    }
                    previousTargetRotation = targetRotation;
                }
                trailingLerpFactor = Math.min(trailingLerpFactor + tpf * tpf * trailingSensitivity, 1);
                rotationF = FastMath.interpolateLinear(trailingLerpFactor, rotationF, targetRotation);

                if (targetRotation + 0.01f >= rotationF && targetRotation - 0.01f <= rotationF) {
                    trailing = false;
                    trailingLerpFactor = 0;
                }
            }

            if (chasing) {
                distance = temp.set(targetLocation).subtractLocal(camera.getLocation()).length();
                distanceLerpFactor = Math.min(distanceLerpFactor + (tpf * tpf * chasingSensitivity * 0.05f), 1);
                distance = FastMath.interpolateLinear(distanceLerpFactor, distance, targetDistance);
                if (targetDistance + 0.01f >= distance && targetDistance - 0.01f <= distance) {
                    distanceLerpFactor = 0;
                    chasing = false;
                }
            }

            if (zooming) {
                distanceLerpFactor = Math.min(distanceLerpFactor + (tpf * tpf * zoomSensitivity), 1);
                distance = FastMath.interpolateLinear(distanceLerpFactor, distance, targetDistance);
                if (targetDistance + 0.1f >= distance && targetDistance - 0.1f <= distance) {
                    zooming = false;
                    distanceLerpFactor = 0;
                }
            }

            if (rotating) {
                rotationLerpFactor = Math.min(rotationLerpFactor + tpf * tpf * rotationSensitivity, 1);
                rotationF = FastMath.interpolateLinear(rotationLerpFactor, rotationF, targetRotation);
                if (targetRotation + 0.01f >= rotationF && targetRotation - 0.01f <= rotationF) {
                    rotating = false;
                    rotationLerpFactor = 0;
                }
            }

            if (vRotating) {
                vRotationLerpFactor = Math.min(vRotationLerpFactor + tpf * tpf * rotationSensitivity, 1);
                vRotation = FastMath.interpolateLinear(vRotationLerpFactor, vRotation, targetVRotation);
                if (targetVRotation + 0.01f >= vRotation && targetVRotation - 0.01f <= vRotation) {
                    vRotating = false;
                    vRotationLerpFactor = 0;
                }
            }

            float hDistance = (distance) * FastMath.sin((FastMath.PI / 2) - vRotation);
            pos.set(hDistance * FastMath.cos(rotationF), (distance) * FastMath.sin(vRotation), hDistance * FastMath.sin(rotationF));
            pos.addLocal(location);
            camera.setLocation(pos.addLocal(lookAtOffset));
        } else {
            vRotation = targetVRotation;
            rotationF = targetRotation;
            distance = targetDistance;

            float hDistance = (distance) * FastMath.sin((FastMath.PI / 2) - vRotation);
            pos.set(hDistance * FastMath.cos(rotationF), (distance) * FastMath.sin(vRotation), hDistance * FastMath.sin(rotationF));
            pos.addLocal(location);
            camera.setLocation(pos.addLocal(lookAtOffset));
        }
        prevPos.set(targetLocation);

        camera.lookAt(targetLocation, initialUpVec);
    }

    protected void zoomCamera(float value) {
        zooming = true;
        targetDistance += value * zoomSensitivity;
        if (targetDistance > maxDistance) {
            targetDistance = maxDistance;
        }
        if (targetDistance < minDistance) {
            targetDistance = minDistance;
        }
        if (veryCloseRotation) {
            if ((targetVRotation < minVerticalRotation) && (targetDistance > (minDistance + 1.0f))) {
                targetVRotation = minVerticalRotation;
            }
        }
    }

    protected void vRotateCamera(float value) {
        if (!canRotate) {
            return;
        }
        vRotating = true;
        float lastGoodRot = targetVRotation;
        targetVRotation += value * rotationSpeed;
        if (targetVRotation > maxVerticalRotation) {
            targetVRotation = lastGoodRot;
        }
        if (veryCloseRotation) {
            if ((targetVRotation < minVerticalRotation) && (targetDistance > (minDistance + 1.0f))) {
                targetVRotation = minVerticalRotation;
            } else if (targetVRotation < -FastMath.DEG_TO_RAD * 90) {
                targetVRotation = lastGoodRot;
            }
        } else {
            if ((targetVRotation < minVerticalRotation)) {
                targetVRotation = lastGoodRot;
            }
        }
    }

    public boolean isCanRotate() {
        return canRotate;
    }

    public void setCanRotate(boolean canRotate) {
        this.canRotate = canRotate;
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        if (name.equals(ViewInput.MOVELEFT)) {
            rotateCamera(-value);
        } else if (name.equals(ViewInput.MOVERIGHT)) {
            rotateCamera(value);
        } else if (name.equals(ViewInput.UP)) {
            vRotateCamera(value);
        } else if (name.equals(ViewInput.DOWN)) {
            vRotateCamera(-value);
        } else if (name.equals(ViewInput.ZOOMIN)) {
            zoomCamera(-value);
            if (zoomin == false) {
                distanceLerpFactor = 0;
            }
            zoomin = true;
        } else if (name.equals(ViewInput.ZOOMOUT)) {
            zoomCamera(+value);
            if (zoomin == true) {
                distanceLerpFactor = 0;
            }
            zoomin = false;
        }
    }

    protected void rotateCamera(float value) {
        if (!canRotate) {
            return;
        }
        rotating = true;
        targetRotation += value * rotationSpeed;

    }

    public final void registerWithInput() {

        String[] inputs = {ViewInput.DOWN, ViewInput.UP, ViewInput.MOVELEFT, ViewInput.MOVERIGHT, ViewInput.TOGGLEROTATE, ViewInput.ZOOMIN, ViewInput.ZOOMOUT};

        if (!invertYaxis) {
            inputManager.addMapping(ViewInput.DOWN, new MouseAxisTrigger(MouseInput.AXIS_Y, true));
            inputManager.addMapping(ViewInput.UP, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        } else {
            inputManager.addMapping(ViewInput.DOWN, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
            inputManager.addMapping(ViewInput.UP, new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        }
        inputManager.addMapping(ViewInput.ZOOMIN, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addMapping(ViewInput.ZOOMOUT, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        if (!invertXaxis) {
            inputManager.addMapping(ViewInput.MOVELEFT, new MouseAxisTrigger(MouseInput.AXIS_X, true));
            inputManager.addMapping(ViewInput.MOVERIGHT, new MouseAxisTrigger(MouseInput.AXIS_X, false));
        } else {
            inputManager.addMapping(ViewInput.MOVELEFT, new MouseAxisTrigger(MouseInput.AXIS_X, false));
            inputManager.addMapping(ViewInput.MOVERIGHT, new MouseAxisTrigger(MouseInput.AXIS_X, true));
        }
        inputManager.addMapping(ViewInput.TOGGLEROTATE, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping(ViewInput.TOGGLEROTATE, new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));

        inputManager.addListener(this, inputs);
    }
}

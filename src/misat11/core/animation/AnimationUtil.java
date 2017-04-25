/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.animation;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitorAdapter;
import com.jme3.scene.Spatial;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import misat11.core.AbstractCore;
import misat11.core.object.AbstractObject;

/**
 *
 * @author misat11
 */
public class AnimationUtil {

    private AbstractCore main;
    private List<AnimControl> animControls;
    private List<AnimChannel> animChannels;

    public AnimationUtil(AbstractCore main, AbstractObject obj) {
        this.main = main;
        setAnimControls(obj.getSpatial());
    }

    private void setAnimControls(Spatial spatial) {
        if (spatial == null) {
            if (animControls != null) {
                for (Iterator<AnimControl> it = animControls.iterator(); it.hasNext();) {
                    AnimControl animControl = it.next();
                    animControl.clearChannels();
                }
            }
            animControls = null;
            animChannels = null;
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
                if (animControls == null) {
                    animControls = new LinkedList<AnimControl>();
                }
                if (animChannels == null) {
                    animChannels = new LinkedList<AnimChannel>();
                }

                animControls.add(control);
                animChannels.add(control.createChannel());
            }
        };
        spatial.depthFirstTraversal(visitor);
    }

    public void setAnimation(final String name) {
        main.enqueue(new Callable() {
            @Override
            public Object call() throws Exception {
                if (animChannels != null) {
                    for (Iterator<AnimChannel> it = animChannels.iterator(); it.hasNext();) {
                        AnimChannel animChannel = it.next();
                        if (animChannel.getAnimationName() == null || !animChannel.getAnimationName().equals(name)) {
                            animChannel.setAnim(name);
                            if (animChannel.getControl().getAnim(name) != null) {
                            }
                        }
                    }
                }
                return null;
            }
        });
    }
}

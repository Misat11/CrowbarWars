/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server.client.guibuilders;

/**
 *
 * @author misat11
 */
public abstract class AbstractGuiBuilder {

    public abstract void build();

    public abstract void destroy();

    protected boolean builded = false;
}

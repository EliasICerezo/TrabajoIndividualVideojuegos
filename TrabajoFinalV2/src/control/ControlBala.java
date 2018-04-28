/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author elias
 */
public class ControlBala extends AbstractControl {
    private Geometry bala;
    private Node rootNode;
    private float time=0;
    public ControlBala(Geometry bala, Node rootNode) {
        this.bala = bala;
        this.rootNode = rootNode;
    }
    
    
    @Override
    protected void controlUpdate(float tpf) {
        time+=tpf;
        
        if(time>5){
            rootNode.detachChild(bala);
        }
        
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        
    }
    
}

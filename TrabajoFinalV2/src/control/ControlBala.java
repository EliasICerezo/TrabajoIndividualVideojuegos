/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.jme3.bounding.BoundingSphere;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import java.util.ArrayList;
import java.util.Iterator;
import modelo.Tanque;
import modelo.TanqueSinComportamiento;

/**
 *
 * @author elias
 */
public class ControlBala extends AbstractControl {

    private Geometry bala;
    private Node balasNode;
    private Node enemigosNode;
    private float time = 0;
    private BoundingSphere contorno;
    private ArrayList<Tanque> listaEnemigos;

    private boolean colision = false;

    public ControlBala(Geometry bala, Node rootNode, Node enemigosNode, BoundingSphere bs, ArrayList<Tanque> listaEnemigos) {
        this.bala = bala;
        this.balasNode = rootNode;
        this.enemigosNode = enemigosNode;
        this.contorno = bs;
        this.listaEnemigos = listaEnemigos;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (bala != null) {
            contorno.setCenter(bala.getWorldTranslation());
            time += tpf;
            //Aqui detectamos la s colisiones
            CollisionResults results = new CollisionResults();
            if (enemigosNode != null) {
                enemigosNode.collideWith(contorno, results);
                Iterator<CollisionResult> iter = results.iterator();
                while (iter.hasNext()) {
                    CollisionResult next = iter.next();
                    System.out.println("Colision con :" + next.getGeometry().getName());

                    colision = true;

                    for (Tanque t : listaEnemigos) {
                        t.eliminaTanque(next.getGeometry());
                    }

                }

                if (time > 5 || colision) {

                    balasNode.detachChild(bala);
                    bala = null;

                }
            }
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }

}

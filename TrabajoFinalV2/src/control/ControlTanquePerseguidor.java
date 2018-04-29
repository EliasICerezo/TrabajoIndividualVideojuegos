/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.Iterator;
import modelo.Tanque;

/**
 *
 * @author elias
 */
public class ControlTanquePerseguidor extends ControlTorreta{
    protected boolean permiso=false;
    
    public ControlTanquePerseguidor(Node tanquejugador, Tanque mitanque) {
        super(tanquejugador, mitanque);
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        tiempodisparo+=tpf;
        
        CollisionResults results=lanzarayos();
        //Aqui apuntamos y disparamos en caso de encontrar una colision con los rayos
        Iterator <CollisionResult> iter=results.iterator();
            while(iter.hasNext()){
                CollisionResult next=iter.next();
                Vector3f direccion = new Vector3f(next.getGeometry().getWorldTranslation().x, 0, next.getGeometry().getWorldTranslation().z);
                mitanque.getNode().lookAt(direccion, Vector3f.UNIT_Y);
                
                perseguir(tpf);
                
                if(next.getDistance()<5){
                    permiso=true;
                }
                
                if(tiempodisparo>2.5 && permiso){
                    mitanque.dispara(estadosFisicos);
                    tiempodisparo=0;
                }
        
            }
    }
    
}

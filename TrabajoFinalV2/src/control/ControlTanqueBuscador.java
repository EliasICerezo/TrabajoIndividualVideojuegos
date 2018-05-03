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
public class ControlTanqueBuscador extends ControlTorreta{
    
    public ControlTanqueBuscador(Node tanquejugador, Tanque mitanque) {
        super(tanquejugador, mitanque);
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        
        tiempodisparo+=tpf;
        
        mitanque.getNode().rotate(0,tpf,0);
        
        Vector3f espiral=mitanque.getEspiral().mult(10);
       
        mitanque.getNode().move(espiral.mult(tpf));
        
        CollisionResults results=lanzarayos();
        //Aqui apuntamos y disparamos en caso de encontrar una colision con los rayos
        Iterator <CollisionResult> iter=results.iterator();
            while(iter.hasNext()){
                CollisionResult next=iter.next();
                Vector3f direccion = new Vector3f(next.getGeometry().getWorldTranslation().x, 0, next.getGeometry().getWorldTranslation().z);
                mitanque.getNode().lookAt(direccion, Vector3f.UNIT_Y);
                perseguir(tpf);
               
                
                
                if(tiempodisparo>2){
                    mitanque.dispara(estadosFisicos);
                    tiempodisparo=0;
                }
        
            }
    }
    
     private void perseguir(float tpf) {
        Vector3f direccion= tanquejugador.getWorldTranslation().subtract(mitanque.getNode().getWorldTranslation());
        
        mitanque.getNode().move(direccion.mult(0.01f*tpf));
    }
    
    
}

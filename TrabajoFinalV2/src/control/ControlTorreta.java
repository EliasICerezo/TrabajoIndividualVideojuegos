/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.jme3.bullet.BulletAppState;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import java.util.Iterator;
import modelo.Tanque;

/**
 *
 * @author elias
 */
public class ControlTorreta extends AbstractControl{
    protected Node tanquejugador;
    protected Tanque mitanque;
    protected BulletAppState estadosFisicos;
    
    //Tiempos de disparo
    protected float tiempodisparo=0;
    
    
    public ControlTorreta(Node tanquejugador, Tanque mitanque) {
        this.tanquejugador = tanquejugador;
        this.mitanque = mitanque;
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
                
                if(tiempodisparo>1){
                    mitanque.dispara(estadosFisicos);
                    tiempodisparo=0;
                }
        
            }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        
    }
    
    protected CollisionResults lanzarayos(){
        CollisionResults results = new CollisionResults();
        Geometry cuerpo=mitanque.getCuerpo();
        
        Vector3f posicion= new Vector3f(cuerpo.getWorldTranslation().x+2, cuerpo.getWorldTranslation().y, cuerpo.getWorldTranslation().z);
        Ray rayo1=new Ray(posicion, new Vector3f(10, 0, 0));
        tanquejugador.collideWith(rayo1, results);
        
        posicion= new Vector3f(cuerpo.getWorldTranslation().x, cuerpo.getWorldTranslation().y, cuerpo.getWorldTranslation().z+2);
        Ray rayo2=new Ray(posicion, new Vector3f(0, 0, 10));
        tanquejugador.collideWith(rayo2, results);
        
        posicion= new Vector3f(cuerpo.getWorldTranslation().x, cuerpo.getWorldTranslation().y, cuerpo.getWorldTranslation().z-2);
        Ray rayo3=new Ray(posicion, new Vector3f(0, 0, -10));
        tanquejugador.collideWith(rayo3, results);
        
        posicion= new Vector3f(cuerpo.getWorldTranslation().x-2, cuerpo.getWorldTranslation().y, cuerpo.getWorldTranslation().z);
        Ray rayo4=new Ray(posicion, new Vector3f(-10, 0, 0));
        tanquejugador.collideWith(rayo4, results);
        
        posicion= new Vector3f(cuerpo.getWorldTranslation().x-2, cuerpo.getWorldTranslation().y, cuerpo.getWorldTranslation().z-2);
        Ray rayo5=new Ray(posicion, new Vector3f(-10, 0, -10));
        tanquejugador.collideWith(rayo5, results);
        
        posicion= new Vector3f(cuerpo.getWorldTranslation().x-2, cuerpo.getWorldTranslation().y, cuerpo.getWorldTranslation().z+2);
        Ray rayo6=new Ray(posicion, new Vector3f(-10, 0, 10));
        tanquejugador.collideWith(rayo6, results);
        
        posicion= new Vector3f(cuerpo.getWorldTranslation().x+2, cuerpo.getWorldTranslation().y, cuerpo.getWorldTranslation().z-2);
        Ray rayo7=new Ray(posicion, new Vector3f(10, 0, -10));
        tanquejugador.collideWith(rayo7, results);
        
        posicion= new Vector3f(cuerpo.getWorldTranslation().x+2, cuerpo.getWorldTranslation().y, cuerpo.getWorldTranslation().z+2);
        Ray rayo8=new Ray(posicion, new Vector3f(10, 0, 10));
        tanquejugador.collideWith(rayo8, results);
        
        posicion= new Vector3f(cuerpo.getWorldTranslation().x+1, cuerpo.getWorldTranslation().y, cuerpo.getWorldTranslation().z+2);
        Ray rayo9=new Ray(posicion, new Vector3f(5, 0, 10));
        tanquejugador.collideWith(rayo9, results);
        
        posicion= new Vector3f(cuerpo.getWorldTranslation().x+2, cuerpo.getWorldTranslation().y, cuerpo.getWorldTranslation().z+1);
        Ray rayo10=new Ray(posicion, new Vector3f(10, 0, 5));
        tanquejugador.collideWith(rayo10, results);
        
        posicion= new Vector3f(cuerpo.getWorldTranslation().x+2, cuerpo.getWorldTranslation().y, cuerpo.getWorldTranslation().z-1);
        Ray rayo11=new Ray(posicion, new Vector3f(10, 0, -5));
        tanquejugador.collideWith(rayo11, results);
        
        posicion= new Vector3f(cuerpo.getWorldTranslation().x+1, cuerpo.getWorldTranslation().y, cuerpo.getWorldTranslation().z-2);
        Ray rayo12=new Ray(posicion, new Vector3f(5, 0, -10));
        tanquejugador.collideWith(rayo12, results);
        
        posicion= new Vector3f(cuerpo.getWorldTranslation().x-1, cuerpo.getWorldTranslation().y, cuerpo.getWorldTranslation().z-2);
        Ray rayo13=new Ray(posicion, new Vector3f(-5, 0, -10));
        tanquejugador.collideWith(rayo13, results);
        
        posicion= new Vector3f(cuerpo.getWorldTranslation().x-2, cuerpo.getWorldTranslation().y, cuerpo.getWorldTranslation().z-1);
        Ray rayo14=new Ray(posicion, new Vector3f(-10, 0, -5));
        tanquejugador.collideWith(rayo14, results);
        
        posicion= new Vector3f(cuerpo.getWorldTranslation().x-2, cuerpo.getWorldTranslation().y, cuerpo.getWorldTranslation().z+1);
        Ray rayo15=new Ray(posicion, new Vector3f(-10, 0, 5));
        tanquejugador.collideWith(rayo15, results);
        
        posicion= new Vector3f(cuerpo.getWorldTranslation().x-2, cuerpo.getWorldTranslation().y, cuerpo.getWorldTranslation().z+1);
        Ray rayo16=new Ray(posicion, new Vector3f(-5, 0, 10));
        tanquejugador.collideWith(rayo16, results);
        
        return results;
        
    }
    
    public void setEstadosFisicos(BulletAppState estadosFisicos){
        this.estadosFisicos=estadosFisicos;
    }
    
    
    
    
}

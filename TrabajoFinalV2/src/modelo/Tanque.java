/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import com.jme3.bullet.BulletAppState;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import java.util.ArrayList;

/**
 *
 * @author elias
 */
public interface Tanque {

    void dispara(BulletAppState estadosFisicos);

    void eliminaTanque(Geometry g);

    Vector3f getApuntado();

    Vector3f getCamara();

    ArrayList<Geometry> getGeomBalas();

    Node getNode();

    void setEnemigos(Node e);

    void setListaEnemigos(ArrayList<Tanque> list);

    void setMaterialCuerpo(Material materialCuerpo);

    void setMaterialcanon(Material materialcanon);
    
    void addControl(AbstractControl control);
    
    Geometry getCuerpo();
    
    Vector3f getEspiral();
    
    void setTanqueJugador(TanqueSinComportamiento t);
}

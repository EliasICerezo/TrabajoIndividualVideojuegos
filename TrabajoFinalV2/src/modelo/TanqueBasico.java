/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;
import control.ControlBala;
import java.util.ArrayList;

/**
 *
 * @author elias
 */
public class TanqueBasico {

    private Node tanque,camara;//El nodo donde esta todo
    private Material materialCuerpo;
    private ArrayList<RigidBodyControl> rigidbalas; //El array de los rigeid body de las balas
    private ArrayList<Geometry> balasGeometry; //Array de las geometry de las balas
    private int i = 0;//Numero de balas
    private String name; //Nombre del tanque
    private AssetManager assetManager; //AssetManager
   
    
    //Necesito tener el cañon para generar las balas delante
    private Geometry canong;

    public TanqueBasico(String name, AssetManager assetManager) {
        tanque = new Node();
        rigidbalas = new ArrayList<>();
        balasGeometry = new ArrayList<>();
        this.name = name;
        this.assetManager = assetManager;
       

        materialCuerpo = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        materialCuerpo.setColor("Color", ColorRGBA.Red);

        Box cuerpo = new Box(1, 1, 1);
        Geometry cuerpog = new Geometry("cuerpo" + name, cuerpo);
        cuerpog.setMaterial(materialCuerpo);
        cuerpog.move(0, 1.01f, 0);

        Material materialcanon = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        materialcanon.setColor("Color", ColorRGBA.Blue);
        Cylinder canon = new Cylinder(30, 30, 0.15f, 0.75f, true);
        canong = new Geometry("canon" + name, canon);
        canong.setMaterial(materialcanon);
        canong.move(0, 2f, 1);
        
        //poniendo la camara detras del tanque
        
        camara=new Node();
        camara.move(0, 4, -7.5f);
        tanque.attachChild(camara);
        
        
        tanque.attachChild(canong);
        tanque.attachChild(cuerpog);
    }

    public Node getNode() {
        return tanque;
    }

    public void dispara(BulletAppState estadosFisicos) {
        Material materialbala = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        materialbala.setColor("Color", ColorRGBA.Black);

        Sphere bala = new Sphere(30, 30, 0.15f, true, true);
        Geometry balag = new Geometry("bala " + name + " " + i, bala);
        balag.move(canong.getWorldTranslation()); 
        balag.setMaterial(materialbala);
        balasGeometry.add(balag);
        
        //Fisicas
        RigidBodyControl fisicaBalas = new RigidBodyControl(1);
        balag.addControl(fisicaBalas);
        estadosFisicos.getPhysicsSpace().add(fisicaBalas);
        fisicaBalas.setRestitution(0.9f);
        
        //Le añado el control
        ControlBala cb=new ControlBala(balag, tanque);
        balag.addControl(cb);
        
       
        
        Vector3f direccion= tanque.getWorldRotation().getRotationColumn(2).normalize().mult(new Vector3f(50, 5, 50)); //new Vector3f(componente.x, 0, componente.z);
        
        
        
        fisicaBalas.applyImpulse(direccion,Vector3f.ZERO);
        rigidbalas.add(fisicaBalas);
        tanque.attachChild(balag);
        i++;
        
    }
    
    public void addRigidBala(RigidBodyControl bala){
        rigidbalas.add(bala);
    }
    
    
    public Vector3f getApuntado(){
        return tanque.getWorldTranslation();
    }
    
    public Vector3f getCamara(){
        Vector3f camv=new Vector3f(camara.getWorldTranslation().x,5,camara.getWorldTranslation().z);
        return camv;
    }
    
}

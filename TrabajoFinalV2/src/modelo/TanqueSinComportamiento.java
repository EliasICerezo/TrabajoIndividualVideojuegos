/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;
import control.ControlBala;
import java.util.ArrayList;

/**
 *
 * @author elias
 */
public class TanqueSinComportamiento implements Tanque {

    private Node tanque, camara;//El nodo donde esta todo
    private Material materialCuerpo;
    private ArrayList<Geometry> balasGeometry; //Array de las geometry de las balas
    private int i = 0;//Numero de balas
    private String name; //Nombre del tanque
    private AssetManager assetManager; //AssetManager
    private Material materialcanon;
    private Geometry cuerpog;
    private Node enemigosNode;
    private ArrayList<Tanque> listaEnemigos;
    private Node miPadre; //Darth Vader
    private TanqueSinComportamiento tanquejugador;
    //Necesito tener el cañon para generar las balas delante
    private Geometry canong;

    //Para gfeneral la espiral
    private Geometry geomEspiral;
    
    /**
     * Con este constructor generamos un tanque compuesto por un box a modo de cuerpo y un cilindro a modo de cañon, no lleva comportamiento asociado
     * y tiene todos los metodos que se necesitan para hacer que el tanque funcione
     * @param name Nombre del tanque (esto nos sirve para identificar por ejemplo de que tanque vienen las balas)
     * @param assetManager El assetManager es necesario para cargar materiales asi que se le pasa desde el main
     * @param padre El nodo padre del tanque, esto es necesario cuando queremos borrar el tanque por ejemplo
     */
    public TanqueSinComportamiento(String name, AssetManager assetManager, Node padre) {
        tanque = new Node();
        
        balasGeometry = new ArrayList<>();
        this.name = name;
        this.assetManager = assetManager;
        this.miPadre = padre;
        //inicializo la lista de enemigos como una lista vacia
        listaEnemigos = new ArrayList<>();

        materialCuerpo = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        materialCuerpo.setColor("Color", ColorRGBA.Red);

        Box cuerpo = new Box(1, 1, 1);
        cuerpog = new Geometry("cuerpo" + name, cuerpo);
        cuerpog.setMaterial(materialCuerpo);
        cuerpog.move(0, 1.1f, 0);

        materialcanon = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        materialcanon.setColor("Color", ColorRGBA.Blue);
        Cylinder canon = new Cylinder(30, 30, 0.15f, 0.75f, true);
        canong = new Geometry("canon" + name, canon);
        canong.setMaterial(materialcanon);
        canong.move(0, 2f, 1);

        //poniendo la camara detras del tanque
        camara = new Node();
        camara.move(0, 4, -7.5f);
        tanque.attachChild(camara);

        tanque.attachChild(canong);
        tanque.attachChild(cuerpog);

        generaCubosparaRayos();
    }

    @Override
    public Node getNode() {
        return tanque;
    }

    @Override
    public void dispara(BulletAppState estadosFisicos) {
        Material materialbala = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        materialbala.setColor("Color", ColorRGBA.Black);

        BoundingSphere esferabound = new BoundingSphere();

        Sphere bala = new Sphere(30, 30, 0.15f, true, true);
        bala.setBound(esferabound);
        bala.updateBound();
        Geometry balag = new Geometry("bala " + name + " " + i, bala);
        balag.move(canong.getWorldTranslation());
        balag.setMaterial(materialbala);
        balasGeometry.add(balag);
        esferabound.setCenter(balag.getWorldTranslation());

        //Fisicas
        RigidBodyControl fisicaBalas = new RigidBodyControl(1);
        balag.addControl(fisicaBalas);
        estadosFisicos.getPhysicsSpace().add(fisicaBalas);
        fisicaBalas.setRestitution(0.9f);

        //Le añado el control
        ControlBala cb = new ControlBala(balag, tanque, enemigosNode, esferabound, listaEnemigos);
        if(tanquejugador!=null){
            cb.setTanqueJugador(tanquejugador);
        }
        balag.addControl(cb);

        Vector3f direccion = tanque.getWorldRotation().getRotationColumn(2).normalize().mult(new Vector3f(50, 5, 50)); //new Vector3f(componente.x, 0, componente.z);

        fisicaBalas.applyImpulse(direccion, Vector3f.ZERO);
        
        tanque.attachChild(balag);
        i++;

    }

    

    @Override
    public Vector3f getApuntado() {
        return tanque.getWorldTranslation();
    }

    @Override
    public Vector3f getCamara() {
        Vector3f camv = new Vector3f(camara.getWorldTranslation().x, 5, camara.getWorldTranslation().z);
        return camv;
    }

    @Override
    public void setMaterialCuerpo(Material materialCuerpo) {
        this.materialCuerpo = materialCuerpo;
        cuerpog.setMaterial(materialCuerpo);
    }

    @Override
    public void setMaterialcanon(Material materialcanon) {
        this.materialcanon = materialcanon;
        canong.setMaterial(materialcanon);
    }

    @Override
    public ArrayList<Geometry> getGeomBalas() {
        return balasGeometry;
    }

    @Override
    public void eliminaTanque(Geometry g) {
        if (g.equals(cuerpog)|| g.equals(canong)) {
            miPadre.detachChild(tanque);

        }

    }

    @Override
    public void setEnemigos(Node e) {
        enemigosNode = e;
    }

    @Override
    public void setListaEnemigos(ArrayList<Tanque> list) {
        listaEnemigos = list;
    }

    private void generaCubosparaRayos() {
        Material materialrayos = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        materialrayos.setColor("Color", ColorRGBA.White);

//        Box b1 = new Box(0.1f, 0.1f, 0.1f);
        Box b2 = new Box(0.001f, 0.001f, 0.001f);
//        Box b3 = new Box(0.1f, 0.1f, 0.1f);
//        Box b4 = new Box(0.1f, 0.1f, 0.1f);
//        Box b5 = new Box(0.1f, 0.1f, 0.1f);
//        Box b6 = new Box(0.1f, 0.1f, 0.1f);
//        Box b7 = new Box(0.1f, 0.1f, 0.1f);
//        Box b8 = new Box(0.1f, 0.1f, 0.1f);

        ArrayList<Geometry> geometrias = new ArrayList<>();

         //Geometry g= new Geometry("r1", b1);
//        g.setMaterial(materialrayos);
//        g.move(2, 1, 0);
//        geometrias.add(g);

        geomEspiral = new Geometry("r2", b2);
        geomEspiral.setMaterial(materialrayos);
        geomEspiral.move(0, 1, 2);
        geometrias.add(geomEspiral);

//        g = new Geometry("r3", b3);
//        g.setMaterial(materialrayos);
//        g.move(0, 1, -2);
//        geometrias.add(g);
//
//        g = new Geometry("r4", b4);
//        g.setMaterial(materialrayos);
//        g.move(-2, 1, 0);
//        geometrias.add(g);
//
//        g = new Geometry("r5", b5);
//        g.setMaterial(materialrayos);
//        g.move(2, 1, 2);
//        geometrias.add(g);
//
//        g = new Geometry("r6", b6);
//        g.setMaterial(materialrayos);
//        g.move(2, 1, -2);
//        geometrias.add(g);
//
//        g = new Geometry("r7", b7);
//        g.setMaterial(materialrayos);
//        g.move(-2, 1, 2);
//        geometrias.add(g);
//
//        g = new Geometry("r8", b8);
//        g.setMaterial(materialrayos);
//        g.move(-2, 1, -2);
//        geometrias.add(g);

        for (Geometry gf : geometrias) {
            tanque.attachChild(gf);
        }

    }

    @Override
    public void addControl(AbstractControl control) {
        tanque.addControl(control);
    }

    @Override
    public Geometry getCuerpo() {
        return cuerpog;
    }
    
    @Override
    public Vector3f getEspiral(){
        return new Vector3f(geomEspiral.getWorldTranslation().x-cuerpog.getWorldTranslation().x, 0, geomEspiral.getWorldTranslation().z-cuerpog.getWorldTranslation().z);
    }
    
    @Override
    public void setTanqueJugador(TanqueSinComportamiento t){
        tanquejugador=t;
    }

}

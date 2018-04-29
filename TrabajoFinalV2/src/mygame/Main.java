package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import control.ControlTanquePerseguidor;
import control.ControlTorreta;
import java.util.ArrayList;
import java.util.Iterator;
import modelo.Tanque;
import modelo.TanqueSinComportamiento;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    private BulletAppState estadosFisicos;
    private RigidBodyControl fisicaSuelo;
    private Node world;
    private Node enemigosNode;
    private Node mipersonaje;
    //Mi personaje
    private TanqueSinComportamiento mipj;
    //Primer enemigo
    private ArrayList<Tanque> enemigos;
    private TanqueSinComportamiento e1,e2;
    
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    public Main() {
        this.estadosFisicos = new BulletAppState();
    }

    @Override
    public void simpleInitApp() {
        inicTeclado();
        world=new Node();
        mipersonaje=new Node();
        enemigosNode=new Node();
        enemigos=new ArrayList<>();
        //pongo el cielo
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        //asocio las fisicas al mapa 
        stateManager.attach(estadosFisicos);
        //iluminacion y camara
        ponerIluminacion();
        cam.setLocation(new Vector3f(0, 5, -5));
        this.flyCam.setMoveSpeed(50);
        //Desactivamos el movimiento de la camara dado que va a ir con nuestro tanque
        this.flyCam.setEnabled(false);
        
        
        //Materials
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.White);
        Material verde = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        verde.setColor("Color", ColorRGBA.Green);
        Material negro = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        negro.setColor("Color", ColorRGBA.Black);
        Material amarillo = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        amarillo.setColor("Color", ColorRGBA.Yellow);
        
        //Suelo
        Box b = new Box(10000, 0.1f, 10000);
        Geometry suelog = new Geometry("Box", b);
        suelog.setMaterial(mat);

        //Fisicas del suelo
        fisicaSuelo = new RigidBodyControl(0f); //creación la fisicaSuelo con masa 0
        suelog.addControl(fisicaSuelo); //asociación geometry y física de suelo
        estadosFisicos.getPhysicsSpace().add(fisicaSuelo); //integración de fisicaSuelo en entorno físico
        fisicaSuelo.setRestitution(0.9f); //darndo rebote a fisicaSuelo} 
        
        //Mi tanque
        mipj = new TanqueSinComportamiento("Tanque1", assetManager,mipersonaje);
        
        
        //Primer enemigo
        e1=new TanqueSinComportamiento("Torreta",assetManager,enemigosNode);
        e1.getNode().move(0,0,-40);
        //Asignamos el control correspondiente a este enemigo
        ControlTorreta controlTorreta=new ControlTorreta(mipj.getNode(), e1);
        controlTorreta.setEstadosFisicos(estadosFisicos);
        e1.addControl(controlTorreta);
        //Lo pintamos de otro color
        e1.setMaterialCuerpo(verde);
        e1.setMaterialcanon(negro);
        //Lo añadimos a las estructuras correspondientes
        enemigosNode.attachChild(e1.getNode());
        enemigos.add(e1);
        
        
        //SegundoEnemigo en este caso va a ser un tanque que nos va a perseguir y cuando se acerque nos disparara
        e2=new TanqueSinComportamiento("TanquePerseguidor", assetManager, enemigosNode);
        e2.getNode().move(40, 0, 40);
        //Aqui ira el control
        ControlTanquePerseguidor perseguidor=new ControlTanquePerseguidor(mipj.getNode(), e2);
        perseguidor.setEstadosFisicos(estadosFisicos);
        e2.addControl(perseguidor);
        //Lo pintamos en otro color
        e2.setMaterialCuerpo(amarillo);
        e2.setMaterialcanon(negro);
        //Lo añadimos a las estructuras correspondientes
        enemigosNode.attachChild(e2.getNode());
        enemigos.add(e2);
       
        
        
        
        
        
        
        
        mipersonaje.attachChild(mipj.getNode());
        world.attachChild(suelog);
        
        rootNode.attachChild(mipersonaje);
        rootNode.attachChild(world);
        rootNode.attachChild(enemigosNode);
        mipj.setEnemigos(enemigosNode);
        mipj.setListaEnemigos(enemigos);
    }
    
    /**
     * 
     * @param tpf 
     */
    @Override
    public void simpleUpdate(float tpf) {
        actualizacamara();
        
    }
    
    private void actualizacamara() {
        cam.setLocation(mipj.getCamara());
        Vector3f canon=mipj.getApuntado();
        this.cam.lookAt(new Vector3f(canon.x, canon.y+2, canon.z), Vector3f.UNIT_Y);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    //esto al final llevara una interfaz que implementaran todos los tipos de tanques
    void ponerIluminacion() {
        DirectionalLight sun1 = new DirectionalLight();
        DirectionalLight sun2 = new DirectionalLight();
        sun1.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)).normalizeLocal());
        sun2.setDirection((new Vector3f(0.5f, -0.5f, 0.5f)).normalizeLocal());
        sun1.setColor(ColorRGBA.White);
        sun2.setColor(ColorRGBA.Gray);
        rootNode.addLight(sun1);
        rootNode.addLight(sun2);
    }
    
    
    private void inicTeclado() {

        inputManager.addMapping("Adelante", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Atras", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Derecha", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Izquierda", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Disparar", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addListener(actionListener, "Disparar");
        inputManager.addListener(analogListener, "Adelante", "Atras", "Derecha", "Izquierda", "Saltar");
    }
    
    private ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            if (name.equals("Disparar") && isPressed ) {
               mipj.dispara(estadosFisicos);
            }
        }
    };
    
    
    private AnalogListener analogListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float value, float tpf) {
            mipersonaje.setLocalTransform(mipj.getNode().getWorldTransform()); //El world del geom es World padre
            mipj.getNode().setLocalTransform(new Transform()); //Se reinicia la transf. local del geom

            float velocidadAvance = 0.01f;

            if (name.equals("Adelante")) {
                mipj.getNode().move(0,0,velocidadAvance);
            }
            if (name.equals("Atras")) {
                mipj.getNode().move(0,0,-velocidadAvance);
            }
            
            if (name.equals("Derecha")) {
                mipj.getNode().rotate(0,-5*tpf,0);
            }
            if (name.equals("Izquierda")) {
                mipj.getNode().rotate(0, 5*tpf,0);
            }
            
    
        }
    };
    
}

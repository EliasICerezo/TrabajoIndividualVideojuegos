package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import modelo.TanqueBasico;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    private BulletAppState estadosFisicos;
    private RigidBodyControl fisicaSuelo;

    //Mi personaje
    private TanqueBasico mipj;
    
    //tiempos
    private float tiempodisparo=0;
    
    
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

        //Suelo
        Box b = new Box(100, 0.01f, 100);
        Geometry suelog = new Geometry("Box", b);
        suelog.setMaterial(mat);

        //Fisicas
        fisicaSuelo = new RigidBodyControl(0f); //creación la fisicaSuelo con masa 0
        suelog.addControl(fisicaSuelo); //asociación geometry y física de suelo
        estadosFisicos.getPhysicsSpace().add(fisicaSuelo); //integración de fisicaSuelo en entorno físico
        fisicaSuelo.setRestitution(0.9f); //darndo rebote a fisicaSuelo} 
        
        //Mi tanque
        mipj = new TanqueBasico("Tanque1", assetManager);
        
        
        
        
        
        rootNode.attachChild(mipj.getNode());
        rootNode.attachChild(suelog);
    }

    @Override
    public void simpleUpdate(float tpf) {
        tiempodisparo+=tpf;
        
        Vector3f cam=mipj.getCamara();
        this.cam.setLocation(cam);
        Vector3f canon=mipj.getApuntado();
        this.cam.lookAt(new Vector3f(canon.x, canon.y+2, canon.z), cam);
      
        
        
       
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
        inputManager.addMapping("Saltar", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Disparar", new KeyTrigger(KeyInput.KEY_P));

        inputManager.addListener(analogListener, "Adelante", "Atras", "Derecha", "Izquierda", "Saltar", "Disparar");
    }
    
    private AnalogListener analogListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float value, float tpf) {
            
            float velocidadRotacion = 0.005f;
            float velocidadAvance = 0.01f;
            if (name.equals("Adelante")) {
                System.out.println("Estoy yendo hacia adelante");
                mipj.getNode().move(0,0.1f,0);
            }
            if (name.equals("Atras")) {
                
            }
            if (name.equals("Saltar")) {
                
            }
            if (name.equals("Derecha")) {
                
            }
            if (name.equals("Izquierda")) {
                
            }
            if (name.equals("Disparar")) {
               
            }
    
        }
    };
    
}

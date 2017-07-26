import display.DisplayManager;
import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJParser;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.Texture;

/**
 * Created by Ivan on 20.7.2017.
 */
public class Main {

    public static void main(String[] args) {

        DisplayManager.createDisplay(); //always first!

        Loader loader = new Loader();


        RawModel model = OBJParser.loadObjModel("dragon", loader);
        Texture texture = new Texture(loader.loadTexture("white"));

        TexturedModel texturedModel = new TexturedModel(model, texture);
        texture = texturedModel.getTexture();
        texture.setShineDamper(15);
        texture.setReflectivity(1);

        Entity entity = new Entity(texturedModel, new Vector3f(0, -0.2f, -2), 0,0,0,0.06f);//place an object somewhere in the center
        Camera camera = new Camera();
        Light light = new Light(new Vector3f(130, 110, -8), new Vector3f(1, 1, 1));

        MasterRenderer masterRenderer = new MasterRenderer();
        while(!Display.isCloseRequested()){
            entity.increasePosition(0, 0, 0);
            entity.increaseRotation(0.1f, 0, 0);
            camera.move();

//            for(Entity e: )
            masterRenderer.processEntity(entity);
            masterRenderer.render(light, camera);
            Display.update();
        }

        masterRenderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
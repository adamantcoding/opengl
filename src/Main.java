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
import terrains.Terrain;
import textures.Texture;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Ivan on 20.7.2017.
 */
public class Main {

    public static void main(String[] args) {

        DisplayManager.createDisplay(); //always first!

        Loader loader = new Loader();

        RawModel model = OBJParser.loadObjModel("tree", loader);
        TexturedModel texturedModel = new TexturedModel(model, new Texture(loader.loadTexture("tree")));

        Texture texture = texturedModel.getTexture();
        texture.setShineDamper(15);
        texture.setReflectivity(1);

        List<Entity> entities = new ArrayList<>();
        Random random = new Random();
        for(int i=0;i<500;i++){
            entities.add(new Entity(texturedModel, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,3));
        }

//        Entity entity = new Entity(texturedModel, new Vector3f(0, -0.2f, -2), 0,0,0,0.06f);//place an object somewhere in the center
//        Camera camera = new Camera();
        Entity entity = new Entity(texturedModel, new Vector3f(0, 0, -25), 0,0,0,1);//place an object somewhere in the center
        Light light = new Light(new Vector3f(3000, 2000, -8), new Vector3f(1, 1, 1));

        Terrain terrain = new Terrain(0,- 1, loader, new Texture(loader.loadTexture("grass")));
        Terrain terrain2 = new Terrain(-1,-1, loader, new Texture(loader.loadTexture("grass")));

        Camera camera = new Camera();

        MasterRenderer masterRenderer = new MasterRenderer();
        while(!Display.isCloseRequested()){
//            entity.increasePosition(0, 1, 0);
            entity.increaseRotation(0f, 1, 0);
            camera.move();

////            for(Entity e: )
//            masterRenderer.processEntity(entity);
//            masterRenderer.render(light, camera);
            masterRenderer.processTerrain(terrain);
            masterRenderer.processTerrain(terrain2);
            for(Entity e:entities){
                masterRenderer.processEntity(e);
            }
            masterRenderer.render(light, camera);

            DisplayManager.updateDisplay();
        }

        masterRenderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
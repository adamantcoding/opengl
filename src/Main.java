import display.DisplayManager;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import hud.HUDRenderer;
import hud.HUDTexture;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import parser.ModelData;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import parser.OBJParser;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
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

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        ModelData model = OBJParser.loadOBJ("tree");
        RawModel treeModel = loader.loadToVAO(model.getVertices(), model.getTextureCoords(), model.getNormals(), model.getIndices());
        ModelData model2 = OBJParser.loadOBJ("grassModel");
        RawModel grassModel = loader.loadToVAO(model2.getVertices(), model2.getTextureCoords(), model2.getNormals(), model2.getIndices());
        ModelData model3 = OBJParser.loadOBJ("fern");
        RawModel fernModel = loader.loadToVAO(model3.getVertices(), model3.getTextureCoords(), model3.getNormals(), model3.getIndices());
        ModelData model4 = OBJParser.loadOBJ("pine");
//        RawModel bobbleModel = loader.loadToVAO(model4.getVertices(), model3.getTextureCoords(), model3.getNormals(), model3.getIndices());
        ModelData model5 = OBJParser.loadOBJ("lamp");
        RawModel lampModel = loader.loadToVAO(model5.getVertices(), model5.getTextureCoords(), model5.getNormals(), model5.getIndices());

        TexturedModel tree = new TexturedModel(treeModel, new Texture(loader.loadTexture("tree")));
//        tree.getTexture().setHasTransparency(true);
        TexturedModel grass = new TexturedModel(grassModel, new Texture(loader.loadTexture("grassTexture")));
        Texture texture = grass.getTexture();
        texture.setUseFakeLighting(true);
        texture.setHasTransparency(true);
        TexturedModel bush = new TexturedModel(fernModel, new Texture(loader.loadTexture("fernAtlas")));
        bush.getTexture().setHasTransparency(true);
        bush.getTexture().setNumOfRows(2);
        TexturedModel bobble = new TexturedModel(OBJLoader.loadObjModel("pine", loader), new Texture(loader.loadTexture("pine")));
        bobble.getTexture().setHasTransparency(true);
        bobble.getTexture().setUseFakeLighting(true);
        TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader), new Texture(loader.loadTexture("lamp")));
        lamp.getTexture().setUseFakeLighting(true);

        ModelData model6 = OBJParser.loadOBJ("person");
        RawModel manModel = loader.loadToVAO(model6.getVertices(), model6.getTextureCoords(), model6.getNormals(), model6.getIndices());
        TexturedModel man = new TexturedModel(manModel, new Texture(loader.loadTexture("playerTexture")));
//        man.getTexture().setReflectivity(1.1f);
        Player player = new Player(man, new Vector3f(100, 15, -150), 0,180,0, 1);

//        texture.setShineDamper(15);
//        texture.setReflectivity(1);

        Terrain terrain = new Terrain(0,- 1, loader, texturePack, blendMap, "heightMap");
//        Terrain terrain2 = new Terrain(-1,-1, loader, texturePack, blendMap, "heightMap");
        List<Entity> entities = new ArrayList<>();
        Random random = new Random(676452);
        for(int i=0;i<400;i++){
            if(i % 3 == 0){
                float x = random.nextFloat()*400 - 200;
                float z = random.nextFloat() * -400;
                float y = terrain.getTerrainHeight(x, z);
                entities.add(new Entity(grass, new Vector3f(x,y,z),0,0,0,0.6f));
            }
            if(i % 2 == 0){
                float x = random.nextFloat()*400 - 200;
                float z = random.nextFloat() * -400;
                float y = terrain.getTerrainHeight(x, z);

                entities.add(new Entity(bush, random.nextInt(4), new Vector3f(x, y, z), 0,random.nextFloat()*360,0,0.9f));

                x = random.nextFloat()*800 - 400;
                z = random.nextFloat() * -600;
                y = terrain.getTerrainHeight(x, z);

                entities.add(new Entity(bobble, random.nextInt(4), new Vector3f(x,y,z),0,random.nextFloat()*360,0,
                        random.nextFloat()*0.1f + 0.6f));

//                x = random.nextFloat()*800 - 400;
//                z = random.nextFloat() * -600;
//                y = terrain.getTerrainHeight(x, z);
//
//                entities.add(new Entity(tree, new Vector3f(x, y, z), 0,0,0,random.nextFloat() * 1 + 4));
            }
        }

//      Entity entity = new Entity(texturedModel, new Vector3f(0, -0.2f, -2), 0,0,0,0.06f);//place an object somewhere in the center
//      Entity entity = new Entity(grass, new Vector3f(0, 0, -25), 0,0,0,1);//place an object somewhere in the center
        List<Light> lights = new ArrayList<>();
        lights.add(new Light(new Vector3f(185, 1000, -7000), new Vector3f(0.4f, 0.4f, 0.4f)));
        lights.add(new Light(new Vector3f(185, 12.5f, -293), new Vector3f(2, 0, 0), new Vector3f(0.5f, 0.001f, 0.0002f)));//tweak these to change brightness/attenuation
        lights.add(new Light(new Vector3f(370, 25f, -300), new Vector3f(0, 2, 2), new Vector3f(0.5f, .001f, 0.0002f)));
        lights.add(new Light(new Vector3f(293, 17, -305), new Vector3f(2, 2, 0), new Vector3f(0.5f, .001f, 0.0002f)));

        entities.add(new Entity(lamp, new Vector3f(185, -5f, -293), 0,0,0,1));
        entities.add(new Entity(lamp, new Vector3f(370, 4.2f, -300), 0,0,0,1));
        entities.add(new Entity(lamp, new Vector3f(293, -6.8f, -315), 0,0,0,1));

        Camera camera = new Camera(player);
        MasterRenderer masterRenderer = new MasterRenderer(loader);
        List<HUDTexture> guis = new ArrayList<>();
        HUDTexture hudTexture = new HUDTexture(loader.loadTexture("health"), new Vector2f(0.6f, 0.8f), new Vector2f(0.25f, 0.25f));
        guis.add(hudTexture);
        HUDRenderer hudRenderer = new HUDRenderer(loader);

        while(!Display.isCloseRequested()){
//            entity.increaseRotation(0, 1, 0);
            camera.move();
            player.move(terrain);
            masterRenderer.processEntity(player);
            masterRenderer.processTerrain(terrain);
//            masterRenderer.processTerrain(terrain2);
            for(Entity e:entities){
                masterRenderer.processEntity(e);
            }

            masterRenderer.render(lights, camera);
            hudRenderer.render(guis);
            DisplayManager.updateDisplay();
        }

        hudRenderer.clear();
        masterRenderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
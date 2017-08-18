import display.DisplayManager;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector3f;
import parser.ModelData;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import parser.OBJParser;
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
        ModelData model4 = OBJParser.loadOBJ("lowPolyTree");
        RawModel bobbleModel = loader.loadToVAO(model4.getVertices(), model3.getTextureCoords(), model3.getNormals(), model3.getIndices());

        TexturedModel tree = new TexturedModel(treeModel, new Texture(loader.loadTexture("tree")));
//        tree.getTexture().setHasTransparency(true);
        TexturedModel grass = new TexturedModel(grassModel, new Texture(loader.loadTexture("grassTexture")));
        Texture texture = grass.getTexture();
        texture.setUseFakeLighting(true);
        texture.setHasTransparency(true);
        TexturedModel bush = new TexturedModel(fernModel, new Texture(loader.loadTexture("fern")));
        bush.getTexture().setHasTransparency(true);
        TexturedModel bobble = new TexturedModel(bobbleModel, new Texture(loader.loadTexture("lowPolyTree")));
        bush.getTexture().setHasTransparency(true);

        ModelData model5 = OBJParser.loadOBJ("stanfordBunny");
        RawModel bunnyModel = loader.loadToVAO(model5.getVertices(), model5.getTextureCoords(), model5.getNormals(), model5.getIndices());
        TexturedModel stanfordBunny = new TexturedModel(bunnyModel, new Texture(loader.loadTexture("white")));
        Player player = new Player(stanfordBunny, new Vector3f(100, 0, -50), 0,0,0, 1);
//
//        texture.setShineDamper(15);
//        texture.setReflectivity(1);

        List<Entity> entities = new ArrayList<>();
        Random random = new Random(676452);
        for(int i=0;i<400;i++){
            if(i % 7 == 0){
                entities.add(new Entity(grass, new Vector3f(random.nextFloat()*400 - 200,0,random.nextFloat() * -400),0,0,0,1));

            }
            if(i % 3 == 0){
                entities.add(new Entity(bush, new Vector3f(random.nextFloat()*400 - 200, 0, random.nextFloat()* -400), 0,random.nextFloat()*360,0,0.9f));
                entities.add(new Entity(bobble, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,random.nextFloat()*360,0,
                        random.nextFloat()*0.1f + 0.6f));
                entities.add(new Entity(tree, new Vector3f(random.nextFloat()*800 - 400, 0, random.nextFloat()* -600), 0,0,0,random.nextFloat() * 1 + 4));
            }
        }

//      Entity entity = new Entity(texturedModel, new Vector3f(0, -0.2f, -2), 0,0,0,0.06f);//place an object somewhere in the center
//      Entity entity = new Entity(grass, new Vector3f(0, 0, -25), 0,0,0,1);//place an object somewhere in the center
        Light light = new Light(new Vector3f(player.getPosition().x, 20000, 20000), new Vector3f(1, 1, 1));

        Terrain terrain = new Terrain(0,- 1, loader, texturePack, blendMap);
        Terrain terrain2 = new Terrain(-1,-1, loader, texturePack, blendMap);

        Camera camera = new Camera(player);

        MasterRenderer masterRenderer = new MasterRenderer();
        while(!Display.isCloseRequested()){
//            entity.increaseRotation(0, 1, 0);
            camera.move();
            player.move();
            masterRenderer.processEntity(player);
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
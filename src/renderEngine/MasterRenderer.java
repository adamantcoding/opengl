package renderEngine;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;
import shaders.TerrainRenderer;
import shaders.TerrainShader;
import terrains.Terrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ivan on 26.7.2017.
 */
public class MasterRenderer {

    private Matrix4f projectionMatrix;
    private StaticShader staticShader = new StaticShader();
    private EntityRenderer entityRenderer;

    private TerrainRenderer terrainRenderer;
    private TerrainShader terrainShader = new TerrainShader();

    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
    private List<Terrain> terrains = new ArrayList<>();

    private static final float RED = 0.51f;  //0.5 for grey sky, 0.8 pinky
    private static final float GREEN = 0.5f;
    private static final float BLUE = 0.55f;

    private static final float FOV = 90;
    private static final float FAR = 1000;
    private static final float NEAR = 1f;

    public MasterRenderer(){
        enableCulling();
        createProjectionMatrix();
        entityRenderer = new EntityRenderer(staticShader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
    }

    /**
     * clears out the screen at each frame
     */
    public void clear(){
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT| GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(RED, GREEN, BLUE, 0);  //here is how the "sky" color is set
    }

    public void render(List<Light> lights, Camera camera){
        clear();
        staticShader.start();
        staticShader.loadSkyColor(RED, GREEN, BLUE);
        staticShader.loadLights(lights);
        staticShader.loadViewMatrix(camera);
        entityRenderer.render(entities);
        staticShader.stop();

        terrainShader.start();
        terrainShader.loadSkyColor(RED, GREEN, BLUE);
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains);
        terrainShader.stop();

        terrains.clear();
        entities.clear();
    }

    public void processTerrain(Terrain terrain){
        terrains.add(terrain);
    }

    public void processEntity(Entity entity){
        TexturedModel texturedModel = entity.getTexturedModel();
        List<Entity> batch = entities.get(texturedModel);
        if(batch != null){
            batch.add(entity);
        } else {
            List<Entity> newList = new ArrayList<>();
            newList.add(entity);
            entities.put(texturedModel, newList);
        }
    }

    public static void enableCulling(){
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public static void disableCulling(){
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    private void createProjectionMatrix(){
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV/2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR - NEAR;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR + NEAR) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((1 * NEAR * FAR) / frustum_length); //change to 2 or 3 depending how close you want to allow it to get without getting "swallowed" by the background
        projectionMatrix.m33 = 0; // how far on z axis is it by default
    }

    public void cleanUp(){
        staticShader.clean();
        terrainShader.clean();
    }
}
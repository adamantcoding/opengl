package renderEngine;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ivan on 26.7.2017.
 */
public class MasterRenderer {
    private StaticShader staticShader = new StaticShader();
    private Renderer renderer = new Renderer(staticShader);
    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();

    public void render(Light light, Camera camera){
        renderer.clear();
        staticShader.start();
        staticShader.loadLight(light);
        staticShader.loadViewMatrix(camera);
        renderer.render(entities);
        staticShader.stop();
        entities.clear();
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

    public void cleanUp(){
        staticShader.clean();
    }
}
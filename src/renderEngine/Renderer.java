package renderEngine;

import entities.Entity;
import models.TexturedModel;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;
import textures.Texture;
import utils.MathUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by Ivan on 20.7.2017.
 */
public class Renderer {
    private static final float FOV = 70;
    private static final float FAR = 1000;
    private static final float NEAR = 1f;
    private Matrix4f projectionMatrix;
    private StaticShader shader;

    public Renderer(StaticShader shader){
        this.shader = shader;
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        createProjectionMatrix();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }
    /**
     * clears out the screen at each frame
     */
    public void clear(){
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT| GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(1, 0, 0, 1);
    }

    public void render(Map<TexturedModel, List<Entity>> entities){
        for(TexturedModel model : entities.keySet()){
            prepareTexturedModel(model);
            List<Entity> batch = entities.get(model);
            for(Entity entity : batch){
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0); //indices based
            }
            disable();//unbind textured model
        }
    }

    private void prepareTexturedModel(TexturedModel texturedModel){
        GL30.glBindVertexArray(texturedModel.getRawModel().getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        Texture texture = texturedModel.getTexture();
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getTextureID());

    }

    private void prepareInstance(Entity entity){
        Matrix4f transformMatrix = MathUtils.createTransformationMatrix(entity.getPosition(), entity.getRx(), entity.getRy(), entity.getRz(), entity.getScale());
        shader.loadTransformationMatrix(transformMatrix);
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

    private void disable(){
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }
}
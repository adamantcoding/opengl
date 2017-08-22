package hud;

import models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import renderEngine.Loader;
import utils.MathUtils;

import java.util.List;

/**
 * Created by Ivan on 21.8.2017.
 */
public class HUDRenderer {
    private final RawModel quad;
    private HUDShader shader;

    public HUDRenderer(Loader loader) {
        float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
        quad = loader.loadToVAO(positions);
        shader = new HUDShader();
    }

    public void render(List<HUDTexture> guis){
        shader.start();
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL11.glEnable(GL11.GL_BLEND); //make a 2d hud texture transparent (alpha-bending)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST); //in case there're 2 textures, avoid overlapping

        for (HUDTexture texture: guis) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
            Matrix4f transformMatrix = MathUtils.createTransformationMatrix(texture.getPosition(), texture.getScale());
            shader.loadTransformation(transformMatrix);
            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
        }

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }

    public void clear(){
        shader.clean();
    }
}
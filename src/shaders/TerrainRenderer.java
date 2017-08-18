package shaders;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import terrains.Terrain;
import textures.TerrainTexturePack;
import textures.Texture;
import utils.MathUtils;
import java.util.List;

/**
 * Created by Ivan on 26.7.2017.
 */
public class TerrainRenderer {
    private TerrainShader terrainShader;
    public TerrainRenderer(TerrainShader terrainShader, Matrix4f projectionMatrix){
        this.terrainShader = terrainShader;
        terrainShader.start();
        terrainShader.loadProjectionMatrix(projectionMatrix);
        terrainShader.connectTextureUnits();
        terrainShader.stop();
    }

    public void render(List<Terrain> terrains){
        for (Terrain terrain: terrains){
            prepareTerrain(terrain);
            loadModelMatrix(terrain);
            GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0); //indices based
            disable();
        }
    }

    private void prepareTerrain(Terrain terrain){
//        System.out.println("Terrain" + terrain);
//        System.out.println(terrain.getRawModel());
        GL30.glBindVertexArray(terrain.getRawModel().getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        bindTextures(terrain);
        terrainShader.loadShineVariables(1, 0);
    }

    private void bindTextures(Terrain terrain){
        TerrainTexturePack pack = terrain.getTerrainTexturePack();

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, pack.getBackgroundTexture().getTextureID());

        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, pack.getrTexture().getTextureID());

        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, pack.getgTexture().getTextureID());

        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, pack.getbTexture().getTextureID());

        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap().getTextureID());
    }

    private void loadModelMatrix(Terrain terrain){
        Matrix4f transformMatrix = MathUtils.createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1);
        terrainShader.loadTransformationMatrix(transformMatrix);
    }

    private void disable(){
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }
}
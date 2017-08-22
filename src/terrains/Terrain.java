package terrains;

import models.RawModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import textures.Texture;
import utils.MathUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Ivan on 26.7.2017.
 */
public class Terrain {
    private static final float SIZE = 800;
    private static final float MAX_HEIGHT = 40, MIN_HEIGHT = -40;
    private static final float MAX_PIXEL_COLOR = 256 * 256 * 256;

    private float x, z;
    private RawModel rawModel;
    private TerrainTexturePack texturePack;
    private TerrainTexture blendMap;

    private float[][] heights;

    public Terrain(float x, float z, Loader loader, TerrainTexturePack terrainTexturePack, TerrainTexture blendMap, String heightMap) {
        this.x = x * SIZE;
        this.z = z * SIZE;
        this.texturePack = terrainTexturePack;
        this.blendMap = blendMap;
        this.rawModel = generateTerrain(loader, heightMap);
    }

    private RawModel generateTerrain(Loader loader, String heightMap){
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("res/" + heightMap + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int vertexCount = image.getHeight();
        heights = new float[vertexCount][vertexCount];

        int count = vertexCount * vertexCount;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*2];
        int[] indices = new int[6*(vertexCount-1)*(vertexCount-1)];
        int vertexPointer = 0;
        for(int i=0;i<vertexCount;i++){
            for(int j=0;j<vertexCount;j++){
                vertices[vertexPointer*3] = (float)j/((float)vertexCount - 1) * SIZE;
                float height = getHeight(j, i, image);
                vertices[vertexPointer*3+1] = height;
                heights[j][i] = height;
                vertices[vertexPointer*3+2] = (float)i/((float)vertexCount - 1) * SIZE;
                Vector3f normal = calcNormal(j, i, image);
                normals[vertexPointer*3] = normal.x;
                normals[vertexPointer*3+1] = normal.y;
                normals[vertexPointer*3+2] = normal.z;
                textureCoords[vertexPointer*2] = (float)j/((float)vertexCount - 1);
                textureCoords[vertexPointer*2+1] = (float)i/((float)vertexCount - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for(int gz=0;gz<vertexCount-1;gz++){
            for(int gx=0;gx<vertexCount-1;gx++){
                int topLeft = (gz*vertexCount)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*vertexCount)+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        return loader.loadToVAO(vertices, textureCoords, normals, indices);
    }

    private float getHeight(int x, int y, BufferedImage img){
        if(x < 0 || x >= img.getHeight() || y < 0 || y >= img.getHeight()){
            return 0;
        }
        float height = img.getRGB(x, y);
        height += MAX_PIXEL_COLOR / 2f;
        height /= MAX_PIXEL_COLOR / 2f;
        height *= MAX_HEIGHT;
        return height;
    }

    public float getTerrainHeight(float coordX, float coordZ){
        float terrainX = coordX - this.x;
        float terrainZ = coordZ - this.z;
        float gridSquareSize = SIZE / ((float)heights.length - 1);
        int gridX = (int) Math.floor(terrainX / gridSquareSize);
        int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
        if(gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0){
            return 0;
        }
        float currentX = (terrainX % gridSquareSize) / gridSquareSize;
        float currentZ = (terrainZ % gridSquareSize) / gridSquareSize;
        float result;
        if(currentX <= 1 - currentZ){
            result = MathUtils.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1, heights[gridX + 1][gridZ + 1], 1), new Vector3f(0, heights[gridX][gridZ + 1], 1),
                    new Vector2f(currentX, currentZ));
        } else {
            result = MathUtils.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1, heights[gridX + 1][gridZ + 1], 1), new Vector3f(0, heights[gridX][gridZ + 1], 1),
                    new Vector2f(currentX, currentZ));
        }
        return result;
    }

    private Vector3f calcNormal(int x, int y, BufferedImage img){
        float heightL = getHeight(x - 1, y, img);
        float heightR = getHeight(x + 1, y, img);
        float heightD = getHeight(x, y - 1, img);
        float heightU = getHeight(x, y +1, img);
        Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
        normal.normalise();
        return normal;
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public TerrainTexturePack getTerrainTexturePack() {
        return this.texturePack;
    }
    public TerrainTexture getBlendMap(){
        return this.blendMap;
    }
}
package renderEngine;

import models.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan on 20.7.2017.
 * Loads data into memory (prepares VAO)
 */
public class Loader {
    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> textures = new ArrayList<>();

    public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices){
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        sendDataToAttribList(0, 3, positions);
        sendDataToAttribList(1, 2, textureCoords);
        sendDataToAttribList(2, 3, normals);
        unbindVAO();
        return new RawModel(vaoID, indices.length);
    }

    public int loadTexture(String texturePath){
        Texture texture = null;
        try {
            texture = TextureLoader.getTexture("PNG", new FileInputStream("res/" + texturePath + ".png"));
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int id = texture.getTextureID();
        textures.add(id);
        return id;
    }

    public void cleanUp(){
        for(int vao: vaos){
            GL30.glDeleteVertexArrays(vao);
        }
        for(int vbo: vbos){
            GL15.glDeleteBuffers(vbo);
        }
        for(int texture: textures){
            GL11.glDeleteTextures(texture);
        }
    }

    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    /**
     *
     * @param index index of the VBO in the VAO where data is going to be put
     * @param positions
     */
    private void sendDataToAttribList(int index, int coordSize, float[] positions) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer fb = loadDataToFloatBuffer(positions);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, fb, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(index, coordSize, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    private void bindIndicesBuffer(int[] indices){
        //create a new vbo
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID); //element array = index array
        IntBuffer intBuffer = loadDataToIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, intBuffer, GL15.GL_STATIC_DRAW);
    }

    private FloatBuffer loadDataToFloatBuffer(float[] data){
        FloatBuffer fb = BufferUtils.createFloatBuffer(data.length);
        fb.put(data);
        fb.flip();
        return fb;
    }

    /**
     * processes indices array
     * @param data
     * @return
     */
    private IntBuffer loadDataToIntBuffer(int[] data){
        IntBuffer intBuffer = BufferUtils.createIntBuffer(data.length);
        intBuffer.put(data);
        intBuffer.flip();
        return intBuffer;
    }
}
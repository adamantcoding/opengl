package entities;

import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Ivan on 23.7.2017.
 */
public class Entity {
    private TexturedModel texturedModel;
    private Vector3f position;
    private float rx, ry, rz; //rotation parameters
    private float scale;
    private int textureIndex = 0;

    public Entity(TexturedModel texturedModel, Vector3f position, float rx, float ry, float rz, float scale) {
        this.texturedModel = texturedModel;
        this.position = position;
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
        this.scale = scale;
    }

    public Entity(TexturedModel texturedModel, int index, Vector3f position, float rx, float ry, float rz, float scale) {
        this.textureIndex = index;
        this.texturedModel = texturedModel;
        this.position = position;
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
        this.scale = scale;
    }

    public void increasePosition(float x, float y, float z){
        this.position.x += x;
        this.position.y += y;
        this.position.z += z;
    }

    public void increaseRotation(float x, float y, float z){
        this.rx += x;
        this.ry += y;
        this.rz += z;
    }

    public float getTextureXOffset(){
        int column = textureIndex % texturedModel.getTexture().getNumOfRows();
        return (float) column/(float) texturedModel.getTexture().getNumOfRows();
    }

    public float getTextureYOffset(){
        int row = textureIndex % texturedModel.getTexture().getNumOfRows();
        return (float) row/(float) texturedModel.getTexture().getNumOfRows();
    }

    public TexturedModel getTexturedModel() {
        return texturedModel;
    }

    public void setTexturedModel(TexturedModel texturedModel) {
        this.texturedModel = texturedModel;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRx() {
        return rx;
    }

    public void setRx(float rx) {
        this.rx = rx;
    }

    public float getRy() {
        return ry;
    }

    public void setRy(float ry) {
        this.ry = ry;
    }

    public float getRz() {
        return rz;
    }

    public void setRz(float rz) {
        this.rz = rz;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
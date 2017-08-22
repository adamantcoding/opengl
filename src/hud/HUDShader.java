package hud;

import org.lwjgl.util.vector.Matrix4f;
import shaders.AbstractShader;

/**
 * Created by Ivan on 21.8.2017.
 */
public class HUDShader extends AbstractShader {
    private static final String VERTEX_FILE = "C:\\Users\\Ivan\\IdeaProjects\\opengl\\src\\shaders\\HUDVertexShader.glsl";
    private static final String FRAGMENT_FILE = "C:\\Users\\Ivan\\IdeaProjects\\opengl\\src\\shaders\\HUDFragmentShader.glsl";

    private int location_transformationMatrix;

    public HUDShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public void loadTransformation(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
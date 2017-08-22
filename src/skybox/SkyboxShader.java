package skybox;

import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import shaders.AbstractShader;
import utils.MathUtils;

/**
 * Created by Ivan on 22.8.2017.
 */
public class SkyboxShader extends AbstractShader {
    private static final String VERTEX_FILE = "C:\\Users\\Ivan\\IdeaProjects\\opengl\\src\\skybox\\skyboxVertexShader.glsl";
    private static final String FRAGMENT_FILE = "C:\\Users\\Ivan\\IdeaProjects\\opengl\\src\\skybox\\skyboxFragmentShader.glsl";

    private int location_projectionMatrix;
    private int location_viewMatrix;

    public SkyboxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera){
        Matrix4f matrix = MathUtils.createViewMatrix(camera);
        matrix.m30 = 0;
        matrix.m31 = 0;
        matrix.m32 = 0;
        super.loadMatrix(location_viewMatrix, matrix);
    }

    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
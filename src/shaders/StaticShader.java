package shaders;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import utils.MathUtils;

import java.util.List;

/**
 * Created by Ivan on 20.7.2017.
 */
public class StaticShader extends AbstractShader {

    private static final String VERTEX_SHADER = "C:\\Users\\Ivan\\IdeaProjects\\opengl\\src\\shaders\\vertexShader.glsl";//FIXME try to use relative path
    private static final String FRAGMENT_SHADER = "C:\\Users\\Ivan\\IdeaProjects\\opengl\\src\\shaders\\fragmentShader.glsl";

    private final static int MAX_LIGHTS = 4;

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition[];
    private int location_lightColor[];
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_useFakeLighting;
    private int location_skyColor;
    private int location_numOfRows;
    private int location_offset;

    public StaticShader() {
        super(VERTEX_SHADER, FRAGMENT_SHADER);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "pos");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_shineDamper = super.getUniformLocation("shineDamper");
        location_reflectivity = super.getUniformLocation("reflectivity");
        location_useFakeLighting = super.getUniformLocation("useFakeLighting");
        location_skyColor = super.getUniformLocation("skyColor");
        location_numOfRows = super.getUniformLocation("numOfRows");
        location_offset = super.getUniformLocation("offset");

        location_lightPosition = new int[MAX_LIGHTS];
        location_lightColor = new int[MAX_LIGHTS];
        for(int i = 0; i < MAX_LIGHTS; i++){
            location_lightPosition[i] = super.getUniformLocation("lightPos[" + i + "]");
            location_lightColor[i] = super.getUniformLocation("lightColor[" + i + "]");
        }
    }

    public void loadNumOfRows(float value){
        super.loadFloat(location_numOfRows, value);
    }

    public void loadOffset(float x, float y){
        super.loadVector2f(location_offset, new Vector2f(x, y));
    }

    public void loadUseFakeLighting(boolean use){
        super.loadBoolean(location_useFakeLighting, use);
    }

    public void loadSkyColor(float r, float g, float b){
        super.loadVector(location_skyColor, new Vector3f(r, g, b));
    }

    public void loadLights(List<Light> lights){
        for(int i = 0; i < MAX_LIGHTS; i++){
            if(i < lights.size()){
                super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
                super.loadVector(location_lightColor[i], lights.get(i).getColor());
            } else {
                super.loadVector(location_lightPosition[i], new Vector3f(0,0,0));
                super.loadVector(location_lightColor[i], new Vector3f(0,0,0));
            }
        }
    }

    public void loadShineVariables(float damper, float reflectivity){
        super.loadFloat(location_shineDamper, damper);
        super.loadFloat(location_reflectivity, reflectivity);
    }

    public void loadTransformationMatrix(Matrix4f matrix4f){
        super.loadMatrix(location_transformationMatrix, matrix4f);
    }

    public void loadProjectionMatrix(Matrix4f matrix4f){
        super.loadMatrix(location_projectionMatrix, matrix4f);
    }

    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix = MathUtils.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }
}
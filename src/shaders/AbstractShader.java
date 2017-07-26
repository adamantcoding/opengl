package shaders;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

/**
 * Created by Ivan on 20.7.2017.
 */
public abstract class AbstractShader {
    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;
    private static FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(16);

    public AbstractShader(String vertexShaderPath, String fragmentShaderPath){
        vertexShaderID = loadShader(vertexShaderPath, GL20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentShaderPath, GL20.GL_FRAGMENT_SHADER);
        programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        bindAttributes();
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);
//        start();
        getAllUniformLocations();
    }

    protected int getUniformLocation(String uniformName){
        return GL20.glGetUniformLocation(programID, uniformName);
    }

    protected void loadFloat(int location, float value){
        GL20.glUniform1f(location, value);
    }

    protected void loadVector(int location, Vector3f value){
        GL20.glUniform3f(location, value.getX(), value.getY(), value.getZ());
    }

    protected void loadBoolean(int location, boolean value){
        float toLoad = 0;
        if(value){
            toLoad = 1;
        }
        GL20.glUniform1f(location, toLoad);
    }

    protected void loadMatrix(int location, Matrix4f matrix4f){
        matrix4f.store(floatBuffer);
        floatBuffer.flip();
        GL20.glUniformMatrix4(location, false, floatBuffer);
    }

    public void start(){
        GL20.glUseProgram(programID); //run the program
    }

    public void stop(){
        GL20.glUseProgram(0);
    }

    public void clean(){
        stop();
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(programID);
    }

    /**
     * links the inputs of shaders with vao attributes
     */
    protected abstract void bindAttributes();
    protected abstract void getAllUniformLocations();

    protected void bindAttribute(int attribIndex, String varName){
        GL20.glBindAttribLocation(programID, attribIndex, varName);
    }

    private static int loadShader(String path, int type){
        StringBuilder shaderSource = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new FileReader(path))){
            String line = null;
            while((line = reader.readLine()) != null){
                shaderSource.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if(GL20.glGetShader(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE){
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.exit(-1);
        }
        return shaderID;
    }

}
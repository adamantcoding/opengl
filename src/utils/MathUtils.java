package utils;

import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Ivan on 23.7.2017.
 */
public class MathUtils {
    public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale){
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.setIdentity();
        matrix4f.translate(translation, matrix4f, matrix4f);//model is getting distanced on a z axis
//        matrix4f.rotate((float)Math.toRadians(rx), new Vector3f(1, 0,0), matrix4f, matrix4f); //tupsy turvy on a z direction
        matrix4f.rotate((float)Math.toRadians(rx), new Vector3f(0, 1,0), matrix4f, matrix4f); //horizontally(y)
//        matrix4f.rotate((float)Math.toRadians(rx), new Vector3f(0, 0,1), matrix4f, matrix4f);//tupsy turvy on x direction
        matrix4f.scale(new Vector3f(scale, scale, scale), matrix4f, matrix4f);
        return matrix4f;
    }

    public static Matrix4f createViewMatrix(Camera camera){
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.setIdentity();
        Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix,
                viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
        Vector3f cameraPos = camera.getPosition();
        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
        Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
        return viewMatrix;
    }
}
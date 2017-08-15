package entities;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Ivan on 23.7.2017.
 */
public class Camera {
    private Vector3f position = new Vector3f(0,5,0);
    private float pitch = 10;
    private float yaw ;
    private float roll;

    public Camera(){}

    public void move(){
        try {
            Mouse.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
            position.z-=0.02f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            position.x+=0.02f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_A)){
            position.x-=0.02f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            position.z+=0.02f;
        }
        if(Mouse.isButtonDown(0)){
            pitch +=0.03f;
        }
        if(Mouse.isButtonDown(1)){
            pitch -=0.02f;
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }
}
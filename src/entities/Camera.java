package entities;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Ivan on 23.7.2017.
 */
public class Camera {
    private Vector3f position = new Vector3f(120,15,0);
    private float pitch = 1;
    private float yaw ; //rotation around y
    private float roll; //rotation around z
    private float distanceFromPlayer = 50;
    private float angleAroundPlayer = 0;

    private Player player;

    public Camera(Player player){
        this.player = player;
    }

    public void move(){
        calcZoom();
        calcPitch();
        calcAngleAroundPlayer();
        float horizontalDistance = calcHorizontalDistance();
        float verticalDistance = calcVerticalDistance();
        calcCameraPosition(horizontalDistance, verticalDistance);
        this.yaw = 180 - (player.getRy() + angleAroundPlayer);
    }

    private void calcZoom(){
        float zoom = Mouse.getDWheel() * 0.01f;
        distanceFromPlayer -= zoom;
    }

    private void calcPitch(){
        if(Mouse.isButtonDown(1)){
            float pitchChange = Mouse.getDY() * 0.01f;
            pitch -= pitchChange;
        }
    }

    private void calcAngleAroundPlayer(){
        if(Mouse.isButtonDown(0)){
            float angleChange = Mouse.getDX() * 0.03f;
            angleAroundPlayer -= angleChange;
        }
    }

    private void calcCameraPosition(float horizDistance, float vertDistance){
        float theta = player.getRy() + angleAroundPlayer;
        float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
        position.x = player.getPosition().x - offsetX;
        position.z = player.getPosition().z - offsetZ;
        position.y = player.getPosition().y + vertDistance;
    }

    private float calcHorizontalDistance(){
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }

    private float calcVerticalDistance(){
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
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
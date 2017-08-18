package entities;

import display.DisplayManager;
import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Ivan on 15.8.2017.
 */
public class Player extends Entity {

    private static final float RUN_SPEED = 20;
    private static final float TURN_SPEED = 30;
    private static final float GRAVITY = -50;
    private static final float JUMP_POWER = 30;
    private static final float TERRAIN_HEIGHT = 0;

    private float currentRunSpeed = 0;
    private float currentTurnSpeed = 0;
    private float upwardsSpeed = 0;
    private boolean isInAir = false;

    public Player(TexturedModel texturedModel, Vector3f position, float rx, float ry, float rz, float scale) {
        super(texturedModel, position, rx, ry, rz, scale);
    }

    public void move(){
        checkInputs();
        super.increaseRotation(0, currentTurnSpeed * DisplayManager.getDelta(), 0);
        float distance = currentRunSpeed * DisplayManager.getDelta();
        float dx = (float) (distance * Math.sin(Math.toRadians(super.getRy())));
        float dz = (float) (distance * Math.cos(Math.toRadians(super.getRy())));
        super.increasePosition(dx, 0, dz);
        upwardsSpeed += GRAVITY * DisplayManager.getDelta();
        super.increasePosition(0, upwardsSpeed * DisplayManager.getDelta(), 0);
        if(super.getPosition().y < TERRAIN_HEIGHT){
            upwardsSpeed = 0;
            isInAir = false;
            super.getPosition().y = TERRAIN_HEIGHT;
        }
    }

    private void jump(){
        if(!isInAir){
            upwardsSpeed = JUMP_POWER;
            isInAir = true;
        }
    }

    private void checkInputs(){
        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
            this.currentRunSpeed = RUN_SPEED;
        } else if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            this.currentRunSpeed = -RUN_SPEED;
        } else {
            this.currentRunSpeed = 0;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            this.currentTurnSpeed = -TURN_SPEED;
        } else if(Keyboard.isKeyDown(Keyboard.KEY_A)){
            this.currentTurnSpeed = TURN_SPEED;
        } else {
            currentTurnSpeed = 0;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            jump();
        }
    }
}
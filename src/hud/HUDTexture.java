package hud;

import org.lwjgl.util.vector.Vector2f;

/**
 * Created by Ivan on 21.8.2017.
 */
public class HUDTexture {
    private int textureID;
    private Vector2f position;
    private Vector2f scale;

    public HUDTexture(int textureID, Vector2f position, Vector2f scale) {
        this.textureID = textureID;
        this.position = position;
        this.scale = scale;
    }

    public int getTextureID() {
        return textureID;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getScale() {
        return scale;
    }
}

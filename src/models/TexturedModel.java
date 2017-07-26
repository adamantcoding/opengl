package models;

import textures.Texture;

/**
 * Created by Ivan on 20.7.2017.
 * Represents a textured object
 */
public class TexturedModel {
    private RawModel rawModel;
    private Texture texture;

    public TexturedModel(RawModel rawModel, Texture texture) {
        this.rawModel = rawModel;
        this.texture = texture;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public Texture getTexture() {
        return texture;
    }
}
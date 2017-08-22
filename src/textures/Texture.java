package textures;

/**
 * Created by Ivan on 20.7.2017.
 */
public class Texture {
    private int textureID;
    private float shineDamper = 1;
    private float reflectivity = 0;
    private boolean hasTransparency = false;
    private boolean useFakeLighting = false;

    private int numOfRows = 1; //for texture atlases

    public int getNumOfRows() {
        return numOfRows;
    }
    public void setNumOfRows(int numOfRows) {
        this.numOfRows = numOfRows;
    }
    public float getShineDamper() {
        return shineDamper;
    }
    public boolean hasTransparency() {
        return hasTransparency;
    }

    public void setHasTransparency(boolean hasTransparency) {
        this.hasTransparency = hasTransparency;
    }

    public boolean isUseFakeLighting() {
        return useFakeLighting;
    }

    public void setUseFakeLighting(boolean useFakeLighting) {
        this.useFakeLighting = useFakeLighting;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    public Texture(int textureID) {
        this.textureID = textureID;
    }

    public int getTextureID() {
        return textureID;
    }
}
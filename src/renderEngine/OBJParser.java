package renderEngine;

import models.RawModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan on 24.7.2017.
 */
public class OBJParser {
    public static RawModel loadObjModel(String filename, Loader loader){
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(new File("res/" + filename + ".obj"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(fileReader);
        String line;
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        float[] verticesArray = null;
        float[] normalsArray = null;
        float[] texturesArray = null;
        int[] indicesArray = null;
        try{
            while (true){
                line = reader.readLine();
                String[] current = line.split(" ");
                if(line.startsWith("v ")){
                    Vector3f vertex = new Vector3f(Float.parseFloat(current[1]), Float.parseFloat(current[2]), Float.parseFloat(current[3]));
                    vertices.add(vertex);
                } else if(line.startsWith("vt ")){
                    Vector2f texture = new Vector2f(Float.parseFloat(current[1]), Float.parseFloat(current[2]));
                    textures.add(texture);
                } else if(line.startsWith("vn ")){
                    Vector3f normal = new Vector3f(Float.parseFloat(current[1]), Float.parseFloat(current[2]), Float.parseFloat(current[3]));
                    normals.add(normal);
                } else if(line.startsWith("f ")){
                    texturesArray = new float[vertices.size() * 2];
                    normalsArray = new float[vertices.size() * 3];
                    break;
                }
            }
            while(line != null){
                if(!line.startsWith("f ")){
                    line = reader.readLine();
                    continue;
                }
                String[] current = line.split(" ");
                String[] vertex1 = current[1].split("/");
                String[] vertex2 = current[2].split("/");
                String[] vertex3 = current[3].split("/");

                processVertex(vertex1, indices, textures, normals, texturesArray, normalsArray);
                processVertex(vertex2, indices, textures, normals, texturesArray, normalsArray);
                processVertex(vertex3, indices, textures, normals, texturesArray, normalsArray);
                line = reader.readLine();
            }
            reader.close();

        } catch (Exception e){
            e.printStackTrace();
        }
        verticesArray = new float[vertices.size()*3];
        indicesArray = new int[indices.size()];
        int vertexPointer = 0;

        for(Vector3f vertex: vertices){
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }
        for(int i = 0; i < indices.size(); i++){
            indicesArray[i] = indices.get(i);
        }
        return loader.loadToVAO(verticesArray, texturesArray, normalsArray, indicesArray);
    }

    private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals, float[] textureArray, float[] normalsArr){
        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
        indices.add(currentVertexPointer);
        Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
        textureArray[currentVertexPointer*2] = currentTex.x;
        textureArray[currentVertexPointer*2 + 1] = 1 - currentTex.y;
        Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
        normalsArr[currentVertexPointer*3] = currentNorm.x;
        normalsArr[currentVertexPointer*3 + 1] = currentNorm.y;
        normalsArr[currentVertexPointer*3 + 2] = currentNorm.z;
    }
}

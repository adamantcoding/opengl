#version 400 core

in vec3 pos;
in vec2 textureCoords;
in vec3 normal;

out vec2 output_textureCoords;
out vec3 surfaceNormal;
out vec3 toLightVector[4];
out vec3 toCameraVector;
out float visibility;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPos[4];
uniform float useFakeLighting;

uniform float numOfRows;
uniform vec2 offset;

const float density = 0.008;
const float gradient = 0.9;

void main() {
    vec4 worldPos = transformationMatrix * vec4(pos, 1.0);
    vec4 posRelativeToCam = viewMatrix * worldPos;
    gl_Position = projectionMatrix * posRelativeToCam;
    output_textureCoords = (textureCoords/numOfRows) + offset;

    vec3 actualNormal = normal;
    if(useFakeLighting > 0.5){
        actualNormal = vec3(0.0, 1.0, 0.0);
    }

    surfaceNormal = (transformationMatrix * vec4(actualNormal, 0.0)).xyz;
    for(int i = 0; i < 4; i++){
        toLightVector[i] = lightPos[i] - worldPos.xyz;
    }

    toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPos.xyz;

    float distance = length(posRelativeToCam.xyz);
    visibility = exp(-pow((distance * density), gradient));
    visibility = clamp(visibility, 0.0, 1.0);
}
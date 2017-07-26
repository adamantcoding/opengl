#version 400 core

in vec3 pos;
in vec2 textureCoords;
in vec3 normal;

out vec2 output_textureCoords;
out vec3 surfaceNormal;
out vec3 toLightVector;
out vec3 toCameraVector;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPos;

void main() {
    vec4 worldPos = transformationMatrix * vec4(pos, 1.0);
    gl_Position = projectionMatrix * viewMatrix * worldPos;
    output_textureCoords = textureCoords;

    surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;
    toLightVector = lightPos - worldPos.xyz;
    toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPos.xyz;
}

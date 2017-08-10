#version 400 core

in vec2 output_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float visibility;

out vec4 outputColor;

uniform sampler2D textureSampler;
uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;

void main() {
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(toLightVector);

    float dotProduct = dot(unitNormal, unitLightVector);
    float brightness = max(dotProduct, 0.2);
    vec3 diffuse = brightness * lightColor;

    vec3 unitVectorToCamera = normalize(toCameraVector);
    vec3 lightDirection = -unitLightVector;
    vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);

    float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
    specularFactor = max(specularFactor, 0.0);
    float dampedFactor = pow(specularFactor, shineDamper);
    vec3 finalSpecular = dampedFactor * reflectivity * lightColor;

    outputColor = vec4(diffuse, 1.0) * texture(textureSampler, output_textureCoords) + vec4(finalSpecular, 1.0);
    outputColor = mix(vec4(skyColor, 1.0), outputColor, visibility);
}

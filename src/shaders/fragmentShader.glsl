#version 400 core

in vec2 output_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector[4];
in vec3 toCameraVector;
in float visibility;

out vec4 outputColor;

uniform sampler2D textureSampler;
uniform vec3 lightColor[4];
uniform vec3 attenuation[4];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;

void main() {
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitVectorToCamera = normalize(toCameraVector);

    vec3 totalDiffuse = vec3(0.0);
    vec3 totalSpecular = vec3(0.0);

    for(int i = 0; i < 4; i++){
        float distance = length(toLightVector[i]);
        float attenFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance*distance);
        vec3 unitLightVector = normalize(toLightVector[i]);
        float dotProduct = dot(unitNormal, unitLightVector);
        float brightness = max(dotProduct, 0.0);

        vec3 lightDirection = -unitLightVector;
        vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);

        float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
        specularFactor = max(specularFactor, 0.0);
        float dampedFactor = pow(specularFactor, shineDamper);
        totalDiffuse = totalDiffuse + (brightness * lightColor[i])/attenFactor;
        totalSpecular = totalSpecular + (dampedFactor * reflectivity * lightColor[i])/attenFactor;
    }
    totalDiffuse = max(totalDiffuse, 0.2);

    vec4 textureColor = texture(textureSampler, output_textureCoords);
    if(textureColor.a < 0.5){
        discard;
    }

    outputColor = vec4(totalDiffuse, 1.0) * textureColor + vec4(totalSpecular, 1.0);
    outputColor = mix(vec4(skyColor, 1.0), outputColor, visibility);
}
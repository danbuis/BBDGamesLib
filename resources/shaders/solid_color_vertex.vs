#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 texCoord;

out vec2 outTexCoord;
out vec4 desiredColor;

uniform mat4 worldMatrix;
uniform mat4 projectionMatrix;
uniform vec4 solidColor;

void main()
{
    gl_Position = projectionMatrix * worldMatrix * vec4(position, 1.0);
    outTexCoord = texCoord;
    desiredColor = solidColor;
}
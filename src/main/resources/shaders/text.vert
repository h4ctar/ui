#version 330 core

uniform mat4 pmv;

layout(location = 0) in vec2 position;
layout(location = 1) in vec2 textureCoordinate;

out vec2 textureCoordinate2;

void main() {
    gl_Position = pmv * vec4(position, 0, 1);
    textureCoordinate2 = textureCoordinate;
}

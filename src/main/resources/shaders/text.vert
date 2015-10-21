#version 120

uniform mat4 pmv;

attribute vec4 position;
attribute vec2 texCoord;

varying vec2 texCoord2;

void main() {
    texCoord2 = texCoord;
    gl_Position = pmv * position;
}

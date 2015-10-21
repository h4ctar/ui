#version 120

uniform mat4 pmv;

attribute vec4 position;

void main() {
    gl_Position = pmv * position;
}

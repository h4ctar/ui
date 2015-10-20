#version 330

uniform mat4 pmv;

layout(location = 0) in vec3 position;

void main() {
    gl_Position = pmv * vec4(position, 1);
}

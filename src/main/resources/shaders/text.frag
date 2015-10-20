#version 330

uniform vec4 color;

uniform sampler2D tex;

in vec2 textureCoordinate2;

out vec4 colorOut;

void main() {
    colorOut = color * texture(tex, textureCoordinate2);
}

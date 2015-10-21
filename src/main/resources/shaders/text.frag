#version 120

uniform sampler2D tex;
uniform vec4 color;

varying vec2 texCoord2;

void main() {
    gl_FragColor = color * texture2D(tex, texCoord2);
}

#version 120

uniform sampler2D tex;

varying vec2 texCoord2;

void main() {
    gl_FragColor = texture2D(tex, texCoord2);
}

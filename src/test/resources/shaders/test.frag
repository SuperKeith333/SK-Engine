#version 330 core

out vec4 FragColor;

uniform float r;

void main() {
    FragColor = vec4(r, 0, 0, 1);
}
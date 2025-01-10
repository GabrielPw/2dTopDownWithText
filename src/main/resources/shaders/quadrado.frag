#version 330 core

out vec4 FragColor;

in vec2 fragPos;
in vec3 fragColor;

uniform float time;

void main()
{

    vec3 red = vec3(1.f, 0.f, 0.f);
    FragColor = vec4(red, 1.f);
};
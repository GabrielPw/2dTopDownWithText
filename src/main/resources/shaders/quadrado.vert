#version 330 core
layout (location = 0) in vec2 aPos;
layout (location = 1) in vec3 aColor;

out vec2 fragPos;
out vec3 fragColor;

uniform mat4 projection;

void main()
{
    fragPos   = aPos;
    fragColor = aColor;

    vec3 position = vec3(aPos, 1.f);

    gl_Position = projection * vec4(position, 1.0);
};
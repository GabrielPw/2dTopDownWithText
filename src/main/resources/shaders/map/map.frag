#version 330 core

in vec2 vertexTextureCoord; // Coordenadas de textura interpoladas
uniform sampler2D atlasTexture; // Textura contendo os glifos

out vec4 FragColor;
void main() {

    FragColor = texture(atlasTexture, vertexTextureCoord);
}
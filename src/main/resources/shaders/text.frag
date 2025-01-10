#version 330 core

in vec2 vertexTextureCoord; // Coordenadas de textura interpoladas
uniform sampler2D textureBitmap; // Textura contendo os glifos
uniform vec3 textColor;      // Cor do texto

out vec4 FragColor;
void main() {
    vec4 sampled = texture(textureBitmap, vertexTextureCoord);
    if (sampled.a < 0.3) discard;  // Descarta pixels invisíveis
    FragColor = vec4(sampled.rgb * textColor, sampled.a);
}
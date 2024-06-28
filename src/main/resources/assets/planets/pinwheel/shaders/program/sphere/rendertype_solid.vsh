#include veil:fog
#include veil:light
#include veil:deferred_utils

layout(location = 0) in vec3 Position;
layout(location = 1) in vec4 Color;
layout(location = 2) in vec2 UV0;
layout(location = 3) in ivec2 UV2;
layout(location = 4) in vec3 Normal;

uniform sampler2D Sampler2;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform vec3 ChunkOffset;
uniform int FogShape;

out float vertexDistance;
out vec4 vertexColor;
out vec2 texCoord0;
out vec4 normal;

#define RADP RAD + 0.001
#define RADN RAD - 0.001

bool insideRegion(vec3 pos) {
    return RADP > pos.x && pos.x > -RADN && RADP > pos.z && pos.z > -RADN;
}

void main() {
    vec3 pos = Position + ChunkOffset;
    #ifdef SPHERE_ENABLED
    vec3 worldPos = playerSpaceToWorldSpace(pos);
    vec3 leftOffset = vec3(-SEP - RAD * 2, 0, 0);
    vec3 topOffset = vec3(0, 0, -SEP - RAD * 2);
    vec3 rightOffset = vec3(SEP + RAD * 2, 0, 0);
    vec3 bottomOffset = vec3(0, 0, SEP + RAD * 2);
    vec3 backOffset = vec3(2 * (SEP + RAD * 2), 0, 0);

    if (insideRegion(worldPos)) {
        vertexColor = vec4(1, 0, 0, 1);
    } else if (insideRegion(worldPos + leftOffset)) {
        vertexColor = vec4(0, 0, 1, 1);
        pos.x -= SEP;
    } else if (insideRegion(worldPos + rightOffset)) {
        vertexColor = vec4(1, 1, 0, 1);
        pos.x += SEP;
    } else if (insideRegion(worldPos + backOffset)) {
        vertexColor = vec4(0, 1, 0, 1);
        pos.x += SEP * 2;
    } else if (insideRegion(worldPos + topOffset)) {
        vertexColor = vec4(1, 1, 1, 1);
        pos.z -= SEP;
    } else if (insideRegion(worldPos + bottomOffset)) {
        vertexColor = vec4(0, 0, 0, 1);
        pos.z += SEP;
    } else return;

    if (insideRegion(VeilCamera.CameraPosition + leftOffset)) pos.x += SEP;
    else if (insideRegion(VeilCamera.CameraPosition + rightOffset)) pos.x -= SEP;
    else if (insideRegion(VeilCamera.CameraPosition + backOffset)) pos.x -= 2 * SEP;
    else if (insideRegion(VeilCamera.CameraPosition + topOffset)) pos.z += SEP;
    else if (insideRegion(VeilCamera.CameraPosition + bottomOffset)) pos.z -= SEP;


//    #else
    #endif
    vertexColor = Color * minecraft_sample_lightmap(Sampler2, UV2);

    gl_Position = ProjMat * ModelViewMat * vec4(pos, 1.0);
    vertexDistance = fog_distance(ModelViewMat, pos, FogShape);
//    vertexColor = vec4(worldPos, 1);
    texCoord0 = UV0;
    normal = ProjMat * ModelViewMat * vec4(Normal, 0.0);
}

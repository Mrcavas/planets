#include veil:fog
#include veil:light
#include veil:space_helper

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
#define CameraPos VeilCamera.CameraPosition
#define PI 3.14159265358979323846

#define adjustConst 0.642092615934330703001
//#define adjustCoord(C) C = adjustConst*tan(C)
#define adjustCoord(C) C = C
#define adjustPos(P) adjustCoord(P.x);adjustCoord(P.z);

bool insideRegion(vec3 pos) {
    return RADP > pos.x && pos.x > -RADN && RADP > pos.z && pos.z > -RADN;
}

vec4 eulerQuat(float roll, float pitch, float yaw) { // roll (x), pitch (y), yaw (z)
    float cr = cos(roll * 0.5);
    float sr = sin(roll * 0.5);
    float cp = cos(pitch * 0.5);
    float sp = sin(pitch * 0.5);
    float cy = cos(yaw * 0.5);
    float sy = sin(yaw * 0.5);

    return vec4(
    sr * cp * cy - cr * sp * sy,
    cr * sp * cy + sr * cp * sy,
    cr * cp * sy - sr * sp * cy,
    cr * cp * cy + sr * sp * sy
    );
}

vec4 axisAngleQuat(vec3 axis, float angle) {
    float s = sin(angle/2);
    return vec4(axis.x * s,
    axis.y * s,
    axis.z * s,
    cos(angle/2)
    );
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
    vec3 centerOffset = vec3(0, RAD, 0);

    if (insideRegion(worldPos)) {
        vec3 centeredPos = (worldPos + centerOffset) / RAD;
        float y = centeredPos.y;
        adjustPos(centeredPos);
        vec3 adjustedYPos = centeredPos * vec3(y, 1, y);
        vec3 adjustedAxesPos = vec3(adjustedYPos.x, adjustedYPos.y, adjustedYPos.z);

        pos = y * normalize(adjustedAxesPos);
        pos = pos * RAD - centerOffset - CameraPos;
    } else if (insideRegion(worldPos + leftOffset)) {
        vec3 centeredPos = (worldPos + leftOffset + centerOffset) / RAD;
        float y = centeredPos.y;
        adjustPos(centeredPos);
        vec3 adjustedYPos = centeredPos * vec3(y, 1, y);
        vec3 adjustedAxesPos = vec3(adjustedYPos.y, -adjustedYPos.x, adjustedYPos.z);

        pos = y * normalize(adjustedAxesPos);
        pos = pos * RAD - centerOffset - CameraPos;
    } else if (insideRegion(worldPos + rightOffset)) {
        vec3 centeredPos = (worldPos + rightOffset + centerOffset) / RAD;
        float y = centeredPos.y;
        adjustPos(centeredPos);
        vec3 adjustedYPos = centeredPos * vec3(y, 1, y);
        vec3 adjustedAxesPos = vec3(-adjustedYPos.y, adjustedYPos.x, adjustedYPos.z);

        pos = y * normalize(adjustedAxesPos);
        pos = pos * RAD - centerOffset - CameraPos;
    } else if (insideRegion(worldPos + backOffset)) {
        vec3 centeredPos = (worldPos + backOffset + centerOffset) / RAD;
        float y = centeredPos.y;
        adjustPos(centeredPos);
        vec3 adjustedYPos = centeredPos * vec3(y, 1, y);
        vec3 adjustedAxesPos = vec3(-adjustedYPos.x, -adjustedYPos.y, adjustedYPos.z);

        pos = y * normalize(adjustedAxesPos);
        pos = pos * RAD - centerOffset - CameraPos;
    } else if (insideRegion(worldPos + topOffset)) {
        vec3 centeredPos = (worldPos + topOffset + centerOffset) / RAD;
        float y = centeredPos.y;
        adjustPos(centeredPos);
        vec3 adjustedYPos = centeredPos * vec3(y, 1, y);
        vec3 adjustedAxesPos = vec3(adjustedYPos.x, -adjustedYPos.z, adjustedYPos.y);

        pos = y * normalize(adjustedAxesPos);
        pos = pos * RAD - centerOffset - CameraPos;
    } else if (insideRegion(worldPos + bottomOffset)) {
        vec3 centeredPos = (worldPos + bottomOffset + centerOffset) / RAD;
        float y = centeredPos.y;
        adjustPos(centeredPos);
        vec3 adjustedYPos = centeredPos * vec3(y, 1, y);
        vec3 adjustedAxesPos = vec3(adjustedYPos.x, adjustedYPos.z, -adjustedYPos.y);

        pos = y * normalize(adjustedAxesPos);
        pos = pos * RAD - centerOffset - CameraPos;
    } else return;

    if (insideRegion(CameraPos)) {
        vec3 centeredPos = (CameraPos + centerOffset) / RAD;
        float y = centeredPos.y;
        adjustPos(centeredPos);
        vec3 adjustedYPos = centeredPos * vec3(y, 1, y);
        vec3 adjustedAxesPos = vec3(adjustedYPos.x, adjustedYPos.y, adjustedYPos.z);

        vec3 onSpherePos = y * normalize(adjustedAxesPos);
                pos += (centeredPos - onSpherePos) * RAD;

//        vertexColor = vec4(centeredPos - onSpherePos, 1);
        float zRot = atan(adjustedAxesPos.x, adjustedAxesPos.y);
        float xRot = -atan(adjustedAxesPos.z, adjustedAxesPos.y);

//        vertexColor = vec4((xRot + 1) / 2, (zRot + 1) / 2, 0, 1);
        vec4 q = eulerQuat(xRot, 0, zRot);
        pos += 2.0*cross(q.xyz, cross(q.xyz, pos) + q.w * pos);
    }
/*else if (insideRegion(CameraPos + leftOffset)) pos.x += SEP;
    else if (insideRegion(CameraPos + rightOffset)) pos.x -= SEP;
    else if (insideRegion(CameraPos + backOffset)) pos.x -= 2 * SEP;
    else if (insideRegion(CameraPos + topOffset)) pos.z += SEP;
    else if (insideRegion(CameraPos + bottomOffset)) pos.z -= SEP;*/



    #else
    #endif
    vertexColor = Color * minecraft_sample_lightmap(Sampler2, UV2);

    gl_Position = ProjMat * ModelViewMat * vec4(pos, 1.0);
    vertexDistance = fog_distance(ModelViewMat, pos, FogShape);
    //    vertexColor = vec4(worldPos, 1);
    texCoord0 = UV0;
    normal = ProjMat * ModelViewMat * vec4(Normal, 0.0);
}

/*

        //        float y = centeredPos.y;
        adjustPos(centeredPos);
        //        vec3 adjustedYPos = centeredPos * vec3(1, 1, 1);
        //        vec3 adjustedAxesPos = vec3(adjustedYPos.x, adjustedYPos.y, adjustedYPos.z);

        float alpha = atan(centeredPos.x, 1);// around Z
        float beta = -atan(centeredPos.z, 1);// around X
        //        float gamma = atan(centeredPos.x, centeredPos.z); // around Y?

        //        pos = vec3(pos.x, pos.y * cos(beta) - pos.z * sin(beta), pos.y * sin(beta) + pos.z * cos(beta));
        //        pos = vec3(pos.x * cos(alpha) - pos.y * sin(alpha), pos.x * sin(alpha) + pos.y * cos(alpha), pos.z);

//        vec4 q = axisAngleQuat(cross(vec3(0, 1, 0), normalize(vec3(-alpha, 0, beta))), 0.174);
        vec4 q = eulerQuat(beta, 0, alpha);
        vec3 temp = cross(q.xyz, pos) + q.w * pos;
        pos = pos + 2.0*cross(q.xyz, temp);
//                vertexColor = vec4(-alpha, -beta, 0, 1);

        pos.x += CameraPos.x / RAD;
        pos.z += CameraPos.z / RAD;
*/
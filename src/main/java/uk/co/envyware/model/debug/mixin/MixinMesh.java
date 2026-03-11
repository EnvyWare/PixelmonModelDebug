package uk.co.envyware.model.debug.mixin;

import com.pixelmonmod.pixelmon.client.render.entity.renderers.assimp.Face;
import com.pixelmonmod.pixelmon.client.render.entity.renderers.assimp.Mesh;
import com.pixelmonmod.pixelmon.client.render.entity.renderers.assimp.Vertex;
import com.pixelmonmod.pixelmon.client.render.entity.renderers.assimp.animation.BoneWeight;
import com.pixelmonmod.pixelmon.client.render.entity.renderers.assimp.render.BufferHelper;
import com.pixelmonmod.pixelmon.client.render.entity.renderers.assimp.render.RenderedObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import uk.co.envyware.model.debug.ModelDebug;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

@Mixin(value = Mesh.class, remap = false)
public abstract class MixinMesh {

    @Shadow
    protected static int bindBufferData(int index, int size, float[] bufferData) {
        return 0;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static void createVAO(RenderedObject renderedObject, List<Vertex> vertices, List<Face> faces, BoneWeight[][] boneWeights) {
        var vertexCount = vertices.size();
        float[] vertexPositions = new float[vertexCount * 3];
        float[] vertexTextureCoordinates = new float[vertexCount * 2];
        float[] vertexNormals = new float[vertexCount * 3];
        int[] vertexBoneIndices = new int[vertexCount * 4];
        float[] vertexBoneWeights = new float[vertexCount * 4];

        for (int i = 0; i < vertices.size(); i++) {
            var vertex = vertices.get(i);
            vertexPositions[i * 3] = vertex.pos().x();
            vertexPositions[i * 3 + 1] = vertex.pos().y();
            vertexPositions[i * 3 + 2] = vertex.pos().z();

            vertexTextureCoordinates[i * 2] = vertex.textureCoordinates().x();
            vertexTextureCoordinates[i * 2 + 1] = vertex.textureCoordinates().y();

            vertexNormals[i * 3] = vertex.normal().x();
            vertexNormals[i * 3 + 1] = vertex.normal().y();
            vertexNormals[i * 3 + 2] = vertex.normal().z();

            if (boneWeights.length <= i || boneWeights[i].length == 0) {
                vertexBoneIndices[i * 4] = 0;
                vertexBoneIndices[i * 4 + 1] = 0;
                vertexBoneIndices[i * 4 + 2] = 0;
                vertexBoneIndices[i * 4 + 3] = 0;

                vertexBoneWeights[i * 4] = 1;
                vertexBoneWeights[i * 4 + 1] = 0;
                vertexBoneWeights[i * 4 + 2] = 0;
                vertexBoneWeights[i * 4 + 3] = 0;
                continue;
            }

            var vertexBoneWeight = boneWeights[i];

            var boneIndex0 = vertexBoneWeight.length > 0 && vertexBoneWeight[0] != null ? vertexBoneWeight[0].boneId() : 0;
            var boneIndex1 = vertexBoneWeight.length > 1 && vertexBoneWeight[1] != null ? vertexBoneWeight[1].boneId() : 0;
            var boneIndex2 = vertexBoneWeight.length > 2 && vertexBoneWeight[2] != null ? vertexBoneWeight[2].boneId() : 0;
            var boneIndex3 = vertexBoneWeight.length > 3 && vertexBoneWeight[3] != null ? vertexBoneWeight[3].boneId() : 0;

            var weight0 = vertexBoneWeight.length > 0 && vertexBoneWeight[0] != null ? vertexBoneWeight[0].weight() : 0;
            var weight1 = vertexBoneWeight.length > 1 && vertexBoneWeight[1] != null  ? vertexBoneWeight[1].weight() : 0;
            var weight2 = vertexBoneWeight.length > 2 && vertexBoneWeight[2] != null  ? vertexBoneWeight[2].weight() : 0;
            var weight3 = vertexBoneWeight.length > 3 && vertexBoneWeight[3] != null  ? vertexBoneWeight[3].weight() : 0;

            vertexBoneIndices[i * 4] = boneIndex0;
            vertexBoneIndices[i * 4 + 1] = boneIndex1;
            vertexBoneIndices[i * 4 + 2] = boneIndex2;
            vertexBoneIndices[i * 4 + 3] = boneIndex3;

            vertexBoneWeights[i * 4] = weight0;
            vertexBoneWeights[i * 4 + 1] = weight1;
            vertexBoneWeights[i * 4 + 2] = weight2;
            vertexBoneWeights[i * 4 + 3] = weight3;
        }

        var index = 0;
        var vertexIndices = new int[faces.size() * 3];

        for (var face : faces) {
            for (var vertexIndex : face.vertices()) {
                vertexIndices[index++] = vertexIndex;
            }
        }

        var indiceBuffer = glGenBuffers();
        List<Integer> vertexBuffers = new ArrayList<>();

        glBindVertexArray(renderedObject.vaoId());

        vertexBuffers.add(bindBufferData(0, 3, vertexPositions));
        glEnableVertexAttribArray(0);

        vertexBuffers.add(bindBufferData(1, 2, vertexTextureCoordinates));
        glEnableVertexAttribArray(1);

        vertexBuffers.add(bindBufferData(2, 3, vertexNormals));
        glEnableVertexAttribArray(2);

        vertexBuffers.add(bindBufferIntData(3, 4, vertexBoneIndices));
        glEnableVertexAttribArray(3);

        vertexBuffers.add(bindBufferData(4, 4, vertexBoneWeights));
        glEnableVertexAttribArray(4);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indiceBuffer);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferHelper.fromArray(vertexIndices), GL_STATIC_DRAW);

        glBindVertexArray(0);
    }

    private static int bindBufferIntData(int index, int size, int[] bufferData) {
        var buffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, buffer);
        glBufferData(GL_ARRAY_BUFFER, BufferHelper.fromArray(bufferData), GL_STATIC_DRAW);
        GL30.glVertexAttribIPointer(index, size, GL11.GL_UNSIGNED_INT, 0, 0L);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return buffer;
    }
}

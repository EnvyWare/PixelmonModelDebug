package uk.co.envyware.model.debug.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.MeshData;
import com.pixelmonmod.pixelmon.client.render.entity.renderers.assimp.Material;
import com.pixelmonmod.pixelmon.client.render.entity.renderers.assimp.Mesh;
import com.pixelmonmod.pixelmon.client.render.entity.renderers.assimp.Scene;
import com.pixelmonmod.pixelmon.client.render.entity.renderers.assimp.render.AssimpRenderType;
import com.pixelmonmod.pixelmon.client.render.entity.renderers.entity.RenderContext;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.AbstractTexture;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.spongepowered.asm.mixin.*;
import uk.co.envyware.model.debug.ModelDebug;
import uk.co.envyware.model.debug.ModelDebugShaders;

import java.util.function.Supplier;

@Mixin(value = AssimpRenderType.class, remap = false)
public abstract class MixinAssimpRenderType {

    @Shadow
    protected abstract void _drawWithShader(Matrix4f projectionMatrix, ShaderInstance shader);

    @Mutable
    @Shadow
    @Final
    private Supplier<ShaderInstance> shader;

    @Shadow
    @Final
    private Scene scene;

    @Shadow
    protected abstract void setupBoneUniforms(int meshId);

    @Shadow
    protected abstract AbstractTexture getTexture(Material material);

    @Shadow
    @Final
    private RenderContext context;

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public void draw(MeshData meshData) {
        meshData.close();
        this.shader = () -> ModelDebugShaders.rendertypeEntitySmoothCutoutShader;

        if (!RenderSystem.isOnRenderThreadOrInit()) {
            RenderSystem.recordRenderCall(() -> _drawWithShader(RenderSystem.getProjectionMatrix(), this.shader.get()));
        } else {
            _drawWithShader(RenderSystem.getProjectionMatrix(), this.shader.get());
        }
    }
}

package uk.co.envyware.model.debug.mixin;

import com.pixelmonmod.pixelmon.api.pokemon.species.palette.locator.Animation;
import com.pixelmonmod.pixelmon.client.render.entity.renderers.assimp.Scene;
import com.pixelmonmod.pixelmon.client.render.entity.renderers.smd.SMDModelLoader;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL30;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import uk.co.envyware.model.debug.ModelDebug;

import java.util.List;

@Mixin(value = SMDModelLoader.class, remap = false)
public class MixinSMDModelLoader {

    @Inject(
            method = "loadScene",
            at = @At("HEAD")
    )
    public void onLoadScene(ResourceLocation target, ResourceLocation backupTexture, List<Animation> animations, CallbackInfoReturnable<Scene> cir) {
        ModelDebug.LOGGER.info("Pixelmon Debug Bone Limit: " + GL30.glGetInteger(GL30.GL_MAX_VERTEX_UNIFORM_COMPONENTS));
    }
}

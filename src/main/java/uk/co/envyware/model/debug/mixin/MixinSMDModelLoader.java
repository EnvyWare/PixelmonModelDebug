package uk.co.envyware.model.debug.mixin;

import com.pixelmonmod.pixelmon.api.pokemon.species.palette.locator.Animation;
import com.pixelmonmod.pixelmon.client.render.entity.renderers.assimp.Scene;
import com.pixelmonmod.pixelmon.client.render.entity.renderers.smd.SMDModelLoader;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
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
        ModelDebug.LOGGER.info("GL_VENDOR   = {}", GL11.glGetString(GL11.GL_VENDOR));
        ModelDebug.LOGGER.info("GL_RENDERER = {}", GL11.glGetString(GL11.GL_RENDERER));
        ModelDebug.LOGGER.info("GL_VERSION  = {}", GL11.glGetString(GL11.GL_VERSION));

        ModelDebug.LOGGER.info("GL_MAX_VERTEX_UNIFORM_COMPONENTS = {}", GL11.glGetInteger(GL20.GL_MAX_VERTEX_UNIFORM_COMPONENTS));
        ModelDebug.LOGGER.info("GL_MAX_UNIFORM_BUFFER_BINDINGS   = {}", GL11.glGetInteger(GL31.GL_MAX_UNIFORM_BUFFER_BINDINGS));
        ModelDebug.LOGGER.info("GL_MAX_VERTEX_UNIFORM_BLOCKS     = {}", GL11.glGetInteger(GL31.GL_MAX_VERTEX_UNIFORM_BLOCKS));
    }
}

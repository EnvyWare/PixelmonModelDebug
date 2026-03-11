package uk.co.envyware.model.debug;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.util.ThrowableSupplier;
import com.pixelmonmod.pixelmon.api.util.helpers.ResourceLocationHelper;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import java.util.function.Consumer;

@EventBusSubscriber(modid = ModelDebug.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ModelDebugShaders {

    public static final ResourceLocation CUSTOM_SHADER = ResourceLocationHelper.of(ModelDebug.MOD_ID, "rendertype_entity_cutout_no_cull");

    public static ShaderInstance rendertypeEntitySmoothCutoutShader;

    @SubscribeEvent
    public static void onRegisterShaders(RegisterShadersEvent event) {
        createShaderSafely(event,
                () -> new ShaderInstance(event.getResourceProvider(), CUSTOM_SHADER, DefaultVertexFormat.NEW_ENTITY),
                shader -> rendertypeEntitySmoothCutoutShader = shader
        );
    }


    private static void createShaderSafely(RegisterShadersEvent event, ThrowableSupplier<ShaderInstance> supplier, Consumer<ShaderInstance> setter) {
        try {
            ShaderInstance shader = supplier.get();
            event.registerShader(shader, setter);
        } catch (Exception e) {
            Pixelmon.LOGGER.error("Failed to create shader!", e);
        }

    }
}

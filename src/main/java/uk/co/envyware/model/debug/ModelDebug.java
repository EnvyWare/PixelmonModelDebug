package uk.co.envyware.model.debug;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ModelDebug.MOD_ID)
public class ModelDebug {

    public static final String MOD_ID = "modeldebug";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public ModelDebug(IEventBus bus) {
    }
}

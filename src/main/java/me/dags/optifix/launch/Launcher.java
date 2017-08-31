package me.dags.optifix.launch;

import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

/**
 * @author dags <dags@dags.me>
 */
public class Launcher {

    public static final Logger logger = LogManager.getLogger("OptiFix");

    static void launch() {
        logger.info("Attempting to launch OptiFix mod");
        logger.info("Initializing mixin bootstrap");
        MixinBootstrap.init();

        String config = "mixin.optifix.json";
        logger.info("Adding mixin configuration: {}", config);
        Mixins.addConfiguration(config);

        if (fmlIsPresent()) {
            String context = "searge";
            logger.info("Setting obfuscation context: {}", context);
            MixinEnvironment.getDefaultEnvironment().setObfuscationContext(context);
        }
    }

    private static boolean fmlIsPresent() {
        for (IClassTransformer transformer : net.minecraft.launchwrapper.Launch.classLoader.getTransformers()) {
            if (transformer.getClass().getName().contains("fml")) {
                logger.info("Detected Forge");
                return true;
            }
        }
        return false;
    }
}

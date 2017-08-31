package me.dags.optifix.mixin;

import me.dags.optifix.launch.Launcher;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author dags <dags@dags.me>
 */
@Mixin(ChunkCache.class)
public abstract class MixinChunkCache {

    @Shadow
    protected int chunkX;
    @Shadow
    protected int chunkZ;
    @Shadow
    protected Chunk[][] chunkArray;
    @Shadow
    protected World worldObj;

    @Inject(method = "getBiome", at = @At("HEAD"), cancellable = true)
    public void onGetBiome(BlockPos pos, CallbackInfoReturnable<Biome> info) {
        int i = (pos.getX() >> 4) - this.chunkX;
        int j = (pos.getZ() >> 4) - this.chunkZ;

        if (i < 0 || i >= chunkArray.length) {
            Launcher.logger.warn("Prevented Optifine/BetterFoliage crash: i={}, chunks.length={}", i, chunkArray.length);
            info.setReturnValue(Biomes.PLAINS);
            return;
        }

        if (j < 0 || j >= chunkArray[i].length) {
            Launcher.logger.warn("Prevented Optifine/BetterFoliage crash: j={}, chunks[i].length={}", j, chunkArray[i].length);
            info.setReturnValue(Biomes.PLAINS);
        }
    }
}

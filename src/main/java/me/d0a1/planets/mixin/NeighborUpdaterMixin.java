package me.d0a1.planets.mixin;

import kotlin.Unit;
import me.d0a1.planets.Planets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.block.NeighborUpdater;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NeighborUpdater.class)
public interface NeighborUpdaterMixin {
	@Shadow
	static void tryNeighborUpdate(World world, BlockState state, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
	}

	@Inject(at = @At("HEAD"), method = "tryNeighborUpdate", cancellable = true)
	private static void tryNeighborUpdate(World world, BlockState state, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, CallbackInfo ci) {
		Planets.Companion.doIfBlockLinked(pos, (to) -> {
			tryNeighborUpdate(world, state, to, sourceBlock, sourcePos, notify);
			ci.cancel();
			return Unit.INSTANCE;
		});
	}
}

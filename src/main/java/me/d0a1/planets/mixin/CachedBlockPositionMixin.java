package me.d0a1.planets.mixin;

import kotlin.Unit;
import me.d0a1.planets.Planets;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.block.pattern.CachedBlockPosition.class)
public class CachedBlockPositionMixin {
	@Mutable
	@Shadow
	@Final
	private BlockPos pos;

	@Inject(at = @At("TAIL"), method = "<init>")
	private void init(WorldView world, BlockPos pos, boolean forceLoad, CallbackInfo ci) {
		Planets.Companion.doIfBlockLinked(pos, (to) -> {
			this.pos = to;
			return Unit.INSTANCE;
		});
	}
}

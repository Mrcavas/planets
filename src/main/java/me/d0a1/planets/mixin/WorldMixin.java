package me.d0a1.planets.mixin;

import kotlin.Unit;
import me.d0a1.planets.Planets;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class WorldMixin {
	@Shadow
	public abstract boolean setBlockState(BlockPos pos, BlockState state, int flags, int maxUpdateDepth);

	@Shadow
	public abstract BlockState getBlockState(BlockPos pos);

	@Inject(at = @At("HEAD"), method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z", cancellable = true)
	public void setBlockState(BlockPos pos, BlockState state, int flags, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir) {
		Planets.Companion.doIfBlockLinked(pos, (to) -> {
			cir.setReturnValue(this.setBlockState(to, state, flags, maxUpdateDepth));
			return Unit.INSTANCE;
		});
	}

	@Inject(at = @At("HEAD"), method = "getBlockState", cancellable = true)
	public void getBlockState(BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
		Planets.Companion.doIfBlockLinked(pos, (to) -> {
			cir.setReturnValue(this.getBlockState(to));
			return Unit.INSTANCE;
		});
	}
}
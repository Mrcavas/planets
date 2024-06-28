package me.d0a1.planets.mixin;

import kotlin.Unit;
import me.d0a1.planets.Planets;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkRendererRegion.class)
public abstract class ChunkRendererRegionMixin {
	@Shadow
	public abstract BlockState getBlockState(BlockPos pos);

	@Shadow
	public abstract @Nullable BlockEntity getBlockEntity(BlockPos pos);

	@Inject(at = @At("HEAD"), method = "getBlockState", cancellable = true)
	public void getBlockState(BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
		Planets.Companion.doIfBlockLinked(pos, (to) -> {
			cir.setReturnValue(this.getBlockState(to));
			return Unit.INSTANCE;
		});
	}

	@Inject(at = @At("HEAD"), method = "getBlockEntity", cancellable = true)
	public void getBlockEntity(BlockPos pos, CallbackInfoReturnable<BlockEntity> cir) {
		Planets.Companion.doIfBlockLinked(pos, (to) -> {
			cir.setReturnValue(this.getBlockEntity(to));
			return Unit.INSTANCE;
		});
	}
}

package me.d0a1.planets.mixin;

import kotlin.Unit;
import me.d0a1.planets.Planets;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntity.class)
public class BlockEntityMixin {
	@Shadow
	@Final
	protected BlockPos pos;

	@Inject(at = @At("HEAD"), method = "getPos", cancellable = true)
	public void getPos(CallbackInfoReturnable<BlockPos> cir) {
		Planets.Companion.doIfBlockLinked(pos, (to) -> {
			cir.setReturnValue(to);
			return Unit.INSTANCE;
		});
	}
}

package me.d0a1.planets.mixin;

import kotlin.Unit;
import me.d0a1.planets.Planets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
//	@Inject(at = @At("HEAD"), method = "tickMovement", cancellable = true)
//	public void tickMovement(CallbackInfo ci) {
//		Entity self = (Entity) (Object) this;
//		Vec3d pos = self.getPos();
//		Planets.Companion.doIfPositionLinked(pos, (region) -> {
////			self.setPos(pos.x + region.getDiff().getX(), pos.y + region.getDiff().getY(), pos.z + region.getDiff().getZ());
//			Planets.Companion.getLogger().info("tickmoved into linked area");
////			ci.cancel();
//			return Unit.INSTANCE;
//		});
//	}
}

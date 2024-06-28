package me.d0a1.planets.mixin;

import kotlin.Unit;
import me.d0a1.planets.Planets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

	@Shadow
	private Vec3d pos;

	@Shadow
	public abstract void setPos(double x, double y, double z);

	@Inject(at = @At("HEAD"), method = "setPos", cancellable = true)
	public void setPos(double x, double y, double z, CallbackInfo ci) {
		Vec3d pos = new Vec3d(x, y, z);
		Planets.Companion.doIfPositionLinked(pos, (region) -> {
			setPos(pos.x + region.getDiff().getX(), pos.y + region.getDiff().getY(), pos.z + region.getDiff().getZ());
			Planets.Companion.getLogger().info("setpos into linked area");
			ci.cancel();
			return Unit.INSTANCE;
		});
	}

	@Inject(at = @At("HEAD"), method = "move")
	public void move(MovementType movementType, Vec3d movement, CallbackInfo ci) {
		Vec3d to = pos.add(movement);
		Planets.Companion.doIfPositionLinked(to, (region) -> {
			Vec3d newPos = pos.add(Vec3d.of(region.getDiff()));
			setPos(newPos.x, newPos.y, newPos.z);
			return Unit.INSTANCE;
		});
	}
}

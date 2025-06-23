package me.d0a1.planets.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
	// TODO: don't allow cheats, make it actually check if player was in a linked region
	@Redirect(method = "onPlayerMove", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V"))
	public void itsOkBro(Logger instance, String s, Object o, @Local(ordinal = 2) LocalBooleanRef isMovingWrongly) {
		isMovingWrongly.set(false);
	}

	@Redirect(method = "onPlayerMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;isHost()Z"))
	public boolean itsOkBro(ServerPlayNetworkHandler instance) {
		return true;
	}

	@Redirect(method = "onVehicleMove", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;[Ljava/lang/Object;)V", ordinal = 1))
	public void itsOkBroVehicle(Logger instance, String s, Object[] objects, @Local(ordinal = 2) LocalBooleanRef isMovingWrongly) {
		isMovingWrongly.set(false);
	}

	@Redirect(method = "onVehicleMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;isHost()Z"))
	public boolean itsOkBroVehicle(ServerPlayNetworkHandler instance) {
		return true;
	}
}

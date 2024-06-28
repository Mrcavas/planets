package me.d0a1.planets.mixin;

import me.d0a1.planets.PlanetsRenderLayers;
import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderLayer.class)
public class RenderLayerMixin {
	@Inject(method = "getSolid", at = @At("HEAD"), cancellable = true)
	private static void replaceSolid(CallbackInfoReturnable<RenderLayer> cir) {
		cir.setReturnValue(PlanetsRenderLayers.sphere());
	}
}

package me.d0a1.planets

import foundry.veil.api.client.render.VeilRenderSystem
import foundry.veil.api.client.render.VeilRenderer
import foundry.veil.fabric.event.FabricVeilRendererAvailableEvent
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW


class PlanetsClient : ClientModInitializer {
	companion object {
		var sphereEnabled = true
		lateinit var toggleShadersKeyBinding: KeyBinding
	}

	override fun onInitializeClient() {
		toggleShadersKeyBinding = KeyBindingHelper.registerKeyBinding(
			KeyBinding(
				"key.planets.toggleShaders", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_P, KeyBinding.MISC_CATEGORY
			)
		)

		ClientTickEvents.END_CLIENT_TICK.register {
			while (toggleShadersKeyBinding.wasPressed()) {
				sphereEnabled = !sphereEnabled
				if (sphereEnabled) VeilRenderSystem.renderer().shaderDefinitions.set("SPHERE_ENABLED")
				else VeilRenderSystem.renderer().shaderDefinitions.remove("SPHERE_ENABLED")
			}
		}

		FabricVeilRendererAvailableEvent.EVENT.register { renderer: VeilRenderer ->
			renderer.shaderDefinitions.set("RAD", Planets.settings.radius.toString())
			renderer.shaderDefinitions.set("SEP", Planets.settings.separation.toString())
		}
	}
}

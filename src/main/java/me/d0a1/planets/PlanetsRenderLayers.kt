package me.d0a1.planets

import foundry.veil.api.client.render.VeilRenderBridge
import me.d0a1.planets.Planets.Companion.path
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormat.DrawMode
import net.minecraft.client.render.VertexFormats

class PlanetsRenderLayers private constructor(
	string: String,
	vertexFormat: VertexFormat,
	mode: DrawMode,
	i: Int,
	bl: Boolean,
	bl2: Boolean,
	runnable: Runnable,
	runnable2: Runnable,
) : RenderLayer(string, vertexFormat, mode, i, bl, bl2, runnable, runnable2) {
	companion object {
		private val SPHERE: ShaderProgram = VeilRenderBridge.shaderState(path("sphere/rendertype_solid"))
		private val SOLID: RenderLayer = of(
			"sphere_solid",
			VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL,
			DrawMode.QUADS,
			0x200000,
			true,
			false,
			MultiPhaseParameters
					.builder()
					.lightmap(ENABLE_LIGHTMAP)
					.program(SPHERE)
					.texture(MIPMAP_BLOCK_ATLAS_TEXTURE)
					.build(true)
		)

		@JvmStatic
		fun sphere() = SOLID
	}
}

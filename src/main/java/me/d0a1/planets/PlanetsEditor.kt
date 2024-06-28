package me.d0a1.planets

import foundry.veil.api.client.editor.SingleWindowEditor
import foundry.veil.api.client.render.VeilRenderSystem
import imgui.ImGui
import imgui.type.ImBoolean
import net.minecraft.text.Text

class PlanetsEditor : SingleWindowEditor() {
	override fun renderComponents() {
		val definitions = VeilRenderSystem.renderer().shaderDefinitions

		if (ImGui.beginTabBar("Examples")) {
			for (value in Example.entries) {
				if (ImGui.beginTabItem(value.title + " Example")) {
					if (value == Example.TESSELLATION) {
						ImGui.checkbox("Use Tessellation", useTessellation)
						ImGui.sameLine()
						ImGui.checkbox("Wireframe", tessellationWireframe)
						if (ImGui.dragInt("Min Tessellation Level", minTessLevel, 1f, 1f, Int.MAX_VALUE.toFloat())) {
							definitions.define("MIN_TESS_LEVEL", minTessLevel[0].toString())
						}
						if (ImGui.dragInt(
								"Max Tessellation Level", maxTessLevel, 1f, minTessLevel[0].toFloat(), Int.MAX_VALUE.toFloat()
							)) {
							definitions.define("MAX_TESS_LEVEL", maxTessLevel[0].toString())
						}
						if (ImGui.dragInt("Min Distance", minDistance, 1f, 0f, Int.MAX_VALUE.toFloat())) {
							definitions.define("MIN_DISTANCE", minDistance[0].toString())
						}
						if (ImGui.dragInt("Max Distance", maxDistance, 1f, minDistance[0].toFloat(), Int.MAX_VALUE.toFloat())) {
							definitions.define("MAX_DISTANCE", maxDistance[0].toString())
						}
					}
				}
				ImGui.endTabItem()

				if (ImGui.isItemHovered()) {
					ImGui.beginTooltip()
					ImGui.pushTextWrapPos(ImGui.getFontSize() * 35.0f)
					ImGui.textUnformatted(value.tooltip)
					ImGui.popTextWrapPos()
					ImGui.endTooltip()
				}
			}

			ImGui.endTabBar()
		}
	}

	override fun getDisplayName(): Text = Text.literal("Veil Example Mod")

	private enum class Example(val title: String, val tooltip: String?) {
		TESSELLATION("Tessellation", "")
	}

	companion object {
		private val minTessLevel = intArrayOf(4)
		private val maxTessLevel = intArrayOf(64)
		private val minDistance = intArrayOf(1)
		private val maxDistance = intArrayOf(6)
		private val useTessellation = ImBoolean(true)
		private val tessellationWireframe = ImBoolean(false)

		fun useTessellation() = useTessellation.get()
		fun tessellationWireframe() = tessellationWireframe.get()
		fun getMinTessLevel() = minTessLevel[0]
		fun getMaxTessLevel() = maxTessLevel[0]
		fun getMinDistance() = minDistance[0]
		fun getMaxDistance() = maxDistance[0]
	}
}

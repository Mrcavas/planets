package me.d0a1.planets

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents
import net.minecraft.command.argument.BlockPosArgumentType
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Planets : ModInitializer {
	companion object {
		val RAD = 2
		val SEP = 5
		val LIN = (SEP - 1) / 2
		val YB = -2
		val YT = 4

		val Logger: Logger = LoggerFactory.getLogger("Planets")
		val linkedRegions = mutableSetOf<LinkedRegion>(
			LinkedRegion.byDiff(
				BlockPos(SEP, YB, RAD - 1), // 5 -1 1
				BlockPos(SEP + LIN - 1, YT, -RAD), // 6 4 -2
				BlockPos(-SEP, 0, 0) // 0 -1 1
			), // br
			LinkedRegion.byDiff(
				BlockPos(-RAD - 1, YB, RAD - 1), // -3 -1 1
				BlockPos(-RAD - LIN, YT, -RAD), // -4 4 -2
				BlockPos(-SEP, 0, 0) // -8 -1 1
			), // ry
			LinkedRegion.byDiff(
				BlockPos(-RAD - 1 - (RAD * 2 + SEP), YB, RAD - 1), // -12 -1 1
				BlockPos(-RAD - LIN - (RAD * 2 + SEP), YT, -RAD), // -13 4 -2
				BlockPos(-SEP, 0, 0) // -17 -1 1
			), // yg
			LinkedRegion.byDiff(
				BlockPos(-RAD - 1 - 2 * (RAD * 2 + SEP), YB, RAD - 1), // -21 -1 1
				BlockPos(-RAD - LIN - 2 * (RAD * 2 + SEP), YT, -RAD), // -22 4 -2
				BlockPos(SEP * 3 + RAD * 8, 0, 0) // 10 -1 1
			), // gb
			//			LinkedRegion.diffed(
			//				BlockPos(RAD, YB, RAD - 1), //
			//				BlockPos(RAD - 1 + LIN, YT, -RAD), //
			//				BlockPos(SEP, 0, 0)
			//			), //
		)

		fun doIfBlockLinked(pos: BlockPos, cb: (BlockPos) -> Unit) {
			for (region in linkedRegions) if (pos in region) return cb(pos + region.diff)
		}

		fun doIfPositionLinked(pos: Vec3d, cb: (LinkedRegion) -> Unit) {
			for (region in linkedRegions) if (pos in region) return cb(region)
		}

		fun path(path: String) = Identifier("planets", path)
	}

	override fun onInitialize() {
		ServerWorldEvents.UNLOAD.register { _, _ ->
			linkedRegions.clear()
		}

		CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, _, _ ->
			dispatcher.register(
				literal("link").then(
					argument("corner1", BlockPosArgumentType.blockPos()).then(
						argument(
							"corner2", BlockPosArgumentType.blockPos()
						).then(argument("to", BlockPosArgumentType.blockPos()).executes {
							val corner1 = BlockPosArgumentType.getBlockPos(it, "corner1")
							val corner2 = BlockPosArgumentType.getBlockPos(it, "corner2")
							val to = BlockPosArgumentType.getBlockPos(it, "to")
							val diff = to - corner1

							linkedRegions.add(LinkedRegion(corner1, corner2, to))
							for (pos in corner1..corner2) {
								it.source.world.breakBlock(pos, false)
								it.source.world.breakBlock(pos + diff, false)
							}

							it.source.sendFeedback(
								{ Text.literal("Linking every action from region ${corner1.toShortString()} - ${corner2.toShortString()} to region ${(corner1 + diff).toShortString()} - ${(corner2 + diff).toShortString()}") },
								false
							)
							1
						})
					)
				)
			)
		})
	}
}
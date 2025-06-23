package me.d0a1.planets

import com.mojang.brigadier.arguments.IntegerArgumentType
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
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
		val Logger: Logger = LoggerFactory.getLogger("Planets")

		data class RegionSettings(val radius: Int, val separation: Int, val bottomY: Int, val topY: Int)
		var settings = RegionSettings(4, 7, -2, 4)

		private fun makeLinkedRegions(settings: RegionSettings): MutableSet<LinkedRegion> {
			val RAD = settings.radius
			val SEP = settings.separation
			val YT = settings.topY
			val YB = settings.bottomY
			val LIN = (settings.separation - 1) / 2 // distance from islands being linked // 3

			return mutableSetOf(
				// X:
				LinkedRegion(
					BlockPos(RAD - 1 + SEP, YB, RAD - 1), // 10, 3
					BlockPos(RAD - LIN + SEP, YT, -RAD), // 8, -4
					BlockPos(-SEP, 0, 0) //
				), // br
				LinkedRegion(
					BlockPos(-RAD - 1, YB, RAD - 1), //
					BlockPos(-RAD - LIN, YT, -RAD), //
					BlockPos(-SEP, 0, 0) //
				), // ry
				LinkedRegion(
					BlockPos(-RAD - 1 - (RAD * 2 + SEP), YB, RAD - 1), //
					BlockPos(-RAD - LIN - (RAD * 2 + SEP), YT, -RAD), //
					BlockPos(-SEP, 0, 0) //
				), // yg
				LinkedRegion(
					BlockPos(-RAD - 1 - 2 * (RAD * 2 + SEP), YB, RAD - 1), //
					BlockPos(-RAD - LIN - 2 * (RAD * 2 + SEP), YT, -RAD), //
					BlockPos(SEP * 3 + RAD * 8, 0, 0) //
				), // gb
				LinkedRegion(
					BlockPos(3 * RAD - 1 + SEP + LIN, YB, RAD - 1), //
					BlockPos(3 * RAD + SEP, YT, -RAD), //
					BlockPos(-SEP * 3 + -RAD * 8, 0, 0) //
				), // bg
				LinkedRegion(
					BlockPos(RAD - 1 + LIN, YB, RAD - 1), //
					BlockPos(RAD, YT, -RAD), //
					BlockPos(SEP, 0, 0) //
				), // rb
				LinkedRegion(
					BlockPos(RAD - 1 + LIN - (2 * RAD + SEP), YB, RAD - 1), //
					BlockPos(RAD - (2 * RAD + SEP), YT, -RAD), //
					BlockPos(SEP, 0, 0) //
				), // yr
				LinkedRegion(
					BlockPos(RAD - 1 + LIN - 2 * (2 * RAD + SEP), YB, RAD - 1), //
					BlockPos(RAD - 2 * (2 * RAD + SEP), YT, -RAD), //
					BlockPos(SEP, 0, 0) //
				), // gy
				// Z:
				LinkedRegion(
					BlockPos(RAD - 1, YB, 3 * RAD + SEP), //
					BlockPos(-RAD, YT, 3 * RAD - 1 + LIN + SEP), //
					BlockPos(0, 0, -2 * SEP - 6 * RAD) //
				), // wB
				LinkedRegion(
					BlockPos(RAD - 1, YB, RAD), //
					BlockPos(-RAD, YT, RAD - 1 + LIN), //
					BlockPos(0, 0, SEP) //
				), // rw
				LinkedRegion(
					BlockPos(RAD - 1, YB, -RAD - SEP), //
					BlockPos(-RAD, YT, -RAD - 1 + LIN - SEP), //
					BlockPos(0, 0, SEP) //
				), // Br
				LinkedRegion(
					BlockPos(RAD - 1, YB, RAD - 1 + SEP), //
					BlockPos(-RAD, YT, RAD - LIN + SEP), //
					BlockPos(0, 0, -SEP) //
				), // wr
				LinkedRegion(
					BlockPos(RAD - 1, YB, -RAD - 1), //
					BlockPos(-RAD, YT, -RAD - LIN), //
					BlockPos(0, 0, -SEP) //
				), // rB
				LinkedRegion(
					BlockPos(RAD - 1, YB, -3 * RAD - 1 - SEP), //
					BlockPos(-RAD, YT, -3 * RAD - LIN - SEP), //
					BlockPos(0, 0, 2 * SEP + 6 * RAD) //
				), // Bw
			)
		}

		var linkedRegions = makeLinkedRegions(settings)

		fun doIfBlockLinked(pos: BlockPos, cb: (BlockPos) -> Unit) {
			for (region in linkedRegions) if (pos in region) return cb(region.map(pos))
		}

		fun doIfPositionLinked(pos: Vec3d, cb: (Vec3d) -> Unit) {
			for (region in linkedRegions) if (pos in region) return cb(region.map(pos))
		}

		fun path(path: String) = Identifier("planets", path)
	}

	override fun onInitialize() {
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

							linkedRegions.add(LinkedRegion(corner1, corner2, diff))
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
			dispatcher.register(
				literal("setregionsettings").then(
					argument("radius", IntegerArgumentType.integer(2)).then(
						argument("separation", IntegerArgumentType.integer(2)).then(
							argument("ybottom", IntegerArgumentType.integer()).then(
								argument("ytop", IntegerArgumentType.integer()).executes {
									val radius = IntegerArgumentType.getInteger(it, "radius")
									val separation = IntegerArgumentType.getInteger(it, "separation")
									val ybottom = IntegerArgumentType.getInteger(it, "ybottom")
									val ytop = IntegerArgumentType.getInteger(it, "ytop")

									settings = RegionSettings(radius, separation, ybottom, ytop)
									linkedRegions = makeLinkedRegions(settings)


									PlanetsClient.veilRenderer.shaderDefinitions.define("rad", settings.radius.toString())
									PlanetsClient.veilRenderer.shaderDefinitions.define("sep", settings.separation.toString())

									1
								}
							)
						)
					)
				)
			)
//			dispatcher.register(
//				literal("placeplatform").executes {
//					for (pos in )
//				}
//			)
		})
	}
}
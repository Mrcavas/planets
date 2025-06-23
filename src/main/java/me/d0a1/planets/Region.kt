package me.d0a1.planets

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Position
import net.minecraft.util.math.Vec3i

open class Region(corner1: BlockPos, corner2: BlockPos) {
	val corner1: BlockPos
	val corner2: BlockPos

	init {
		// swap coordinates if needed so that x1 <= x2, y1 <= y2, z1 <= z2

		var c1X = corner1.x
		var c1Y = corner1.y
		var c1Z = corner1.z
		var c2X = corner2.x
		var c2Y = corner2.y
		var c2Z = corner2.z
		if (c1X > c2X) c1X = c2X.also { c2X = c1X }
		if (c1Y > c2Y) c1Y = c2Y.also { c2Y = c1Y }
		if (c1Z > c2Z) c1Z = c2Z.also { c2Z = c1Z }

		this.corner1 = BlockPos(c1X, c1Y, c1Z)
		this.corner2 = BlockPos(c2X, c2Y, c2Z)
	}

	operator fun contains(pos: Vec3i) =
		corner1.x <= pos.x && pos.x <= corner2.x
				&& corner1.y <= pos.y && pos.y <= corner2.y
				&& corner1.z <= pos.z && pos.z <= corner2.z


	operator fun contains(pos: Position) =
		corner1.x <= pos.x && pos.x <= (corner2.x + 1)
				&& corner1.y <= pos.y && pos.y <= (corner2.y + 1)
				&& corner1.z <= pos.z && pos.z <= (corner2.z + 1)
}

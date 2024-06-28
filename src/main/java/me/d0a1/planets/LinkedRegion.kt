package me.d0a1.planets

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Position
import net.minecraft.util.math.Vec3i

data class LinkedRegion(val corner1: BlockPos, val corner2: BlockPos, val to: BlockPos) {
	val diff = to - corner1

	companion object {
		fun byDiff(c1: BlockPos, c2: BlockPos, diff: BlockPos) = LinkedRegion(c1, c2, c1 + diff)
	}

	operator fun contains(pos: Vec3i) =
		(if (corner1.x <= corner2.x) corner1.x <= pos.x && pos.x <= corner2.x else corner1.x >= pos.x && pos.x >= corner2.x) && (if (corner1.y <= corner2.y) corner1.y <= pos.y && pos.y <= corner2.y else corner1.y >= pos.y && pos.y >= corner2.y) && (if (corner1.z <= corner2.z) corner1.z <= pos.z && pos.z <= corner2.z else corner1.z >= pos.z && pos.z >= corner2.z)

	operator fun contains(pos: Position) =
		(if (corner1.x <= corner2.x) corner1.x <= pos.x && pos.x < (corner2.x + 1) else (corner1.x + 1) > pos.x && pos.x >= corner2.x) && (if (corner1.y <= corner2.y) corner1.y <= pos.y && pos.y < (corner2.y + 1) else (corner1.y + 1) > pos.y && pos.y >= corner2.y) && (if (corner1.z <= corner2.z) corner1.z <= pos.z && pos.z < (corner2.z + 1) else (corner1.z + 1) > pos.z && pos.z >= corner2.z)
}
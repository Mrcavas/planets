package me.d0a1.planets

import net.minecraft.client.MinecraftClient
import net.minecraft.util.math.*

class LinkedRegion(corner1: BlockPos, corner2: BlockPos, val diff: BlockPos, val rotation: Int = 0) : Region(corner1, corner2) {
	fun map(pos: BlockPos): BlockPos {
		return pos + diff
	}

	fun map(pos: Vec3d): Vec3d {
		return pos + Vec3d.of(diff)
	}
}
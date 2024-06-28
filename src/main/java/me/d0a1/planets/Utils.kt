package me.d0a1.planets

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i


operator fun BlockPos.plus(to: BlockPos): BlockPos = this.add(to)
operator fun BlockPos.minus(to: BlockPos): BlockPos = this.subtract(to)
operator fun BlockPos.times(to: Int): BlockPos = this.multiply(to)
operator fun BlockPos.rangeTo(to: BlockPos) = iterator {
	val xRange = if (this@rangeTo.x <= to.x) this@rangeTo.x..to.x else to.x..this@rangeTo.x
	val yRange = if (this@rangeTo.y <= to.y) this@rangeTo.y..to.y else to.y..this@rangeTo.y
	val zRange = if (this@rangeTo.z <= to.z) this@rangeTo.z..to.z else to.z..this@rangeTo.z
	for (x in xRange) for (y in yRange) for (z in zRange) yield(BlockPos(x, y, z))
}


operator fun Vec3i.plus(to: Vec3i): Vec3i = this.add(to)
operator fun Vec3i.minus(to: Vec3i): Vec3i = this.subtract(to)
operator fun Vec3i.div(to: Int): Vec3i = this * (1 / to)
operator fun Vec3i.times(to: Int): Vec3i = this.multiply(to)
operator fun Vec3i.rangeTo(to: Vec3i) = iterator {
	val xRange = if (this@rangeTo.x <= to.x) this@rangeTo.x..to.x else to.x..this@rangeTo.x
	val yRange = if (this@rangeTo.y <= to.y) this@rangeTo.y..to.y else to.y..this@rangeTo.y
	val zRange = if (this@rangeTo.z <= to.z) this@rangeTo.z..to.z else to.z..this@rangeTo.z
	for (x in xRange) for (y in yRange) for (z in zRange) yield(Vec3i(x, y, z))
}


operator fun Vec3d.plus(to: Vec3d): Vec3d = this.add(to)
operator fun Vec3d.minus(to: Vec3d): Vec3d = this.subtract(to)
operator fun Vec3d.div(to: Double): Vec3d = this * (1 / to)
operator fun Vec3d.times(to: Double): Vec3d = this.multiply(to)

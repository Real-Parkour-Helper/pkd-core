package org.realparkourhelper.core.rooms

data class Checkpoint(
    val x: Int,
    val y: Int,
    val z: Int
)

data class RoomMeta(
    val name: String,
    val width: Int, // X
    val height: Int,
    val depth: Int, // Z
    val checkpoints: List<Checkpoint>
)

data class BlockStructure(
    val palette: MutableMap<String, Int>,  // "0" to "minecraft:stone"
    val blocks: MutableList<BlockEntry>
)

data class BlockEntry(
    val x: Int,
    val y: Int,
    val z: Int,
    val id: Int,
    val meta: Int = 0
)
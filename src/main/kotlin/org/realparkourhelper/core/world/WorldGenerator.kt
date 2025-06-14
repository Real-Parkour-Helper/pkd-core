package org.realparkourhelper.core.world

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.ArmorStand
import org.realparkourhelper.core.rooms.BlockStructure
import org.realparkourhelper.core.rooms.RoomLoader
import org.realparkourhelper.core.rooms.RoomMeta

class WorldGenerator(private val world: World, private val roomList: List<String>) {

    /**
     * Generates the world with the provided rooms.
     * This assumes the world has been unloaded by bukkit
     * and can be written over.
     *
     * Returns a nested list of all checkpoint locations (including the start location and end plate).
     * Each sub-list contains the checkpoint locations for a single room.
     */
    fun generateWorld(): List<List<Location>> {
        val baseY = 65
        var currentZ = 0
        var currentCheckpoint = 1
        val checkpointLocations = mutableListOf<List<Location>>()

        for (room in roomList) {
            val (meta, data) = RoomLoader.loadRoom(room)
            val cpls = pasteRoom(0, baseY, currentZ, currentCheckpoint, meta, data)
            if (cpls.isNotEmpty()) {
                checkpointLocations.add(cpls)
            }

            currentZ += meta.depth
            currentCheckpoint += meta.checkpoints.size
        }

        pasteLobbyPlatform()

        world.setGameRuleValue("doMobSpawning", "false")
        world.setGameRuleValue("doDaylightCycle", "false")
        world.setGameRuleValue("doWeatherCycle", "false")
        world.setGameRuleValue("randomTickSpeed", "0")

        val finalList = mutableListOf(listOf(Location(world, 18.0, 75.0, 4.0)))
        finalList.addAll(checkpointLocations)
        return finalList
    }

    /**
     * Pastes a room at the given coordinates.
     * Also creates the armor stands for checkpoint labels.
     */
    private fun pasteRoom(
        x: Int,
        y: Int,
        z: Int,
        currentCheckpoint: Int,
        meta: RoomMeta,
        data: BlockStructure
    ): List<Location> {
        val idToName = data.palette.entries.associateBy({ it.value }, { it.key })

        for (block in data.blocks) {
            val name = idToName[block.id] ?: continue
            val blockId = blockIDs[name]

            if (blockId == null) {
                Bukkit.getLogger().warning("Block ID not found for $name!")
                continue
            }

            val loc = world.getBlockAt(x + block.x, y + block.y, z + block.z)
            loc.setTypeIdAndData(blockId, block.meta.toByte(), false) // this is teeeechnically deprecated but oh well
        }

        val checkpoints = meta.checkpoints
        val checkpointLocations = mutableListOf<Location>()
        for ((idx, checkpoint) in checkpoints.withIndex()) {
            val loc1 = Location(world, x + checkpoint.x + 0.5, y + checkpoint.y + 0.5, z + checkpoint.z + 0.5)

            checkpointLocations.add(
                Location(
                    world,
                    (x + checkpoint.x).toDouble(),
                    (y + checkpoint.y).toDouble(),
                    (z + checkpoint.z).toDouble()
                )
            )

            if (meta.name != "finish_room") {
                val loc2 = Location(world, x + checkpoint.x + 0.5, y + checkpoint.y + 0.2, z + checkpoint.z + 0.5)

                val stand1 = world.spawn(loc1, ArmorStand::class.java).apply {
                    isVisible = false
                    isCustomNameVisible = true
                    customName = "§a§lCHECKPOINT"
                    isMarker = true
                    setGravity(false)
                }

                val stand2 = world.spawn(loc2, ArmorStand::class.java).apply {
                    isVisible = false
                    isCustomNameVisible = true
                    customName = "§e§l#${currentCheckpoint + idx}"
                    isMarker = true
                    setGravity(false)
                }
            } else {
                val stand1 = world.spawn(loc1, ArmorStand::class.java).apply {
                    isVisible = false
                    isCustomNameVisible = true
                    customName = "§6§lPARKOUR END"
                    isMarker = true
                    setGravity(false)
                }
            }
        }

        return checkpointLocations
    }

    /**
     * Pastes the floating pre-game lobby platform
     * and sets the world spawn in there.
     */
    private fun pasteLobbyPlatform() {
        val originX = 18
        val originY = 135
        val originZ = 3

        val blocks = RoomLoader.loadLobby()

        for ((xz, column) in blocks) {
            val (relX, relZ) = xz.split(",").map { it.toInt() }

            for ((yStr, blockData) in column) {
                val relY = yStr.toInt()
                val absX = originX + relX
                val absY = originY + relY
                val absZ = originZ + relZ

                val parts = blockData.split(":", limit = 2)
                val blockName = "minecraft:${parts[0]}"
                val meta = parts.getOrNull(1)?.toIntOrNull() ?: 0

                val typeId = blockIDs[blockName]
                if (typeId == null) {
                    Bukkit.getLogger().warning("Unknown block type: $blockName")
                    continue
                }

                val block = world.getBlockAt(absX, absY, absZ)
                block.setTypeIdAndData(typeId, meta.toByte(), false)
            }
        }

        world.setSpawnLocation(originX, originY + 1, originZ)
    }
}
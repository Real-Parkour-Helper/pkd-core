package org.realparkourhelper.core.trackers

import org.bukkit.Location
import org.bukkit.entity.Player

class CheckpointTracker(
    checkpointLocations: List<Location>,
    private val players: List<Player>,
    private val onCheckpoint: (Player, Int) -> Unit
) {

    private var tickCounter = 0
    private val checkpointLocations = checkpointLocations.toMutableList()
    private val checkpointMap: MutableMap<Player, Int> = mutableMapOf()

    init {
        for (player in players) {
            checkpointMap[player] = 0 // Initialize all players at checkpoint 0
        }
    }

    /**
     * Run the tick which check for checkpoint updates
     */
    fun tick() {
        if (++tickCounter % 2 != 0) return

        for (player in players) {
            val location = player.location
            val currentCheckpoint = checkpointMap[player] ?: throw IllegalStateException("Player not found in checkpoint map")
            val nextCheckpoint = checkpointLocations.getOrNull(currentCheckpoint + 1) ?: continue // No next checkpoint, so the player is already done

            if (location.distance(nextCheckpoint) < 2.5) {
                checkpointMap[player] = currentCheckpoint + 1
                onCheckpoint(player, currentCheckpoint + 1) // do something with this
            }
        }
    }

    /**
     * Resets the player to their last checkpoint.
     */
    fun resetToCheckpoint(player: Player) {
        val currentCheckpoint = checkpointMap[player] ?: throw IllegalStateException("Player not found in checkpoint map")
        val location = checkpointLocations[currentCheckpoint].clone().add(0.5, 0.5, 0.5)
        player.teleport(location)
    }

    fun getCheckpoint(index: Int): Location? {
        return checkpointLocations.getOrNull(index)
    }

    fun setCheckpoint(index: Int, location: Location) {
        if (index < 0 || index >= checkpointLocations.size) return
        checkpointLocations[index] = location
    }

    fun getCheckpoint(p: Player): Int? = checkpointMap[p]
    fun hasFinished(p: Player)    = (checkpointMap[p] ?: -1) >= checkpointLocations.lastIndex
}
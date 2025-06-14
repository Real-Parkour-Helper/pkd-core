package org.realparkourhelper.core.utils

import org.bukkit.Sound
import org.bukkit.entity.Player

object PKDSounds {

    /**
     * Plays the sound for a player when they reach a checkpoint.
     */
    fun playCheckpointSound(player: Player) {
        player.playSound(player.location, Sound.LEVEL_UP, 1.0f, 1.0f)
    }

    /**
     * Plays the sound for a player when they reset to the last checkpoint.
     */
    fun playResetSound(player: Player) {
        player.playSound(player.location, Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f)
    }

}
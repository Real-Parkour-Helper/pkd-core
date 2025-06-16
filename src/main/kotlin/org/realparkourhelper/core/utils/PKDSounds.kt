package org.realparkourhelper.core.utils

import org.bukkit.Sound
import org.bukkit.entity.Player

object PKDSounds {

    /**
     * Plays the sound for a player when they reach a checkpoint.
     */
    fun playCheckpointSound(player: Player) {
        player.playSound(player.location, Sound.LEVEL_UP, 1f, 1f)
    }

    /**
     * Plays the sound for a player when they reset to the last checkpoint.
     */
    fun playResetSound(player: Player) {
        player.playSound(player.location, Sound.ENDERMAN_TELEPORT, 8f, 4.05f)
    }

    /**
     * Plays the sound for a player when they toggle visibility.
     */
    fun playToggleVisibilitySound(player: Player) {
        player.playSound(player.location, Sound.CLICK, 0.55f, 2f)
    }

    /**
     * Plays the sound for a player when they are ready to use a boost.
     */
    fun playBoostReadySound(player: Player) {
        player.playSound(player.location, Sound.CHICKEN_EGG_POP, 1f, 1f)
    }

    /**
     * Plays the sound for a player when the countdown updates.
     */
    fun playTimerCountdownSound(player: Player) {
        player.playSound(player.location, Sound.NOTE_STICKS, 20f, 1f)
    }
}
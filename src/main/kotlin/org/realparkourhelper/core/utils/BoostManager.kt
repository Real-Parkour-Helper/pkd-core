package org.realparkourhelper.core.utils

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Manages boosts for a player, allowing them to use boosts with a cooldown.
 * The boost provider should return the cooldown time in seconds.
 */
class BoostManager(
    private val player: Player,
    private val plugin: JavaPlugin,
    private val cooldownProvider: () -> Int
) {
    private var lastBoostTime: Long = 0L
    private var taskId: Int = -1

    fun tryBoost(
        onSuccess: () -> Unit,
        onCooldownEnd: () -> Unit,
        onFail: (secondsLeft: Int) -> Unit
    ) {
        val now = System.currentTimeMillis()
        val cooldown = cooldownProvider() * 1000L
        val remaining = (lastBoostTime + cooldown) - now

        if (remaining > 0) {
            onFail(ceil(remaining / 1000.0).toInt())
            return
        }

        applyBoost()
        onSuccess()
        lastBoostTime = now
        startCooldownVisuals(cooldown, onCooldownEnd)
    }

    private fun applyBoost() {
        val magnitude = 12000
        val yaw = player.location.yaw
        val pitch = player.location.pitch

        val yawRad = Math.toRadians(yaw.toDouble())
        val pitchRad = Math.toRadians(pitch.toDouble())

        var x = -cos(pitchRad) * sin(yawRad)
        var y = -sin(pitchRad)
        var z = cos(pitchRad) * cos(yawRad)

        val length = sqrt(x*x + y*y + z*z)
        x /= length
        y /= length
        z /= length

        val packet = PacketContainer(PacketType.Play.Server.ENTITY_VELOCITY)
        packet.integers
            .write(0, player.entityId)
            .write(1, (x * magnitude).toInt())
            .write(2, (y * magnitude).toInt())
            .write(3, (z * magnitude).toInt())

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startCooldownVisuals(duration: Long, onCooldownEnd: () -> Unit) {
        val totalTicks = duration / 50
        var ticksLeft = totalTicks

        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, {
            val secondsLeft = (ticksLeft / 20).toInt()
            val progress = ticksLeft.toFloat() / totalTicks

            player.level = secondsLeft
            player.exp = progress

            if (ticksLeft-- <= 0) {
                Bukkit.getScheduler().cancelTask(taskId)
                player.level = 0
                player.exp = 0f
                onCooldownEnd()
            }
        }, 0L, 1L)
    }
}

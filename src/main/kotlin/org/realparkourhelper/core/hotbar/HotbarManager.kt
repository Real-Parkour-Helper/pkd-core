package org.realparkourhelper.core.hotbar

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

object HotbarManager {
    private val layouts = mutableMapOf<UUID, HotbarLayout>()
    private val heldSlots = mutableMapOf<UUID, Int>()

    private val lastClickTime = mutableMapOf<UUID, Long>()
    private const val CLICK_COOLDOWN_MS = 50L

    fun setLayout(player: Player, layout: HotbarLayout) {
        layouts[player.uniqueId] = layout
    }

    fun updateHeldSlot(player: Player, slot: Int) {
        heldSlots[player.uniqueId] = slot
    }

    fun handleClick(player: Player) {
        val now = System.currentTimeMillis()
        val uuid = player.uniqueId

        val last = lastClickTime[uuid]
        if (last != null && now - last < CLICK_COOLDOWN_MS) {
            return // debounce clicks
        }

        lastClickTime[uuid] = now

        val slot = heldSlots[uuid] ?: player.inventory.heldItemSlot
        layouts[uuid]?.handleClick(player, slot)
    }

    fun clear(player: Player) {
        layouts.remove(player.uniqueId)
        heldSlots.remove(player.uniqueId)
    }

    fun registerHotbarPacketHandlers(plugin: JavaPlugin) {
        val manager = ProtocolLibrary.getProtocolManager()

        manager.addPacketListener(object :
            PacketAdapter(plugin, ListenerPriority.HIGHEST, PacketType.Play.Client.HELD_ITEM_SLOT) {
            override fun onPacketReceiving(event: PacketEvent) {
                val newSlot = event.packet.integers.read(0)
                updateHeldSlot(event.player, newSlot)
            }
        })

        manager.addPacketListener(object : PacketAdapter(
            plugin, ListenerPriority.HIGHEST,
            PacketType.Play.Client.BLOCK_PLACE,
            PacketType.Play.Client.USE_ITEM,
            PacketType.Play.Client.ARM_ANIMATION
        ) {
            override fun onPacketReceiving(event: PacketEvent) {
                handleClick(event.player)
            }
        })
    }
}
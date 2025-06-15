package org.realparkourhelper.core.hotbar

import org.bukkit.entity.Player

class HotbarLayout(private val slots: Map<Int, HotbarSlot>) {
    fun applyTo(player: Player) {
        val inv = player.inventory

        for (i in 0..8) {
            val slot = slots[i]
            inv.setItem(i, slot?.item)
        }

        HotbarManager.setLayout(player, this)
    }

    fun handleClick(player: Player, slot: Int) {
        slots[slot]?.triggerClick(player)
    }
}
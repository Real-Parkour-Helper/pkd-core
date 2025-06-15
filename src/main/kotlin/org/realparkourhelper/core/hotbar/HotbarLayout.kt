package org.realparkourhelper.core.hotbar

import org.bukkit.entity.Player

class HotbarLayout(private val slots: Map<Int, HotbarSlot>) {
    fun applyTo(player: Player) {
        slots.forEach { (index, slot) ->
            player.inventory.setItem(index, slot.item)
        }
        HotbarManager.setLayout(player, this)
    }

    fun handleClick(player: Player, slot: Int) {
        slots[slot]?.triggerClick(player)
    }
}
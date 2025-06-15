package org.realparkourhelper.core.hotbar

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class HotbarSlot {
    lateinit var item: ItemStack
    private var click: ((Player) -> Unit)? = null

    fun onClick(block: (Player) -> Unit) {
        click = block
    }

    fun triggerClick(player: Player) {
        click?.invoke(player)
    }
}
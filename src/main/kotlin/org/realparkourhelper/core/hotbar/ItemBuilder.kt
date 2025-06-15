package org.realparkourhelper.core.hotbar

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class ItemBuilder(private val material: Material) {
    private val item = ItemStack(material)
    private val meta: ItemMeta = item.itemMeta ?: throw IllegalStateException("ItemMeta is null for $material")

    /** Set the display name (with color codes if you like) */
    fun name(displayName: String): ItemBuilder {
        meta.displayName = displayName
        return this
    }

    /** Set the lore lines */
    fun lore(vararg loreLines: String): ItemBuilder {
        meta.lore = loreLines.toList()
        return this
    }

    /** Set the stack amount */
    fun amount(amount: Int): ItemBuilder {
        item.amount = amount
        return this
    }

    /** Set the durability */
    fun durability(durability: Short): ItemBuilder {
        item.durability = durability
        return this
    }

    /** Finalize and return your ItemStack */
    fun build(): ItemStack {
        item.itemMeta = meta
        return item
    }
}
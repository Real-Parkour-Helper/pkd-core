package org.realparkourhelper.core.hotbar

class HotbarLayoutBuilder {
    private val slots = mutableMapOf<Int, HotbarSlot>()

    fun slot(index: Int, init: HotbarSlot.() -> Unit) {
        slots[index] = HotbarSlot().apply(init)
    }

    fun build(): HotbarLayout = HotbarLayout(slots)
}

fun hotbarLayout(init: HotbarLayoutBuilder.() -> Unit): HotbarLayout =
    HotbarLayoutBuilder().apply(init).build()
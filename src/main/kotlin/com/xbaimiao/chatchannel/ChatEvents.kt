package com.xbaimiao.chatchannel

import io.papermc.paper.event.player.AsyncChatEvent
import me.albert.amazingbot.bot.Bot
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.chat.uncolored

/**
 * @Author xbaimiao
 * @Date 2021/10/25 21:26
 */
@Suppress("unused")
object ChatEvents {
    private val enabled = ChatChannel.config.getBoolean("GameChatToGroup.Enable")
    private val groups = ChatChannel.config.getStringList("GameChatToGroup.groups")

    @SubscribeEvent
    fun chat(event: AsyncChatEvent) {
        if (!enabled) return

        val legacyMessage = LegacyComponentSerializer
            .legacySection()
            .serialize(event.message())
            .uncolored()

        groups.forEach { Bot.getApi().sendGroupMsg(it, "${event.player.name}: $legacyMessage") }
    }
}

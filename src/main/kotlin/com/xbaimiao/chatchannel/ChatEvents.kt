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

    @SubscribeEvent
    fun chat(event: AsyncChatEvent) {
        if (!ChatChannel.config.getBoolean("GameChatToGroup.Enable")) return

        val legacyMessage = LegacyComponentSerializer
            .legacySection()
            .serialize(event.message())
            .uncolored()

        ChatChannel.config.getStringList("GameChatToGroup.groups").forEach {
            Bot.getApi().sendGroupMsg(it, "${event.player.name}: $legacyMessage")
        }
    }
}

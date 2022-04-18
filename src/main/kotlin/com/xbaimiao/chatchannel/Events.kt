package com.xbaimiao.chatchannel

import me.albert.amazingbot.bot.Bot
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerAdvancementDoneEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.chat.uncolored

/**
 * @Author xbaimiao
 * @Date 2021/10/25 21:26
 */
object Events {

    @SubscribeEvent
    fun chat(event: AsyncPlayerChatEvent) {
        if (ChatChannel.config.getBoolean("GameChatToGroup.Enable")) {
            for (groups in ChatChannel.config.getStringList("GameChatToGroup.groups")) {
                var message = PlaceholderAPI.setPlaceholders(
                    event.player,
                    ChatChannel.config.getString("GameToGroup")
                )
                message = message.replace("%msg%", event.message.uncolored())
                Bot.getApi().sendGroupMsg(groups, message)
            }
        }
    }

    @SubscribeEvent
    fun join(event: PlayerJoinEvent) {
        val message = PlaceholderAPI.setPlaceholders(event.player, ChatChannel.config.getString("Message.Join"))
        ChatChannel.messageGroup.forEach {
            Bot.getApi().sendGroupMsg(it, message)
        }
    }

    @SubscribeEvent
    fun quit(event: PlayerQuitEvent) {
        val message = PlaceholderAPI.setPlaceholders(event.player, ChatChannel.config.getString("Message.Quit"))
        ChatChannel.messageGroup.forEach {
            Bot.getApi().sendGroupMsg(it, message)
        }
    }

    @SubscribeEvent
    fun death(event: PlayerDeathEvent) {
        val message = PlaceholderAPI.setPlaceholders(event.entity, ChatChannel.config.getString("Message.Death"))
        ChatChannel.messageGroup.forEach {
            Bot.getApi().sendGroupMsg(it, message)
        }
    }

    @SubscribeEvent
    fun a(event: PlayerAdvancementDoneEvent) {
        val key = event.advancement.key.key
        var name = key
        if (key in ChatChannel.advancement.getKeys(false)) {
            name = ChatChannel.advancement.getString(key)!!
        }
        if (name.endsWith("NO")) {
            return
        }
        ChatChannel.messageGroup.forEach {
            Bot.getApi().sendGroupMsg(it, "${event.player.name} 完成了成就: $name")
        }
    }

}
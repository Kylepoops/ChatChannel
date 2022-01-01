package com.xbaimiao.chatchannel

import kotlinx.coroutines.runBlocking
import me.albert.amazingbot.bot.Bot
import me.albert.amazingbot.events.GroupMessageEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.LightApp
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.common.platform.event.SubscribeEvent
import java.util.*

@Suppress("unused")
object GroupEvents {

    private const val unexpectedImageSizeMessage =
        "Amount of image placeholders in text components is %s, which is not equal to image components size %s"

    @SubscribeEvent
    fun chat(event: GroupMessageEvent) = runBlocking {
        val name = Bot.getApi().getPlayer(event.userID)
            ?.let { Bukkit.getOfflinePlayer(it).name }
            ?: event.event.sender.nameCard

        val msg = StringBuilder()
        val message = event.event.message
        val group = event.event.group
        val images = ArrayList<Image>()

        message.forEach {
            when (it) {
                is Image -> images.add(it)
                is At -> sendAtActionBar(name, it)
            }

            msg += when {
                it is Image -> ChatChannel.config.getString("imageFormat")!!
                it is At -> "§a@" + group[it.target]!!.nick
                it.contentToString().contains("\"app\":") -> "§f[§aAPP§f]"
                it is LightApp -> "§f[§aAPP§f]"
                else -> it.contentToString()
            }
        }

        val prefix: String = ChatChannel.config.getString("format")!!.replace("%user%", name)
        val text = prefix.replace("%msg%", msg.toString())

        sendMessage(images, text)
    }

    private suspend fun sendMessage(images: List<Image>, text: String) {
        if (images.isEmpty()) {
            Bukkit.getOnlinePlayers().asSequence()
                .filter(Player::switch)
                .forEach { it.sendMessage(Component.text(text)) }
            return
        }
        sendMixedMessage(images, text)
    }

    private suspend fun sendMixedMessage(images: List<Image>, text: String) {
        val imageFormat = ChatChannel.config.getString("imageFormat")!!
        val imageHover = ChatChannel.config.getString("imageHover")!!
        var message = Component.empty()

        val messageComponents = mutableListOf<Any>()
        val imageComponents = images.toMutableList()
        val textComponents = text.split(imageFormat).toMutableList()

        require(imageComponents.isNotEmpty()) { "Image components is empty" }
        val equals = textComponents.size - 1 == imageComponents.size
        require(equals) { unexpectedImageSizeMessage.format(textComponents.size - 1, imageComponents.size) }

        while (imageComponents.isNotEmpty()) {
            messageComponents += textComponents.removeFirst()
            messageComponents += imageComponents.removeFirst()
        }

        // there will still be one text left because text is always one more than images
        messageComponents += textComponents.removeFirst()

        check(imageComponents.isEmpty()) { "image components is not empty and its size is ${imageComponents.size}" }
        check(textComponents.isEmpty()) { "text components is not empty and its size is ${textComponents.size}" }

        for (component in messageComponents) {
            message += when (component) {
                is String -> Component.text(component)
                is Image -> {
                    Component.text(imageFormat)
                        .hoverEvent(HoverEvent.showText(Component.text(imageHover)))
                        .clickEvent(ClickEvent.runCommand("/qq look ${component.queryStringUrl()}"))
                }
                // should never happen
                else -> throw IllegalStateException("Unknown component type: ${component::class.java.name}")
            }
        }

        Bukkit.getOnlinePlayers().asSequence().filter(Player::switch).forEach { it.sendMessage(message) }
    }

    private fun sendAtActionBar(name: String, at: At) {
        Bot.getApi().getPlayer(at.target)
            ?.let { Bukkit.getOfflinePlayer(it) }?.player
            ?.sendTitle(
                ChatChannel.config.getString("At.title")!!.replace("%player%", name),
                ChatChannel.config.getString("At.subTitle")!!.replace("%player%", name),
                20, 20, 20
            )
    }
}

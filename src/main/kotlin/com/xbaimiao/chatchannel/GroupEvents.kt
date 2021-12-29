package com.xbaimiao.chatchannel

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

    @SubscribeEvent
    suspend fun chat(event: GroupMessageEvent) {
        val userId = event.userID
        val uuid: UUID? = Bot.getApi().getPlayer(userId)
        val name = uuid?.let { Bukkit.getOfflinePlayer(it).name } ?: event.event.sender.nameCard

//        val name = if (uuid == null) {
//            event.event.sender.nameCard
//        } else Bukkit.getOfflinePlayer(uuid).name

        val msg = StringBuilder()
        val message = event.event.message
        val group = event.event.group
        val images = ArrayList<Image>()

        message.forEach {
            when (it) {
                is Image -> images.add(it)
                is At -> sendAt(name, it)
            }

            msg += when {
                it is Image -> ChatChannel.config.getString("imageFormat")!!
                it is At -> "§a@" + Objects.requireNonNull(group[it.target])!!.nick
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
        sendImage(images, text)
    }

    private suspend fun sendImage(images: List<Image>, text: String) {
        val imageFormat = ChatChannel.config.getString("imageFormat")!!
        var message = Component.empty()

        val imageList = images.toMutableList()
        val caches = mutableListOf<Any>()
        var cacheText = text

        while (cacheText.isNotEmpty()) {
            caches += cacheText.substringBefore(imageFormat)
            cacheText = cacheText.substringAfter(imageFormat)
            caches += imageList.removeFirst()
        }

        for (element in caches) {
            message += when (element) {
                is String -> Component.text(element)
                is Image -> {
                    Component.text(imageFormat)
                        .hoverEvent(HoverEvent.showText(Component.text("点击查看图片")))
                        .clickEvent(ClickEvent.runCommand("/qq look ${element.queryStringUrl()}"))
                }
                // should never happen
                else -> throw IllegalStateException("Unknown element type: ${element::class.java.name}")
            }
        }

        Bukkit.getOnlinePlayers().asSequence().filter(Player::switch).forEach { it.sendMessage(message) }

//        no... what the hell is this
//        kinda unreadable to me
//        var imageAt = 0
//        val tellrawJson = TextComponent()
//
//        val replacedText = text.replace(imageFormat, "¤")
//        var stringBuilder = StringBuilder()
//        for (element in replacedText) {
//            if (element == '¤') {
//                tellrawJson.addExtra(stringBuilder.toString())
//                stringBuilder = StringBuilder()
//                val image = images[imageAt]
//                imageAt++
//                val t = TextComponent(imageFormat)
//                t.hoverEvent = HoverEvent(
//                    HoverEvent.Action.SHOW_TEXT,
//                    ComponentBuilder(ChatChannel.config.getString("imageHover")).create()
//                )
//                val url = Util.query(image)
//                t.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/qq look $url")
//                tellrawJson.addExtra(t)
//                continue
//            }
//            stringBuilder.append(element)
//        }
//        for (player in Bukkit.getOnlinePlayers()) {
//            if (player.isSwitch()) {
//                player.spigot().sendMessage(tellrawJson)
//            }
//        }
    }

    private fun sendAt(name: String, at: At) {
        val atId: Long = at.target
        Bot.getApi().getPlayer(atId)
            ?.let { Bukkit.getOfflinePlayer(it) }?.player
            ?.sendTitle(
                ChatChannel.config.getString("At.title")!!.replace("%player%", name),
                ChatChannel.config.getString("At.subTitle")!!.replace("%player%", name),
                20, 20, 20
            )

//        if (uuid != null) {
//            val player1 = Bukkit.getOfflinePlayer(uuid)
//            if (player1.isOnline) {
//                player1.player!!.sendTitle(
//                    ChatChannel.config.getString("At.title")!!.replace("%player%", name),
//                    ChatChannel.config.getString("At.subTitle")!!.replace("%player%", name),
//                    20, 20, 20
//                )
//            }
//        }
    }
}

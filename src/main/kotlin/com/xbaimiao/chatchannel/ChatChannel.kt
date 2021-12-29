package com.xbaimiao.chatchannel

import me.albert.amazingbot.bot.Bot
import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import taboolib.common.platform.Plugin
import taboolib.common.platform.command.PermissionDefault
import taboolib.common.platform.command.command
import taboolib.module.chat.uncolored
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.module.nms.buildMap
import taboolib.platform.BukkitPlugin
import taboolib.platform.util.sendLang
import java.net.URL
import java.util.logging.Logger

object ChatChannel : Plugin() {

    @Config(value = "config.yml")
    lateinit var config: Configuration
        private set

    lateinit var logger: Logger
        private set

//    val plugin by lazy { BukkitPlugin.getInstance() }

    override fun onEnable() {
        logger = BukkitPlugin.getInstance().logger

        command(
            name = "qq",
            permissionDefault = PermissionDefault.TRUE
        ) {
            literal("look", optional = true) {
                dynamic {
                    execute<Player> { sender, _, argument ->
                        runCatching { URL(argument) }
                            .onFailure { sender.sendMessage(Component.text("请输入正确的链接", NamedTextColor.RED)) }
                            .onSuccess { url ->
                                buildMap(url)
                                    .thenAccept { it.sendTo(sender) }
                                    .exceptionally {
                                        sender.sendMessage(
                                            Component.text("处理图片时发生错误，图片地址: $url", NamedTextColor.RED)
                                        )
                                        logger.severe("处理图片时发生错误， 图片地址: $url")
                                        logger.severe("错误信息: ${it.message}")
                                        null
                                    }

                                sender.sendMessage(Component.text("已发送请求，请等待图片下载完成"))
                            }
                    }
                }
            }
            literal("send", optional = true) {
                dynamic {
                    execute<Player> { sender, _, argument ->
                        var message = PlaceholderAPI.setPlaceholders(
                            sender,
                            config.getString("GameToGroup")
                        )
                        message = message.replace("%msg%", argument.uncolored())
                        sender.chat(argument)
                        Bot.getApi().bot.groups.forEach {
                            Bot.getApi().sendGroupMsg(it.id.toString(), message)
                        }
                    }
                }
            }
            literal("onOff", optional = true) {
                execute<Player> { sender, _, _ ->
                    if (sender.switch) {
                        sender.switch = false
                        sender.sendLang("off")
                    } else {
                        sender.switch = true
                        sender.sendLang("on")
                    }
                }
            }
        }
    }
}

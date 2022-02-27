package com.xbaimiao.chatchannel

import com.xbaimiao.chatchannel.Switch.isSwitch
import com.xbaimiao.chatchannel.Switch.setSwitch
import me.albert.amazingbot.bot.Bot
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.entity.Player
import taboolib.common.platform.Plugin
import taboolib.common.platform.command.PermissionDefault
import taboolib.common.platform.command.command
import taboolib.module.chat.uncolored
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import taboolib.module.nms.sendMap
import taboolib.platform.BukkitPlugin
import taboolib.platform.util.sendLang
import java.net.URL

object ChatChannel : Plugin() {

    @Config(value = "config.yml", autoReload = true, migrate = true)
    lateinit var config: ConfigFile
        private set

    @Config(value = "advancement.yml", autoReload = true, migrate = true)
    lateinit var advancement: ConfigFile
        private set

    val plugin by lazy { BukkitPlugin.getInstance() }

    val messageGroup get() = config.getStringList("MessageGroup")

    override fun onDisable() {
        messageGroup.forEach {
            Bot.getApi().sendGroupMsg(it,"服务器已关闭!")
        }
    }

    override fun onEnable() {
        messageGroup.forEach {
            Bot.getApi().sendGroupMsg(it,"服务器已启动!")
        }
        command(
            name = "qq",
            permissionDefault = PermissionDefault.TRUE
        ) {
            literal("look", optional = true) {
                dynamic {
                    execute<Player> { sender, context, argument ->
                        sender.sendMap(URL(argument))
                    }
                }
            }
            literal("send", optional = true) {
                dynamic {
                    execute<Player> { sender, context, argument ->
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
                execute<Player> { sender, context, argument ->
                    if (sender.isSwitch()) {
                        sender.setSwitch(false)
                        sender.sendLang("off")
                    } else {
                        sender.setSwitch(true)
                        sender.sendLang("on")
                    }
                }
            }
        }
    }

}
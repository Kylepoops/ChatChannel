package com.xbaimiao.chatchannel

import taboolib.common.platform.Plugin
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.platform.BukkitPlugin
import java.util.logging.Logger

object ChatChannel : Plugin() {

    @Config(value = "config.yml", autoReload = true, migrate = true)
    lateinit var config: Configuration
        private set

    @Config(value = "advancement.yml", autoReload = true, migrate = true)
    lateinit var advancement: Configuration
        private set

    lateinit var logger: Logger
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
        logger = BukkitPlugin.getInstance().logger
        Command.register()
    }
}

}
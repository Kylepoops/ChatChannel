package com.xbaimiao.chatchannel

import taboolib.common.platform.Plugin
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.platform.BukkitPlugin
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
        Command.register()
    }
}

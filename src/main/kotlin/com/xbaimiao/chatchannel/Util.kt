package com.xbaimiao.chatchannel

import me.albert.amazingbot.bot.Bot
import net.kyori.adventure.text.TextComponent
import net.mamoe.mirai.Mirai
import net.mamoe.mirai.message.data.Image

suspend fun Image.queryStringUrl() = Mirai.queryImageUrl(Bot.getApi().bot, this)

operator fun TextComponent.plus(other: TextComponent) = this.append(other)

operator fun StringBuilder.plusAssign(element: String) {
    this.append(element)
}

package com.xbaimiao.chatchannel

import com.xbaimiao.chatchannel.util.ImageUrlFinder
import net.kyori.adventure.text.TextComponent
import net.mamoe.mirai.message.data.Image

fun Image.queryStringUrl(): String = ImageUrlFinder.queryImageUrl(this)

operator fun TextComponent.plus(other: TextComponent) = this.append(other)

operator fun StringBuilder.plusAssign(element: String) {
    this.append(element)
}

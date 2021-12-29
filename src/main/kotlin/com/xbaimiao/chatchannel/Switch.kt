package com.xbaimiao.chatchannel

import org.bukkit.entity.Player

/**
 * @Author xbaimiao
 * @Date 2021/10/25 21:17
 */

private val map = HashMap<String, Boolean>()

var Player.switch: Boolean
    get() = map[name] ?: true
    set(value) { map[name] = value }

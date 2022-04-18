package com.xbaimiao.chatchannel.manager

import com.google.common.cache.CacheBuilder
import com.xbaimiao.chatchannel.queryStringUrl
import net.mamoe.mirai.message.data.Image
import java.net.URL
import kotlin.random.Random

object ImageManager {
    private val imageCache = CacheBuilder.newBuilder()
        .maximumSize(100)
        .build<Long, URL>()

    fun add(image: Image): Long {
        val id = Random.nextLong(1457607508, Long.MAX_VALUE)
        imageCache.put(id, URL(image.queryStringUrl()))
        return id
    }

    operator fun get(id: Long): URL? {
        return imageCache.getIfPresent(id)
    }
}

package com.xbaimiao.chatchannel.util;

import me.albert.amazingbot.bot.Bot;
import net.mamoe.mirai.Mirai;
import net.mamoe.mirai.message.data.Image;
import org.jetbrains.annotations.NotNull;

public class ImageUrlFinder {

    public static @NotNull String queryImageUrl(Image image) {
        return Mirai.getInstance().queryImageUrl(Bot.getApi().getBot(), image);
    }
}

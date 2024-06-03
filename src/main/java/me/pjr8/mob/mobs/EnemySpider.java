package me.pjr8.mob.mobs;

import me.pjr8.mob.objects.AbstractMob;
import org.bukkit.entity.Spider;

public class EnemySpider extends AbstractMob {
    public EnemySpider() {
        super(Spider.class, 20, 10, "Spider");
    }
}
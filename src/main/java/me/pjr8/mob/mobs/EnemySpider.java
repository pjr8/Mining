package me.pjr8.mob.mobs;

import me.pjr8.mob.objects.IMob;
import me.pjr8.mob.objects.MobStats;
import org.bukkit.entity.*;

public class EnemySpider extends MobStats implements IMob {

    public EnemySpider() {
        entityType = Spider.class;
        maxHealth = 20;
        currentHealth = maxHealth;
        damage = 10;
        name = "Spider";
    }

    @Override
    public void onAttack(Player player) {
        player.sendMessage("You have been attacked by a spider!");
    }
}

package me.pjr8.mob;

import me.pjr8.Item.Item;
import me.pjr8.mob.mobs.EnemyPanda;
import me.pjr8.mob.mobs.EnemySpider;
import me.pjr8.mob.objects.AbstractMob;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class MobDropTable {

    public static Map<Item, Integer> getDrops(AbstractMob abstractMob) {
        Map<Item, Integer> dropMap = new HashMap<>();
        Random random = new Random();
        Objects.requireNonNull(getMobDropTable(abstractMob)).forEach((item, doubles) -> {
            Double randomDouble = random.nextDouble();
            System.out.println(randomDouble);
            if (random.nextDouble() <= doubles[0]) {
                if (doubles.length == 3) {
                    dropMap.put(item, random.nextInt(doubles[2].intValue() - doubles[1].intValue()) + doubles[1].intValue());
                } else {
                    dropMap.put(item, doubles[1].intValue());
                }
            }
        });
        return dropMap;
    }

    private static Map<Item, Double[]> getMobDropTable(AbstractMob abstractMob) {
        if (abstractMob instanceof EnemyPanda) {
            return new HashMap<>() {{
                put(Item.BAMBOO, new Double[] {0.5, 1.0, 3.0});
            }};
        } else if (abstractMob instanceof EnemySpider) {
            return new HashMap<>() {{
                put(Item.STRING, new Double[] {0.5, 1.0, 3.0});
            }};
        }
        return null;
    }
}

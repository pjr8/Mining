package me.pjr8.Item;

import lombok.Getter;

import java.awt.*;

@Getter
public enum Rarity {

    ABUNDANT("Abundant", new Color(153, 153, 153)),
    UNCOMMON("Uncommon", new Color(86, 196, 120)),
    RARE("Rare", new Color(0, 208, 255)),
    EXOTIC("Exotic", new Color(224, 61, 91)),
    MYTHIC("Mythic", new Color(139, 21, 230)),
    PICKAXE("PICKAXE", new Color(93, 222, 173)),
    QUEST("Quest", new Color(255, 255, 0)),
    MISC("Misc.", new Color(255, 255, 255)),
    UPGRADE("Upgrade", new Color(255, 204, 45));

    private final String name;
    private final Color color;

    Rarity(String name, Color color) {
        this.name = name;
        this.color = color;
    }
}
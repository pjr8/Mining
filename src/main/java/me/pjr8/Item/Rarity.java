package me.pjr8.Item;

import lombok.Getter;

import java.awt.*;

@Getter
public enum Rarity {

    ABUNDANT("Abundant", new Color(153, 153, 153)),
    UNCOMMON("Uncommon", new Color(86, 196, 120)),
    RARE("Rare", new Color(0, 208, 255)),
    EXOTIC("Exotic", new Color(224, 61, 91)),
    MYTHIC("Mythic", new Color(139, 21, 230));

    private final String name;
    private final Color color;

    Rarity(String name, Color color) {
        this.name = name;
        this.color = color;
    }
}
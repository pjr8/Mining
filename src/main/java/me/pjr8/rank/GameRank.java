package me.pjr8.rank;

import lombok.Getter;

@Getter
public enum GameRank {

    BEGINNER("", 0),
    INITIATE("Initiate", 1),
    SEEKER("Seeker", 2),
    WARRIOR("Warrior", 3),
    MYSTIC("Mystic", 4),
    SENTINEL("Sentinel", 5),
    TITAN("Titan", 6),
    GUARDIAN("Guardian", 7),
    ASCENDANT("Ascendant", 8);

    private final String name;
    private final int layer;

    GameRank(String name, int layer) {
        this.name = name;
        this.layer = layer;
    }

    public static String getLayerSymbol(int number) {
        return switch (number) {
            case 1 -> "①";
            case 2 -> "②";
            case 3 -> "③";
            case 4 -> "④";
            case 5 -> "⑤";
            case 6 -> "⑥";
            case 7 -> "⑦";
            case 8 -> "⑧";
            case 9 -> "⑨";
            default -> "⓪";
        };
    }
}

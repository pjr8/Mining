package me.pjr8.database.playerdata;

import lombok.Data;
import me.pjr8.skill.SkillType;

import java.util.HashMap;
import java.util.UUID;

@Data
public class PlayerStats {

    private UUID uuid;

    private double maxHealth = 100;

    private double health = 100;

    private int miningExperience = 0;

    private int forgingExperience = 0;

    private int combatExperience = 0;

    private HashMap<String, Integer> mobStats = new HashMap<>();

    //TODO Add a list of a new object (maybe PlayerEntityInfo?) based on the player's stats regarding a specific mob, like kills and deaths to the mob.

    public PlayerStats() {
    }

    public PlayerStats(UUID uuid) {
        this.uuid = uuid;
    }

    public void addExperience(SkillType skillType, int experience) {
        switch (skillType) {
            case MINING -> miningExperience += experience;
            case FORGE -> forgingExperience += experience;
            case COMBAT -> combatExperience += experience;
        }
    }

    public void addMobStatsData(String key, int amount) {
        if (mobStats.containsKey(key)) {
            mobStats.put(key, mobStats.get(key) + amount);
        } else {
            mobStats.put(key, amount);
        }
    }
}

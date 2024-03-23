package me.pjr8.database.playerdata;

import lombok.Data;
import me.pjr8.skill.SkillType;

import java.util.HashMap;
import java.util.UUID;

@Data
public class PlayerStats {

    private UUID uuid;

    private double maxHealth;

    private double health;

    private int miningExperience;

    private int forgingExperience;

    private int combatExperience;

    private HashMap<String, Integer> mobInfo;

    //TODO Add a list of a new object (maybe PlayerEntityInfo?) based on the player's stats regarding a specific mob, like kills and deaths to the mob.

    public void addExperience(SkillType skillType, int experience) {
        switch (skillType) {
            case MINING -> miningExperience += experience;
            case FORGE -> forgingExperience += experience;
            case COMBAT -> combatExperience += experience;
        }
    }

    public void addMobInfoData(String key, int amount) {
        if (mobInfo.containsKey(key)) {
            mobInfo.put(key, mobInfo.get(key) + amount);
        } else {
            mobInfo.put(key, amount);
        }
    }
}

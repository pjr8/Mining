package me.pjr8.rank;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

@Getter
public enum ServerRank {

    //Regular rank
    USER("", 0),

    //Donator Ranks


    //Staff ranks
    ADMIN(ChatColor.RED + "Admin", 9),
    OWNER(ChatColor.GOLD + "" + ChatColor.BOLD + "Owner", 10);

    private final String name;
    private final int authority;

    ServerRank(String name, int authority) {
        this.name = name;
        this.authority = authority;
    }
}

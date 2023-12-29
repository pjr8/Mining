package me.pjr8.forge.objects;

import lombok.Data;
import me.pjr8.forge.enums.ForgeItem;

import java.io.Serializable;

@Data
public class ForgeSlot implements Serializable {

    private ForgeItem forgeItem;
    private Long timeStarted;

}

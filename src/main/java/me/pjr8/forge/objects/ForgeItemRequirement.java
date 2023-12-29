package me.pjr8.forge.objects;

import lombok.Data;
import me.pjr8.Item.Item;

import java.util.List;

@Data
public class ForgeItemRequirement {

    private List<ForgeItemComponent> componentList;

    public ForgeItemRequirement(List<ForgeItemComponent> componentList){
        this.componentList = componentList;
    }

}

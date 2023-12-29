package me.pjr8.forge.objects;

import lombok.Data;
import me.pjr8.Item.Item;

@Data
public class ForgeItemComponent {

    private Item item;
    private int amount;

    public ForgeItemComponent(Item item, int amount) {
        this.item = item;
        this.amount = amount;
    }

}

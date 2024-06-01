package me.pjr8.forge;

import me.pjr8.Item.Item;
import me.pjr8.database.PlayerData;
import me.pjr8.database.PlayerDataHandler;
import me.pjr8.forge.enums.ForgeItem;
import me.pjr8.forge.enums.ForgeMenuType;
import me.pjr8.forge.gui.ForgeMenu;
import me.pjr8.forge.objects.ForgeItemComponent;
import me.pjr8.forge.objects.ForgeSlot;
import me.pjr8.mining.enums.PickaxeType;
import me.pjr8.update.UpdateEvent;
import me.pjr8.update.UpdateType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Forge implements Listener {

    public HashMap<Player, ForgeMenu> playersInForgeMenu = new HashMap<Player, ForgeMenu>();
    public static ArrayList<Player> playerRemovable = new ArrayList<Player>();

    private final PlayerDataHandler playerDataHandler;


    public Forge(PlayerDataHandler playerDataHandler) {
        this.playerDataHandler = playerDataHandler;
    }

    @EventHandler
    public void onForgeInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        ForgeMenu forgeMenu = playersInForgeMenu.get(player);
        ItemStack itemStack = event.getCurrentItem();
        if (forgeMenu == null || itemStack == null) return;
        event.setCancelled(true);
        PlayerData playerData = playerDataHandler.getPlayerDataHolder().get(player.getUniqueId());

        if (itemStack.getType() == Material.ARROW && ChatColor.stripColor(itemStack.getItemMeta().getDisplayName()).contains("Back")) {
            forgeMenu.backUp();
            return;
        }

        if (forgeMenu.getForgeMenuType().equals(ForgeMenuType.MAIN_MENU)) {
            if (itemStack.getType() == Material.LIME_DYE) {
                forgeMenu.initializeForgeSlotTypeSelectionMenu();
                forgeMenu.openInventory();
            } else if (itemStack.getType() != Material.BLACK_STAINED_GLASS_PANE && itemStack.getType() != Material.RED_DYE) {
                Item item = Item.getItemFromItemStack(itemStack);
                for (ForgeSlot forgeSlot : playerData.getForgeData().getForgeSlotsCurrentlyUsed()) {
                    if (forgeSlot.getForgeItem().getItem() == item) {
                        if (forgeMenu.isForgeComplete(forgeSlot)) {
                            if (forgeSlot.getForgeItem().getItem() != null) { //Item
                                player.getInventory().addItem(item.generate(1));
                            } else { //Pickaxe
                                playerData.getPickaxeData().setPickaxeType(forgeSlot.getForgeItem().getPickaxeType());
                            }
                            player.sendMessage("Claimed " + forgeSlot.getForgeItem().getItem().getName());
                            playerData.getForgeData().getForgeSlotsCurrentlyUsed().remove(forgeSlot);
                            forgeMenu.initializeMainMenu(false);
                            player.updateInventory();
                            break;
                        }
                    }
                }
            }

        } else if (forgeMenu.getForgeMenuType().equals(ForgeMenuType.FORGE_MENU_SELECTION)) {
            switch (itemStack.getType()) {
                case DIAMOND_PICKAXE -> forgeMenu.initializeForgeSelectionMenu(ForgeMenuType.FORGE_MENU_PICKAXE, 1);
                case DIAMOND_SWORD -> forgeMenu.initializeForgeSelectionMenu(ForgeMenuType.FORGE_MENU_SWORD, 1);
                case DIAMOND_CHESTPLATE -> forgeMenu.initializeForgeSelectionMenu(ForgeMenuType.FORGE_MENU_ARMOR, 1);
                case DIAMOND_HORSE_ARMOR -> forgeMenu.initializeForgeSelectionMenu(ForgeMenuType.FORGE_MENU_MISC, 1);
            }
            forgeMenu.openInventory();
        } else if (forgeMenu.getForgeMenuType().equals(ForgeMenuType.FORGE_MENU_CONFIRM)) {
            if (itemStack.getType() == Material.RED_STAINED_GLASS_PANE) {
                forgeMenu.backUp();
            } else if (itemStack.getType() == Material.GREEN_STAINED_GLASS_PANE) {
                if (forgeMenu.getCanCraftItem()) {
                    if (playerData.getForgeData().getForgeUnlockedSlots() == playerData.getForgeData().getForgeSlotsCurrentlyUsed().size()) {
                        player.sendMessage("You do not have any more forge slots available. Wait, how are you even in this menu???");
                    } else {
                        removeItemsFromPlayer(forgeMenu.getForgeItem().getForgeItemRequirement().getComponentList(), player);
                        player.closeInventory();
                        ForgeSlot forgeSlot = new ForgeSlot();
                        forgeSlot.setForgeItem(forgeMenu.getForgeItem());
                        forgeSlot.setTimeStarted(System.currentTimeMillis());
                        playerData.getForgeData().getForgeSlotsCurrentlyUsed().add(forgeSlot);
                        player.sendMessage("You have started forging " + forgeMenu.getForgeItem().getItem().getName());
                    }
                }
            }
        } else {
            if (itemStack.getType() != Material.BLACK_STAINED_GLASS_PANE && itemStack.getType() != Material.RED_STAINED_GLASS_PANE && itemStack.getType() != Material.ARROW) {
                Integer item_id = Item.getItemIDFromItemStack(itemStack);
                Integer pickaxe_id = PickaxeType.getPickaxeTypeIDFromItemStack(itemStack);
                ForgeItem forgeItem;
                if (item_id == null) {
                    forgeItem = ForgeItem.getForgeItemFromPickaxeType(PickaxeType.getPickaxeTypeFromID(pickaxe_id));
                } else {
                    forgeItem = ForgeItem.getForgeItemFromItem(Item.getItemFromID(item_id));
                }
                assert forgeItem != null;
                forgeMenu.setForgeItem(forgeItem);
                forgeMenu.initializeForgeConfirmMenu(forgeItem, forgeMenu.getForgeMenuType());
                forgeMenu.openInventory();
            }
        }

    }

    @EventHandler
    public void updateForgeMenuTimeLeft(UpdateEvent event) {
        if (event.getType() != UpdateType.SEC) {
            return;
        }
        for (Map.Entry<Player, ForgeMenu> entrySet : playersInForgeMenu.entrySet()) {
            Player player = entrySet.getKey();
            ForgeMenu forgeMenu = entrySet.getValue();
            if (forgeMenu.getForgeMenuType().equals(ForgeMenuType.MAIN_MENU)) {
                forgeMenu.initializeMainMenu(false);
                player.updateInventory();
            }

        }
    }

    @EventHandler
    public void removeInForgeMenu(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player player) {
            if (playerRemovable.contains(player)) {
                playersInForgeMenu.remove(player);
            }
        }
    }

    private void removeItemsFromPlayer(List<ForgeItemComponent> itemListToRemove, Player player) {
        for (ForgeItemComponent forgeItemComponent : itemListToRemove) {
            int amountToRemove = forgeItemComponent.getAmount();
            ItemStack[] itemStacks = player.getInventory().getContents();
            for (int i = 0 ; i < itemStacks.length ; i++) {
                ItemStack itemStack = itemStacks[i];
                if (itemStack != null) {
                    Item item = Item.getItemFromItemStack(itemStacks[i]);
                    if (amountToRemove > 0) {
                        if (forgeItemComponent.getItem() == item) {
                            if (itemStacks[i].getAmount() > amountToRemove) {
                                itemStack.setAmount(itemStack.getAmount() - amountToRemove);
                                player.getInventory().setItem(i, itemStack);
                                break;
                            } else if (itemStacks[i].getAmount() == amountToRemove) {
                                player.getInventory().setItem(i, new ItemStack(Material.AIR));
                                break;
                            } else {
                                player.getInventory().setItem(i, new ItemStack(Material.AIR));
                                amountToRemove -= itemStacks[i].getAmount();
                            }
                        }
                    }
                }
            }
        }


    }

    public static HashMap<Item, Integer> getItemsInInventory(Player player) {
        HashMap<Item, Integer> toReturn = new HashMap<>();
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack != null) {
                Item item = Item.getItemFromItemStack(itemStack);
                if (toReturn.containsKey(item)) {
                    toReturn.put(item, toReturn.get(item) + itemStack.getAmount());
                } else {
                    toReturn.put(item, itemStack.getAmount());
                }
            }
        }
        return toReturn;
    }

    private int getSlot(int slotGiven) {
        int toReturn = -1;
        switch (slotGiven) {
            case 11 -> toReturn = 0;
            case 12 -> toReturn = 1;
            case 13 -> toReturn = 2;
            case 14 -> toReturn = 3;
            case 15 -> toReturn = 4;
            case 19 -> toReturn = 5;
            case 20 -> toReturn = 6;
            case 21 -> toReturn = 7;
            case 22 -> toReturn = 8;
            case 23 -> toReturn = 9;
            case 24 -> toReturn = 10;
            case 25 -> toReturn = 11;
            case 28 -> toReturn = 12;
            case 29 -> toReturn = 13;
            case 30 -> toReturn = 14;
            case 31 -> toReturn = 15;
            case 32 -> toReturn = 16;
            case 33 -> toReturn = 17;
            case 34 -> toReturn = 18;
            case 38 -> toReturn = 19;
            case 39 -> toReturn = 20;
            case 40 -> toReturn = 21;
            case 41 -> toReturn = 22;
            case 42 -> toReturn = 23;
        }
        return toReturn;
    }
}

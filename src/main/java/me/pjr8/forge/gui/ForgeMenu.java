package me.pjr8.forge.gui;

import lombok.Data;
import me.pjr8.Item.Item;
import me.pjr8.Main;
import me.pjr8.database.PlayerData;
import me.pjr8.forge.Forge;
import me.pjr8.forge.enums.ForgeItem;
import me.pjr8.forge.enums.ForgeMenuType;
import me.pjr8.forge.objects.ForgeItemComponent;
import me.pjr8.forge.objects.ForgeSlot;
import me.pjr8.mining.enums.PickaxeType;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ForgeMenu {
    private final Player player;
    private final PlayerData playerData;
    private final List<Integer> FORGE_SLOT_MAIN_MENU = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 17, 18,
            26, 27, 35, 36, 37, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53);
    private final List<Integer> FORGE_CONFIRM_DENY = List.of(27, 28, 29, 36, 37, 38, 45, 46, 47);
    private final List<Integer> FORGE_CONFIRM_ACCEPT = List.of(33, 34, 35, 42, 43, 44, 51, 52, 53);

    private Inventory inventory;
    private int page;
    private ForgeMenuType forgeMenuType;
    private ForgeMenuType beforeConfirmType;
    private ForgeItem forgeItem;
    private Boolean canCraftItem = false;

    public ForgeMenu(Player player, ForgeMenuType forgeMenuType, int page) {
        this.player = player;
        this.playerData = Main.playerDataHandler.getPlayerDataHolder().get(player.getUniqueId());
        this.forgeMenuType = forgeMenuType;
        switch (forgeMenuType) {
            case MAIN_MENU -> initializeMainMenu(true);
            case FORGE_MENU_SELECTION -> initializeForgeSlotTypeSelectionMenu();
            default -> initializeForgeSelectionMenu(forgeMenuType, page);
        }
        openInventory();
    }

    public void initializeMainMenu(boolean create) {
        this.forgeMenuType = ForgeMenuType.MAIN_MENU;
        if (create) {
            inventory = Bukkit.createInventory(null, 54, "Forge");
        }
        int maxSlots = playerData.getForgeData().getForgeUnlockedSlots();
        int slotsUsed = 0;
        ArrayList<ForgeSlot> forgeSlotsCurrentlyUsed = playerData.getForgeData().getForgeSlotsCurrentlyUsed();
        for (int i = 0; i < inventory.getSize() ; i++) {
            if (FORGE_SLOT_MAIN_MENU.contains(i)) {
                inventory.setItem(i, createBackgroundItem());
            } else {
                if (maxSlots > slotsUsed) {
                    if (forgeSlotsCurrentlyUsed.size() <= slotsUsed || forgeSlotsCurrentlyUsed.isEmpty()) {
                        inventory.setItem(i, createAvailableForgeSlotItem());
                    } else {
                        inventory.setItem(i, createForgeMainMenuSlotItem(forgeSlotsCurrentlyUsed.get(slotsUsed)));
                    }
                    slotsUsed++;
                } else {
                    inventory.setItem(i, createLockedForgeSlotItem());
                }
            }
        }
    }

    public void initializeForgeSlotTypeSelectionMenu() {
        this.forgeMenuType = ForgeMenuType.FORGE_MENU_SELECTION;
        inventory = Bukkit.createInventory(null, 27, "Select the type of item to forge");

        inventory.setItem(10, createItemStack(Material.DIAMOND_PICKAXE, ChatColor.GOLD + "Pickaxe Forges"));
        inventory.setItem(12, createItemStack(Material.DIAMOND_SWORD, ChatColor.GOLD + "Sword Forges"));
        inventory.setItem(14, createItemStack(Material.DIAMOND_CHESTPLATE, ChatColor.GOLD + "Armor Forges"));
        inventory.setItem(16, createItemStack(Material.DIAMOND_HORSE_ARMOR, ChatColor.GOLD + "Misc. Forges"));
        inventory.setItem(18, createBackArrow());

        for (int i = 0; i < inventory.getSize() ; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, createBackgroundItem());
            }
        }
    }

    public void initializeForgeSelectionMenu(ForgeMenuType forgeMenuType, int page) {
        this.forgeMenuType = forgeMenuType;
        inventory = Bukkit.createInventory(null, 54, "Click on an item to forge");
        int slotsAvailable = 45;

        for (int i = 0; i < 8 ; i++) {
            inventory.setItem(i + 45, createBackgroundItem());
        }
        if (page > 1) {
            inventory.setItem(45, createItemStack(Material.ARROW, ChatColor.WHITE + "Go to page " + (page-1)));
            inventory.setItem(46, createBackArrow());
        } else {
            inventory.setItem(45, createBackArrow());
        }

        inventory.setItem(53, createItemStack(Material.ARROW, ChatColor.WHITE + "Go to page " + (page+1)));

        ArrayList<ForgeItem> selectionArrayList = ForgeItem.forgeItemByForgeType(forgeMenuType);

        for (int i = 0 ; i < slotsAvailable ; i++) {
            if (!selectionArrayList.isEmpty() && i < selectionArrayList.size()) {
                inventory.setItem(i, createForgeSelectionMenuSlotItem(selectionArrayList.get(i)));
            } else {
                inventory.setItem(i, createAvailableForgeSelectionLocked());
            }
        }
    }

    public void initializeForgeConfirmMenu(ForgeItem forgeItem, ForgeMenuType beforeConfirmType) {
        this.forgeMenuType = ForgeMenuType.FORGE_MENU_CONFIRM;
        inventory = Bukkit.createInventory(null, 54, "Forge Confirmation");
        this.beforeConfirmType = beforeConfirmType;
        List<ForgeItemComponent> forgeItemComponentList = forgeItem.getForgeItemRequirement().getComponentList();
        ItemStack canCraftConfirmMenuItem;
        HashMap<ForgeItemComponent, Boolean> canCraftList = checkCraftRequirements(forgeItem.getForgeItemRequirement().getComponentList());
        canCraftConfirmMenuItem = createConfirmMenuCraftAcceptItem(canCraftList);

        canCraftItem = !canCraftList.containsValue(false);


        for (int i = 0 ; i < inventory.getSize() ; i++) {
            if (i <= forgeItemComponentList.size()-1) {
                ForgeItemComponent forgeItemComponent = forgeItemComponentList.get(i);
                inventory.setItem(i, createConfirmMenuRequirementItem(forgeItemComponent.getItem(), forgeItemComponent.getAmount()));
            } else if (FORGE_CONFIRM_DENY.contains(i)) {
                inventory.setItem(i, createConfirmDenyItem());
            } else if (FORGE_CONFIRM_ACCEPT.contains(i)) {
                inventory.setItem(i, canCraftConfirmMenuItem);
            } else if (i == 40) {
                inventory.setItem(40, forgeItem.getItem().generate(1));
            } else {
                inventory.setItem(i, createBackgroundItem());
            }
        }
    }

    public void openInventory() {
        Forge.playerRemovable.remove(player);
        player.openInventory(inventory);
        Forge.playerRemovable.add(player);
    }

    public void backUp() {
        switch(forgeMenuType) {
            case FORGE_MENU_SELECTION -> initializeMainMenu(true);
            case FORGE_MENU_CONFIRM -> initializeForgeSelectionMenu(beforeConfirmType, page);
            default -> initializeForgeSlotTypeSelectionMenu();
        }
        canCraftItem = false;
        openInventory();
    }

    private HashMap<ForgeItemComponent, Boolean> checkCraftRequirements(List<ForgeItemComponent> forgeItemComponentList) {
        HashMap<ForgeItemComponent, Boolean> toReturn = new HashMap<>();
        HashMap<Item, Integer> playerItemList = Forge.getItemsInInventory(player);
        for (ForgeItemComponent forgeItemComponent : forgeItemComponentList) {
            if (playerItemList.containsKey(forgeItemComponent.getItem())) {
                if (playerItemList.get(forgeItemComponent.getItem()) >= forgeItemComponent.getAmount()) {
                    toReturn.put(forgeItemComponent, true);
                } else {
                    toReturn.put(forgeItemComponent, false);
                }
            } else {
                toReturn.put(forgeItemComponent, false);
            }
        }
        return toReturn;
    }

    private ItemStack createConfirmMenuCraftAcceptItem(HashMap<ForgeItemComponent, Boolean> list) {
        ItemStack toReturn;
        if (!list.containsValue(false)) {
            toReturn = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        } else {
            toReturn = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        }
        ItemMeta itemMeta = toReturn.getItemMeta();
        if (toReturn.getType() == Material.GREEN_STAINED_GLASS_PANE) {
            itemMeta.setDisplayName("CLICK TO CRAFT");
        } else {
            itemMeta.setDisplayName("REQUIREMENTS NOT MET");
        }

        ArrayList<String> lore = getLore(list);
        itemMeta.setLore(lore);
        toReturn.setItemMeta(itemMeta);
        return toReturn;
    }

    private ArrayList<String> getLore(HashMap<ForgeItemComponent, Boolean> list) {
        ArrayList<String> lore = new ArrayList<>();
        for (Map.Entry<ForgeItemComponent, Boolean> entrySet : list.entrySet()) {
            ForgeItemComponent forgeItemComponent = entrySet.getKey();
            Boolean check = entrySet.getValue();
            if (check) {
                lore.add(ChatColor.GREEN + forgeItemComponent.getItem().getName() + " x" + forgeItemComponent.getAmount());
            } else {
                lore.add(ChatColor.RED + forgeItemComponent.getItem().getName() + " x" + forgeItemComponent.getAmount());
            }
        }
        return lore;
    }

    private ItemStack createForgeMainMenuSlotItem(ForgeSlot forgeSlot) {
        ItemStack itemStack = Item.generateItem(forgeSlot.getForgeItem().getItem(), 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        if (itemMeta.getLore() != null) {
            ArrayList<String> lore = new ArrayList<>(itemMeta.getLore());
            lore.add("");
            if (isForgeComplete(forgeSlot)) {
                lore.add(ChatColor.GREEN + "COMPLETE");
            } else {
                lore.add(ChatColor.GREEN + "Time left: " + calculateTimeRemaining(System.currentTimeMillis() - (forgeSlot.getTimeStarted() + forgeSlot.getForgeItem().getForgeTimeLong())));
            }
            itemMeta.setLore(lore);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack createForgeSelectionMenuSlotItem(ForgeItem forgeItem) {
        ItemStack itemStack;
        if (forgeItem.getItem() == null) {
            itemStack = PickaxeType.generatePickaxe(forgeItem.getPickaxeType());
        } else {
            itemStack = Item.generateItem(forgeItem.getItem(), 1);
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        ArrayList<String> lore = new ArrayList<>(itemMeta.getLore());
        lore.add("");
        for (ForgeItemComponent forgeItemComponent : forgeItem.getForgeItemRequirement().getComponentList()) {
            lore.add(ChatColor.WHITE + forgeItemComponent.getItem().getName() + " x" + forgeItemComponent.getAmount());
        }
        lore.add(ChatColor.RESET + "" + ChatColor.WHITE + "Click to forge");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack createConfirmMenuRequirementItem(Item item, int amount) {
        ItemStack itemStack = Item.generateItem(item, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(item.getName() + " x" + amount);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack createConfirmDenyItem() {
        return createItemStack(Material.RED_STAINED_GLASS_PANE, "Stop and go back");
    }

    public boolean isForgeComplete(ForgeSlot forgeSlot) {
        return (forgeSlot.getForgeItem().getForgeTimeLong() + forgeSlot.getTimeStarted()) <= System.currentTimeMillis();
    }

    private ItemStack createBackgroundItem() {
        return createItemStack(Material.BLACK_STAINED_GLASS_PANE, " ");
    }

    private ItemStack createLockedForgeSlotItem() {
        return createItemStack(Material.RED_DYE, ChatColor.RED + "Slot Locked");
    }

    private ItemStack createAvailableForgeSlotItem() {
        return createItemStack(Material.LIME_DYE, ChatColor.GREEN + "Available Slot");
    }

    private ItemStack createAvailableForgeSelectionLocked() {
        return createItemStack(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "Forge Locked");
    }

    private ItemStack createBackArrow() {
        return createItemStack(Material.ARROW, "Back");
    }

    private ItemStack createItemStack(Material material, String displayName) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(displayName);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public String calculateTimeRemaining(long duration) {
        long seconds =  Math.abs((duration / 1000) % 60);
        long minutes =  Math.abs((duration / (1000*60)) % 60);
        long hours   =  Math.abs((duration / (1000*60*60)) % 24);
        long days =  Math.abs((duration / (1000*60*60*24)) % 7);
        String toReturn = "";
        if (days > 0) {
            toReturn = days + "d " + hours + "h " + minutes + "m " + seconds + "s";
        } else if (hours > 0) {
            toReturn = hours + "h " + minutes + "m " + seconds + "s";
        } else if (minutes > 0) {
            toReturn = minutes + "m " + seconds + "s";
        } else {
            toReturn = seconds + "s";
        }
        return ChatColor.GREEN + toReturn;
    }
}
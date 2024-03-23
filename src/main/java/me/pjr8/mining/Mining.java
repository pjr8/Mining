package me.pjr8.mining;

import com.comphenix.packetwrapper.WrapperPlayServerBlockBreakAnimation;
import com.comphenix.packetwrapper.WrapperPlayServerBlockChange;
import com.comphenix.packetwrapper.WrapperPlayServerWorldEvent;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import me.pjr8.Main;
import me.pjr8.chat.Chat;
import me.pjr8.database.playerdata.PlayerDataHandler;
import me.pjr8.mining.enums.OreType;
import me.pjr8.mining.objects.BlockHolder;
import me.pjr8.mining.objects.BlockRespawn;
import me.pjr8.mining.objects.MiningPlayer;
import me.pjr8.mining.objects.OreDropTable;
import me.pjr8.update.UpdateEvent;
import me.pjr8.update.UpdateType;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Mining implements Listener {

    private final PlayerDataHandler playerDataHandler;
    private final ProtocolManager protocolManager;
    private final JavaPlugin plugin;
    private final HashMap<Player, MiningPlayer> miningPlayers = new HashMap<>();
    private final HashMap<Player, Location> miningPlayerCurrentBlock = new HashMap<>();
    private final HashMap<Player, ArrayList<BlockRespawn>> playerBlockRespawn = new HashMap<>();

    public Mining(PlayerDataHandler playerDataHandler, ProtocolManager protocolManager, JavaPlugin plugin) {
        this.playerDataHandler = playerDataHandler;
        this.protocolManager = protocolManager;
        this.plugin = plugin;
        miningPacketReceiver();
    }

    public void miningPacketReceiver() {
        protocolManager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.HIGHEST, PacketType.Play.Client.BLOCK_DIG) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                PacketContainer packet = event.getPacket();
                if (Main.adminMode.contains(player)) {
                    return;
                }

                event.setCancelled(true);
                if (packet.getPlayerDigTypes().read(0) == EnumWrappers.PlayerDigType.START_DESTROY_BLOCK) {
                    MiningPlayer miningPlayer = miningPlayers.get(player);
                    BlockPosition blockPosition = packet.getBlockPositionModifier().read(0);
                    Block block = Bukkit.getWorld(player.getWorld().getName()).getBlockAt(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
                    BlockHolder blockHolder = miningPlayer.getCurrentOres().get(block.getLocation());
                    ArrayList<BlockRespawn> blockRespawnArrayList = playerBlockRespawn.get(player);
                    if (blockRespawnArrayList != null) {
                        for (BlockRespawn blockRespawn : playerBlockRespawn.get(player)) {
                            if (blockRespawn.getBlock().getLocation() == block.getLocation()) {
                                sendPlayerBlockChange(player, block);
                                return;
                            }
                        }
                    }

                    if (blockHolder == null) {
                        OreType oreType = OreType.getOreTypeFromBlock(block);
                        if (oreType != null) {
                            blockHolder = new BlockHolder(oreType, System.currentTimeMillis());
                            miningPlayer.addCurrentOres(block.getLocation(), blockHolder);
                        } else {
                            player.sendMessage("Invalid block to mine.");
                            return;
                        }
                    }
                    miningPlayerCurrentBlock.put(player, block.getLocation());
                } else if (packet.getPlayerDigTypes().read(0) == EnumWrappers.PlayerDigType.STOP_DESTROY_BLOCK || packet.getPlayerDigTypes().read(0) == EnumWrappers.PlayerDigType.ABORT_DESTROY_BLOCK) {
                    miningPlayerCurrentBlock.remove(player);
                }
            }
        });
    }

    @EventHandler
    public void updatePlayersMining(UpdateEvent event) {
        if (event.getType() != UpdateType.TICK) {
            return;
        }
        for (Map.Entry<Player, Location> entry : miningPlayerCurrentBlock.entrySet()) {
            Player player = entry.getKey();
            Location location = entry.getValue();

            for (BlockRespawn blockRespawn : playerBlockRespawn.get(player)) {
                if (blockRespawn.getBlock() == location.getBlock()) {
                    return;
                }
            }

            MiningPlayer miningPlayer = miningPlayers.get(player);
            BlockHolder blockHolder = miningPlayer.getCurrentOres().get(location);

            if (blockHolder == null) {
                return;
            }

            blockHolder.setDurabilityMined(blockHolder.getDurabilityMined() + playerDataHandler.getPlayerDataHolder().get(player.getUniqueId()).getPickaxeData().calculatePickaxePower());

            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20, 100));

            if (blockHolder.getDurabilityMined() >= blockHolder.getOreType().getDurability()) {
                player.sendMessage("Successfully mined");
                sendPlayerBlockBrokenAnimation(player, location.getBlock());
                sendPlayerMiningBreakAnimationPacket(player, location.getBlock(), -1);
                sendPlayerBlockChange(player, location.getBlock());
                addPlayerBlockRespawn(player, new BlockRespawn(player, location.getBlock(), System.currentTimeMillis() + 2000L));
                miningPlayer.removeCurrentOre(location);
                String text = Chat.setGradient("COMPLETE!", Color.RED, Color.GREEN);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(text));
                spawnBlockDrops(player, location.getBlock(), OreType.getOreTypeFromBlock(location.getBlock()));
                return;
            } else {
                double percentage = (blockHolder.getDurabilityMined() / blockHolder.getOreType().getDurability()) * 10;
                if (percentage >= 9.5) {
                    percentage = 9;
                } else {
                    percentage = Math.round(percentage);
                }
                sendPlayerMiningBreakAnimationPacket(player, location.getBlock(), (int) percentage);
            }

            String text = Chat.setGradient(blockHolder.getDurabilityMined() + "/" + blockHolder.getOreType().getDurability(), Color.MAGENTA, Color.BLUE);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(text));

            miningPlayer.addCurrentOres(location, blockHolder);
        }
    }

    @EventHandler
    public void doPlayerBlockRespawn(UpdateEvent event) {
        if (event.getType() != UpdateType.FASTER) {
            return;
        }
        for (Map.Entry<Player, ArrayList<BlockRespawn>> entry : playerBlockRespawn.entrySet()) {
            Player player = entry.getKey();
            ArrayList<BlockRespawn> blockRespawnArrayList = entry.getValue();
            for (BlockRespawn blockRespawn : blockRespawnArrayList) {
                if (System.currentTimeMillis() >= blockRespawn.getRespawnTime()) {
                    player.sendBlockChange(blockRespawn.getBlock().getLocation(), blockRespawn.getBlock().getBlockData());
                    blockRespawnArrayList.remove(blockRespawn);
                    return;
                }
            }

        }

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        miningPlayers.put(player, new MiningPlayer(player));
        playerBlockRespawn.put(player, new ArrayList<BlockRespawn>());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        miningPlayers.remove(event.getPlayer());
    }

    public void sendPlayerMiningBreakAnimationPacket(Player player, Block block, int stage) {
        WrapperPlayServerBlockBreakAnimation animationWrapped = new WrapperPlayServerBlockBreakAnimation();
        animationWrapped.setEntityID(miningPlayers.get(player).getCurrentOres().get(block.getLocation()).getEntityID());
        animationWrapped.setLocation(new BlockPosition(block.getX(), block.getY(), block.getZ()));
        animationWrapped.setDestroyStage(stage);
        protocolManager.sendServerPacket(player, animationWrapped.getHandle());
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            event.setCancelled(true);
            return;
        }
        UUID uuid = event.getItem().getOwner();
        if (uuid != null) {
            if (!uuid.equals(player.getUniqueId())) {
                //event.setCancelled(true);
            }
        }
    }

    public void sendPlayerBlockBrokenAnimation(Player player, Block block) {
        WrapperPlayServerWorldEvent animationWrapped = new WrapperPlayServerWorldEvent();
        animationWrapped.setEffectId(2001);
        animationWrapped.setLocation(new BlockPosition(block.getX(), block.getY(), block.getZ()));
        animationWrapped.setData(1);
        animationWrapped.setDisableRelativeVolume(false);
        protocolManager.sendServerPacket(player, animationWrapped.getHandle());
    }

    public void sendPlayerBlockChange(Player player, Block block) {
        WrapperPlayServerBlockChange animationWrapped = new WrapperPlayServerBlockChange();
        animationWrapped.setBlockData(WrappedBlockData.createData(Material.BEDROCK));
        animationWrapped.setLocation(new BlockPosition(block.getX(), block.getY(), block.getZ()));
        protocolManager.sendServerPacket(player, animationWrapped.getHandle());
    }

    public void addPlayerBlockRespawn(Player player, BlockRespawn blockRespawn) {
        ArrayList<BlockRespawn> blockRespawnArrayList = playerBlockRespawn.get(player);
        blockRespawnArrayList.add(blockRespawn);
        playerBlockRespawn.put(player, blockRespawnArrayList);
    }

    public void spawnBlockDrops(Player player, Block block, OreType oreType) {
        for (ItemStack itemStack : OreDropTable.getDrops(oreType)) {
            Item item = block.getWorld().spawn(new Location(block.getWorld(), block.getX() + 0.5, block.getY() + 1, block.getZ() + 0.5), Item.class);
            item.setItemStack(itemStack);
            item.setVelocity(new Vector(0f, 0.25f, 0f));
            item.setOwner(player.getUniqueId());
            item.setVisibleByDefault(false);
            player.showEntity(plugin, item);
            ;
        }
    }
}

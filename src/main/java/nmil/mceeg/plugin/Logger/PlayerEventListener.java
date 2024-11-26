package nmil.mceeg.plugin.Logger;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

import nmil.mceeg.plugin.type.*;

public class PlayerEventListener implements Listener {
    private final EventListenerCallback callback;

    public PlayerEventListener(EventListenerCallback callback) {
        this.callback = callback;
    }

    // Block break event
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Location location = event.getBlock().getLocation();
        Map<String, Object> eventInfo = new HashMap<>();
        eventInfo.put("player", player.getName());
        eventInfo.put("event", PlayerEventType.block_break_event);
        eventInfo.put("block_type", event.getBlock().getType());
        eventInfo.put("block_location", "x=" + location.getBlockX() + ", y=" + location.getBlockY() + ", z=" + location.getBlockZ());
        callback.handleEventInfo(player, eventInfo, "event-block");
    }

    // Block damage event
    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getItemInHand();
        Location location = event.getBlock().getLocation();
        Map<String, Object> eventInfo = new HashMap<>();
        eventInfo.put("player", player.getName());
        eventInfo.put("event", PlayerEventType.block_damage_event);
        eventInfo.put("block_type", event.getBlock().getType());
        eventInfo.put("block_location", "x=" + location.getBlockX() + ", y=" + location.getBlockY() + ", z=" + location.getBlockZ());
        eventInfo.put("item_in_hand", itemInHand != null ? itemInHand.getType() : "none");
        callback.handleEventInfo(player, eventInfo, "event-block");
    }

    // Block place event
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Location location = event.getBlockPlaced().getLocation();
        Map<String, Object> eventInfo = new HashMap<>();
        eventInfo.put("player", player.getName());
        eventInfo.put("event", PlayerEventType.block_place_event);
        eventInfo.put("block_type", event.getBlock().getType());
        eventInfo.put("block_location", "x=" + location.getBlockX() + ", y=" + location.getBlockY() + ", z=" + location.getBlockZ());
        callback.handleEventInfo(player, eventInfo, "event-block");
    }

    // Player interaction event
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        Block clickedBlock = event.getClickedBlock();
        ItemStack itemInHand = event.getItem();
        Map<String, Object> eventInfo = new HashMap<>();
        eventInfo.put("player", player.getName());
        eventInfo.put("event", PlayerEventType.player_interact_event);
        eventInfo.put("action", action);
        eventInfo.put("item_in_hand", itemInHand != null ? itemInHand.getType() : "none");
        if (clickedBlock != null) {
            Location location = clickedBlock.getLocation();
            eventInfo.put("block_type", clickedBlock.getType());
            eventInfo.put("block_location", "x=" + location.getBlockX() + ", y=" + location.getBlockY() + ", z=" + location.getBlockZ());
        }
        callback.handleEventInfo(player, eventInfo, "event-interact");
    }

    // Player interacts with entity event
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Map<String, Object> eventInfo = new HashMap<>();
        eventInfo.put("player", player.getName());
        eventInfo.put("event", PlayerEventType.player_interact_entity_event);
        eventInfo.put("entity_type", event.getRightClicked().getType());
        callback.handleEventInfo(player, eventInfo, "event-interact");
    }

    // Player shears entity event
    @EventHandler
    public void onPlayerShearEntity(PlayerShearEntityEvent event) {
        Player player = event.getPlayer();
        Map<String, Object> eventInfo = new HashMap<>();
        eventInfo.put("player", player.getName());
        eventInfo.put("event", PlayerEventType.player_shear_entity_event);
        eventInfo.put("entity_type", event.getEntity().getType());
        callback.handleEventInfo(player, eventInfo, "event-interact");
    }

    // Player attacks entity event
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            Map<String, Object> eventInfo = new HashMap<>();
            eventInfo.put("player", player.getName());
            eventInfo.put("event", PlayerEventType.entity_damage_by_entity_event);
            eventInfo.put("entity_type", event.getEntity().getType());
            callback.handleEventInfo(player, eventInfo, "event-fight");
        }
    }

    // Player gets hurt event
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Map<String, Object> eventInfo = new HashMap<>();
            eventInfo.put("player", player.getName());
            eventInfo.put("event", PlayerEventType.entity_damage_event);
            eventInfo.put("cause", event.getCause());
            callback.handleEventInfo(player, eventInfo, "event-fight");
        }
    }

    // Player consumes item event
    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        Map<String, Object> eventInfo = new HashMap<>();
        eventInfo.put("player", player.getName());
        eventInfo.put("event", PlayerEventType.player_item_consume_event);
        eventInfo.put("item_type", event.getItem().getType());
        callback.handleEventInfo(player, eventInfo, "event-item");
    }

    // Player changes held item event
    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        ItemStack newItem = inventory.getItem(event.getNewSlot());
        Map<String, Object> eventInfo = new HashMap<>();
        eventInfo.put("player", player.getName());
        eventInfo.put("event", PlayerEventType.player_item_held_event);
        eventInfo.put("new_item", newItem != null ? newItem.getType().toString() : "none");
        callback.handleEventInfo(player, eventInfo, "event-item");
    }

    // Player drops item event
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Map<String, Object> eventInfo = new HashMap<>();
        eventInfo.put("player", player.getName());
        eventInfo.put("event", PlayerEventType.player_drop_item_event);
        eventInfo.put("item_type", event.getItemDrop().getItemStack().getType());
        callback.handleEventInfo(player, eventInfo, "event-item");
    }

    // Player picks up item event
    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        Map<String, Object> eventInfo = new HashMap<>();
        eventInfo.put("player", player.getName());
        eventInfo.put("event", PlayerEventType.player_pickup_item_event);
        eventInfo.put("item_type", event.getItem().getItemStack().getType());
        callback.handleEventInfo(player, eventInfo, "event-item");
    }

    // Player interacts with inventory event
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            Map<String, Object> eventInfo = new HashMap<>();
            eventInfo.put("player", player.getName());
            eventInfo.put("event", PlayerEventType.inventory_click_event);
            eventInfo.put("slot", event.getSlot());
            callback.handleEventInfo(player, eventInfo, "event-item");
        }
    }

    @EventHandler
    public void onPlayerCraftItem(CraftItemEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            ItemStack craftedItem = event.getCurrentItem();
            Map<String, Object> eventInfo = new HashMap<>();
            eventInfo.put("player", player.getName());
            eventInfo.put("event", PlayerEventType.craft_item_event);
            eventInfo.put("crafted_item", craftedItem != null ? craftedItem.getType().toString() + " x" + craftedItem.getAmount() : "unknown item");
            callback.handleEventInfo(player, eventInfo, "event-item");
        }
    }
}

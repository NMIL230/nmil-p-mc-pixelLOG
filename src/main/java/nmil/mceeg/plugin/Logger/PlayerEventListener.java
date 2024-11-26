package nmil.mceeg.plugin.Logger;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
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

    private Map<String, Object> createLocationJson(Location location) {
        Map<String, Object> loc = new HashMap<>();
        loc.put("x", location.getBlockX());
        loc.put("y", location.getBlockY());
        loc.put("z", location.getBlockZ());
        return loc;
    }

    private Map<String, Object> createItemJson(ItemStack item) {
        if (item != null) {
            Map<String, Object> itemJson = new HashMap<>();
            itemJson.put("type", item.getType().toString());
            itemJson.put("amount", item.getAmount());
            return itemJson;
        }
        return null;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Map<String, Object> eventInfo = new HashMap<>();
        eventInfo.put("player", player.getName());
        eventInfo.put("event", PlayerEventType.block_break_event);

        Map<String, Object> info = new HashMap<>();
        info.put("block_type", event.getBlock().getType());
        info.put("block_location", createLocationJson(event.getBlock().getLocation()));
        eventInfo.put("info", info);

        callback.handleEventInfo(player, eventInfo, "event-block");
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        Player player = event.getPlayer();
        Map<String, Object> eventInfo = new HashMap<>();
        eventInfo.put("player", player.getName());
        eventInfo.put("event", PlayerEventType.block_damage_event);

        Map<String, Object> info = new HashMap<>();
        info.put("block_type", event.getBlock().getType());
        info.put("block_location", createLocationJson(event.getBlock().getLocation()));
        info.put("item_in_hand", createItemJson(player.getItemInHand()));
        eventInfo.put("info", info);

        callback.handleEventInfo(player, eventInfo, "event-block");
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Map<String, Object> eventInfo = new HashMap<>();
        eventInfo.put("player", player.getName());
        eventInfo.put("event", PlayerEventType.block_place_event);

        Map<String, Object> info = new HashMap<>();
        info.put("block_type", event.getBlock().getType());
        info.put("block_location", createLocationJson(event.getBlockPlaced().getLocation()));
        eventInfo.put("info", info);

        callback.handleEventInfo(player, eventInfo, "event-block");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Map<String, Object> eventInfo = new HashMap<>();
        eventInfo.put("player", player.getName());
        eventInfo.put("event", PlayerEventType.player_interact_event);

        Map<String, Object> info = new HashMap<>();
        info.put("action", event.getAction());
        info.put("item_in_hand", createItemJson(event.getItem()));
        if (event.getClickedBlock() != null) {
            info.put("block_type", event.getClickedBlock().getType());
            info.put("block_location", createLocationJson(event.getClickedBlock().getLocation()));
        }
        eventInfo.put("info", info);

        callback.handleEventInfo(player, eventInfo, "event-interact");
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Map<String, Object> eventInfo = new HashMap<>();
        eventInfo.put("player", player.getName());
        eventInfo.put("event", PlayerEventType.player_interact_entity_event);

        Map<String, Object> info = new HashMap<>();
        info.put("entity_type", event.getRightClicked().getType());
        eventInfo.put("info", info);

        callback.handleEventInfo(player, eventInfo, "event-interact");
    }

    @EventHandler
    public void onPlayerShearEntity(PlayerShearEntityEvent event) {
        Player player = event.getPlayer();
        Map<String, Object> eventInfo = new HashMap<>();
        eventInfo.put("player", player.getName());
        eventInfo.put("event", PlayerEventType.player_shear_entity_event);

        Map<String, Object> info = new HashMap<>();
        info.put("entity_type", event.getEntity().getType());
        eventInfo.put("info", info);

        callback.handleEventInfo(player, eventInfo, "event-interact");
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            Map<String, Object> eventInfo = new HashMap<>();
            eventInfo.put("player", player.getName());
            eventInfo.put("event", PlayerEventType.entity_damage_by_entity_event);

            Map<String, Object> info = new HashMap<>();
            info.put("entity_type", event.getEntity().getType());
            info.put("damage", event.getDamage());
            eventInfo.put("info", info);

            callback.handleEventInfo(player, eventInfo, "event-fight");
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Map<String, Object> eventInfo = new HashMap<>();
            eventInfo.put("player", player.getName());
            eventInfo.put("event", PlayerEventType.entity_damage_event);

            Map<String, Object> info = new HashMap<>();
            info.put("cause", event.getCause());
            info.put("damage", event.getDamage());
            eventInfo.put("info", info);

            callback.handleEventInfo(player, eventInfo, "event-fight");
        }
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        Map<String, Object> eventInfo = new HashMap<>();
        eventInfo.put("player", player.getName());
        eventInfo.put("event", PlayerEventType.player_item_consume_event);

        Map<String, Object> info = new HashMap<>();
        info.put("item", createItemJson(event.getItem()));
        eventInfo.put("info", info);

        callback.handleEventInfo(player, eventInfo, "event-item");
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        ItemStack newItem = inventory.getItem(event.getNewSlot());

        Map<String, Object> eventInfo = new HashMap<>();
        eventInfo.put("player", player.getName());
        eventInfo.put("event", PlayerEventType.player_item_held_event);

        Map<String, Object> info = new HashMap<>();
        info.put("new_item", createItemJson(newItem));
        eventInfo.put("info", info);

        callback.handleEventInfo(player, eventInfo, "event-item");
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Map<String, Object> eventInfo = new HashMap<>();
        eventInfo.put("player", player.getName());
        eventInfo.put("event", PlayerEventType.player_drop_item_event);

        Map<String, Object> info = new HashMap<>();
        ItemStack droppedItem = event.getItemDrop().getItemStack();
        info.put("item_type", droppedItem.getType().toString());
        info.put("amount", droppedItem.getAmount());
        eventInfo.put("info", info);

        callback.handleEventInfo(player, eventInfo, "event-item");
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        Map<String, Object> eventInfo = new HashMap<>();
        eventInfo.put("player", player.getName());
        eventInfo.put("event", PlayerEventType.player_pickup_item_event);

        Map<String, Object> info = new HashMap<>();
        ItemStack pickedUpItem = event.getItem().getItemStack();
        info.put("item_type", pickedUpItem.getType().toString());
        info.put("amount", pickedUpItem.getAmount());
        eventInfo.put("info", info);

        callback.handleEventInfo(player, eventInfo, "event-item");
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            Map<String, Object> eventInfo = new HashMap<>();
            eventInfo.put("player", player.getName());
            eventInfo.put("event", PlayerEventType.inventory_click_event);

            Map<String, Object> info = new HashMap<>();
            info.put("slot", event.getSlot());
            info.put("clicked_item", event.getCurrentItem() != null ? event.getCurrentItem().getType().toString() : "none");
            eventInfo.put("info", info);

            callback.handleEventInfo(player, eventInfo, "event-item");
        }
    }

    @EventHandler
    public void onPlayerCraftItem(CraftItemEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            Map<String, Object> eventInfo = new HashMap<>();
            eventInfo.put("player", player.getName());
            eventInfo.put("event", PlayerEventType.craft_item_event);

            Map<String, Object> info = new HashMap<>();
            ItemStack craftedItem = event.getCurrentItem();
            if (craftedItem != null) {
                info.put("crafted_item", craftedItem.getType().toString());
                info.put("amount", craftedItem.getAmount());
            } else {
                info.put("crafted_item", "unknown item");
                info.put("amount", 0);
            }
            eventInfo.put("info", info);

            callback.handleEventInfo(player, eventInfo, "event-item");
        }
    }

}

package nmil.mceeg.plugin.Logger;

import nmil.mceeg.plugin.type.LogInfoType;
import nmil.mceeg.plugin.type.LogType;
import nmil.mceeg.plugin.util.UTCDatetime;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.*;

public class ObservationSpaceGetter {
    private final Logger logger;

    public ObservationSpaceGetter( Logger logger) {
        this.logger = logger;
    }
    public Map<String, Object> getPlayerObservationSpace(Player player, LogType type, Map<String, Object> event) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        //sdf.format(new Date())
        Map<String, Object> data = new HashMap<>();
        //data.put("Type", LogType.EVENT_LOG);
        data.put("event", null);
        data.put("time", UTCDatetime.getUTCDatetime());
        data.put("game_tick", player.getWorld().getGameTime());
        switch (type) {
            case FULL:
                if (event != null) {
                    data.put(String.valueOf(LogInfoType.event), event);
                    data.put(String.valueOf(LogInfoType.health),(int) player.getHealth());
                    data.put(String.valueOf(LogInfoType.hunger), player.getFoodLevel());
                    data.put(String.valueOf(LogInfoType.location), player.getLocation().toVector());
                    data.put(String.valueOf(LogInfoType.view), getPlayerView(player));
                    data.put(String.valueOf(LogInfoType.target_block),getPlayerTargetBlock(player));
                    data.put(String.valueOf(LogInfoType.ray_trace_entities),getPlayerRayTraceEntity(player));
                    data.put(String.valueOf(LogInfoType.nearby_entities),getNearbyEntities(player));
                    data.put(String.valueOf(LogInfoType.hot_bar),getPlayerHotbar(player));
                    data.put(String.valueOf(LogInfoType.nearby_blocks),getNearbyBlocks(player));
                    data.put(String.valueOf(LogInfoType.biome),getPlayerBiome(player));
                    data.put(String.valueOf(LogInfoType.inventory), getSimpleItemStacks(player.getInventory().getContents()));
                    data.put(String.valueOf(LogInfoType.equipments),getPlayerEquipment(player));
                }
                break;
            case HIGH_FREQUENCY_LOG:
               // data.put("Health",(int) player.getHealth());
               // data.put("Hunger", player.getFoodLevel());
                 data.put(String.valueOf(LogInfoType.type), LogType.HIGH_FREQUENCY_LOG);
                data.put(String.valueOf(LogInfoType.location), player.getLocation().toVector());
                data.put(String.valueOf(LogInfoType.view), getPlayerView(player));
                //data.put(String.valueOf(LogInfoType.target_block),getPlayerTargetBlock(player));
                data.put(String.valueOf(LogInfoType.ray_trace_block), getPlayerTargetBlockExactLocationAndType(player));
                data.put(String.valueOf(LogInfoType.ray_trace_entities),getPlayerRayTraceEntity(player));
               // data.put("NearbyEntities",getNearbyEntities(player));
                //data.put("Hot-bar",getPlayerHotbar(player));
                break;

            case LOW_FREQUENCY_LOG:
                data.put("Type", LogType.LOW_FREQUENCY_LOG);
                data.put("NearbyBlocks",getNearbyBlocks(player));
                data.put("Biome",getPlayerBiome(player));
                break;

            case EVENT_LOG:
                if (event != null) {
                    data.put("Event", event);
                    data.put(String.valueOf(LogInfoType.location), player.getLocation().toVector());
                    data.put(String.valueOf(LogInfoType.target_block),getPlayerTargetBlock(player));
                    data.put(String.valueOf(LogInfoType.inventory), getSimpleItemStacks(player.getInventory().getContents()));
                    // data.put(String.valueOf(LogInfoType.nearby_blocks),getNearbyBlocks(player));
                    data.put(String.valueOf(LogInfoType.nearby_entities),getNearbyEntities(player));
                    data.put(String.valueOf(LogInfoType.ray_trace_entities),getPlayerRayTraceEntity(player));
                    data.put(String.valueOf(LogInfoType.health),(int) player.getHealth());
                    data.put(String.valueOf(LogInfoType.hunger), player.getFoodLevel());
                    data.put(String.valueOf(LogInfoType.biome),getPlayerBiome(player));


//                    data.put("TargetEntity",getPlayerTargetEntity(player));
                }
                break;
            default:
                data.put("null", "null");
                break;
        }
        return data;
    }
    public Map<String, Object>  getPlayerTargetBlockExactLocationAndType(Player player) {
        World world = player.getWorld();
        Location eyeLocation = player.getEyeLocation();
        Vector direction = eyeLocation.getDirection();
        double maxDistance = logger.getMAX_TARGET_DISTANCE();

        if (!world.isChunkLoaded(eyeLocation.getBlockX() >> 4, eyeLocation.getBlockZ() >> 4)) {
            return null;
        }

        RayTraceResult result = world.rayTraceBlocks(eyeLocation, direction, maxDistance);
        Map<String, Object> rayTrace = new HashMap<>();

        if (result != null && result.getHitBlock() != null) {
            Location hitLocation = result.getHitPosition().toLocation(world);

            rayTrace.put("hit_location", hitLocation.toVector());
            rayTrace.put("block_type", result.getHitBlock().getType().toString().toLowerCase());

            return rayTrace;
        }
        return null;
    }

    public  Map<String, Object>  getPlayerRayTraceEntity(Player player) {
        World world = player.getWorld();
        Location eyeLocation = player.getEyeLocation();

        Vector direction = player.getEyeLocation().getDirection();
        Location start = player.getEyeLocation();
        double maxDistance = logger.getMAX_TARGET_DISTANCE();
        if (!world.isChunkLoaded(eyeLocation.getBlockX() >> 4, eyeLocation.getBlockZ() >> 4)) {
            return null;
        }
        RayTraceResult result = player.getWorld().rayTraceEntities(start, direction, maxDistance, 0.0,
                entity -> entity != player);
        Map<String, Object> rayTrace = new HashMap<>();

        if (result != null && result.getHitEntity() != null) {
            Entity hitEntity = result.getHitEntity();

            rayTrace.put("hit_location", hitEntity.getLocation().toVector());
            rayTrace.put("entity", hitEntity.getType().toString().toLowerCase());

            return rayTrace;
        }

        return null;
    }
    public Map<String, Float> getPlayerView(Player player) {
        Map<String, Float> view = new HashMap<>();
        Location viewLocation = player.getLocation();
        view.put("yaw", viewLocation.getYaw());
        view.put("pitch", viewLocation.getPitch());
        return view;
    }
    public List<Map<String, Object>> getSimpleItemStacks(ItemStack[] contents) {
        List<Map<String, Object>> simpleItems = new ArrayList<>();
        for (ItemStack item : contents) {
            if (item != null) {
                Map<String, Object> itemData = new HashMap<>();
                itemData.put("type", item.getType().toString());
                itemData.put("amount", item.getAmount());
                simpleItems.add(itemData);
            }
        }
        return simpleItems;
    }
    public List<Map<String, Object>> getPlayerHotbar(Player player) {
        List<Map<String, Object>> hotbarItems = new ArrayList<>();
        ItemStack[] contents = player.getInventory().getContents();

        for (int i = 0; i < 9; i++) {
            ItemStack item = contents[i];
            Map<String, Object> itemData = new HashMap<>();

            if (item != null) {
                itemData.put("slot", i + 1);
                itemData.put("type", item.getType().toString());
                itemData.put("amount", item.getAmount());
            }
//            else {
//                itemData.put("type", "None");
//                itemData.put("amount", 0);
//            }
            hotbarItems.add(itemData);
        }
        return hotbarItems;
    }
    public Map<String,Object> getPlayerTargetBlock(Player player) {
        Map<String,Object> block = new HashMap<>();
        Block targetBlock = player.getTargetBlock(null, logger.getMAX_TARGET_DISTANCE());
        if (targetBlock.getType() != Material.AIR) {
            Location blockLocation = (targetBlock).getLocation();
            block.put("block_type", targetBlock.getType().toString().toLowerCase());
            block.put("location", blockLocation.toVector());

            return block;
        }
        return null;
    }
//    public String getPlayerTargetEntity(Player player) {
//        List<Entity> nearbyEntities = player.getNearbyEntities(hawkeye.getMAX_TARGET_DISTANCE(), hawkeye.getMAX_TARGET_DISTANCE(), hawkeye.getMAX_TARGET_DISTANCE());
//        Vector playerDirection = player.getLocation().getDirection();
//        Location playerLocation = player.getEyeLocation();
//
//        for (int i = 1; i <= hawkeye.getMAX_TARGET_DISTANCE(); i++) {
//            Location point = playerLocation.add(playerDirection.multiply(i));
////            getLogger().info("Hawkeye: Checking point at " + point);
//
//            for (Entity entity : nearbyEntities) {
//                if (entity.getBoundingBox().contains(point.toVector())) {
////                    getLogger().info("Hawkeye: nearbyEntities " + entity.getType().toString().toLowerCase());
//                    return entity.getType().toString().toLowerCase();
//                }
//            }
//            playerDirection = player.getLocation().getDirection();
//        }
//        return null;
//    }


    public Map<String, String> getNearbyBlocks(Player player) {
        Map<String,String> blocks = new HashMap<>();
        Location location = player.getLocation();
        int radius = logger.getOBSERVATION_RADIUS();
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block block = Objects.requireNonNull(location.getWorld()).getBlockAt(location.add(x, y, z));
                    Location blockLocation = block.getLocation();
                    String locationString = "[x=" + blockLocation.getBlockX() + ", y=" + blockLocation.getBlockY() + ", z=" + blockLocation.getBlockZ()+ "]";
                    blocks.put(block.getType().toString().toLowerCase(), locationString);
                }
            }
        }
        return blocks;
    }
    public Set<String> getNearbyEntities(Player player) {
        Set<String> entities = new HashSet<>();
        int radius = logger.getOBSERVATION_RADIUS();
        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            entities.add(entity.getType().toString().toLowerCase());
        }
        return entities;
    }
    public Map<String, String> getPlayerEquipment(Player player) {
        Map<String, String> equipment = new HashMap<>();
        EntityEquipment playerEquipment = player.getEquipment();
        if (playerEquipment != null) {
            if (playerEquipment.getHelmet() != null)
                equipment.put("helmet", playerEquipment.getHelmet().getType().toString().toLowerCase());
            if (playerEquipment.getChestplate() != null)
                equipment.put("chestplate", playerEquipment.getChestplate().getType().toString().toLowerCase());
            if (playerEquipment.getLeggings() != null)
                equipment.put("leggings", playerEquipment.getLeggings().getType().toString().toLowerCase());
            if (playerEquipment.getBoots() != null)
                equipment.put("boots", playerEquipment.getBoots().getType().toString().toLowerCase());
            playerEquipment.getItemInMainHand();
            equipment.put("main_hand", playerEquipment.getItemInMainHand().getType().toString().toLowerCase());
            playerEquipment.getItemInOffHand();
            equipment.put("off_hand", playerEquipment.getItemInOffHand().getType().toString().toLowerCase());
        }
        return equipment;
    }
    public String getPlayerBiome(Player player) {
        Biome biome = player.getWorld().getBiome(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
        return biome.toString().toLowerCase();
    }

}

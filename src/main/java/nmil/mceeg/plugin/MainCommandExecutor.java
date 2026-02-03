package nmil.mceeg.plugin;

import nmil.mceeg.plugin.util.VersionControl;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.bukkit.ChatColor.*;

public class MainCommandExecutor implements CommandExecutor {

    private final MainPluginCallback callback;

    public MainCommandExecutor(MainPluginCallback callback) {
        this.callback = callback;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(RED + "This command can only be run by a player.");
            return true;
        }

        Player player = (Player) sender;
        switch (label.toLowerCase()) {
            case "dstart":
                handleRealTimePlStart(player);
                break;
            case "dend":
                handleRealTimePlStop(player);
                break;
            case "pl-start":
                handlePlStart(player);
                break;
            case "pl-stop":
                handlePlStop(player);
                break;
            case "pl-start-op":
                handlePlStartOp(player, args);
                break;
            case "pl-stop-op":
                handlePlStopOp(player, args);
                break;
            case "pl-version":
                handlePlVersion(player);
                break;

            case "dreset":
                handleReset(player);
                break;

            default:
                player.sendMessage(RED + "Unknown command.");
                return false;
        }

        return true;
    }

    private void handleRealTimePlStart(Player player) {
        player.sendMessage(GREEN + "Started AI Analysis");
        callback.getNmilPlayer(player).startRealTimeCollectingLogsOnPlayer();
    }

    private void handleRealTimePlStop(Player player) {
        player.sendMessage(GREEN + "Stopped AI Analysis");
        callback.getNmilPlayer(player).stopRealTimeCollectingLogsOnPlayer();
    }

    private void handlePlStart(Player player) {
        player.sendMessage(GREEN + "Started collecting logs.");
        callback.getNmilPlayer(player).startCollectingLogFileOnPlayer();
    }

    private void handlePlStop(Player player) {
        player.sendMessage(GREEN + "Stopped collecting logs.");
        callback.getNmilPlayer(player).stopCollectingLogFileOnPlayer();
    }

    private void handlePlStartOp(Player player, String[] args) {
        if (!player.isOp()) {
            player.sendMessage(RED + "You must be an OP to use this command.");
            return;
        }
        if (args.length < 1) {
            player.sendMessage(RED + "Usage: /pl-start-op [username]");
            return;
        }
        String targetName = args[0];
        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            player.sendMessage(RED + "Player " + targetName + " is not online.");
            return;
        }

        callback.getNmilPlayer(target).startCollectingLogFileOnPlayer();
        player.sendMessage(GREEN + "Started collecting logs for " + targetName + ".");
    }

    private void handlePlStopOp(Player player, String[] args) {
        if (!player.isOp()) {
            player.sendMessage(RED + "You must be an OP to use this command.");
            return;
        }
        if (args.length < 1) {
            player.sendMessage(RED + "Usage: /pl-stop-op [username]");
            return;
        }
        String targetName = args[0];
        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            player.sendMessage(RED + "Player " + targetName + " is not online.");
            return;
        }

        callback.getNmilPlayer(target).stopCollectingLogFileOnPlayer();
        player.sendMessage(GREEN + "Stopped collecting logs for " + targetName + ".");
    }

    private void handlePlVersion(Player player) {
        player.sendMessage(AQUA + "Plugin version: " + VersionControl.getPluginVersion());
    }


    private void handleReset(Player player) {
        player.sendMessage(YELLOW + "[Reset] loading bingo-world...");

        String nickname = player.getName().toLowerCase();
        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String newWorldName = "bingo-world_" + nickname + "_" + datetime;

        Path serverFolder = Bukkit.getWorldContainer().toPath();
        Path sourcePath = serverFolder.resolve("bingo-world");
        Path targetPath = serverFolder.resolve(newWorldName);

        try {
            Files.walk(sourcePath).forEach(src -> {
                Path dst = targetPath.resolve(sourcePath.relativize(src));
                try {
                    if (Files.isDirectory(src)) {
                        if (!Files.exists(dst)) {
                            Files.createDirectories(dst);
                        }
                    } else {
                        Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    player.sendMessage(RED + "[Reset] error: " + e.getMessage());
                    e.printStackTrace();
                }
            });

            Files.deleteIfExists(targetPath.resolve("uid.dat"));

        } catch (IOException e) {
            player.sendMessage(RED + "[Reset] world error: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        World newWorld = Bukkit.createWorld(new WorldCreator(newWorldName));
        if (newWorld == null) {
            player.sendMessage(RED + "[Reset] load world failed.");
            return;
        }
        newWorld.setAutoSave(false);

        newWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        newWorld.setStorm(false);
        newWorld.setThundering(false);

         
        newWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        newWorld.setTime(0);   
        Location spawnLoc = new Location(newWorld, -1028, 66, -1460);
        player.teleport(spawnLoc);

        player.getInventory().clear();
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setSaturation(20F);
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
        ItemStack ironAxe = new ItemStack(Material.IRON_AXE);
        ItemStack ironPickaxe = new ItemStack(Material.IRON_PICKAXE);
        ItemStack ironSword = new ItemStack(Material.IRON_SWORD);
        ItemStack ironShovel = new ItemStack(Material.IRON_SHOVEL);

        PlayerInventory inventory = player.getInventory();
        inventory.addItem(ironAxe);
        inventory.addItem(ironPickaxe);
        inventory.addItem(ironSword);
        inventory.addItem(ironShovel);
        player.sendMessage(GREEN + "[Reset] Welcome to PixelBingo!");
    }
}




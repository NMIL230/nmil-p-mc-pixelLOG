package nmil.mceeg.plugin;

import nmil.mceeg.plugin.util.VersionControl;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        // 保证执行者是玩家
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

            // 新增指令 reset
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

        // 准备新的目标世界名：bingo-world_玩家名_时间戳
        String nickname = player.getName().toLowerCase();
        String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String newWorldName = "bingo-world_" + nickname + "_" + datetime;

        // 拼接路径
        Path serverFolder = Bukkit.getWorldContainer().toPath();
        Path sourcePath = serverFolder.resolve("bingo-world");
        Path targetPath = serverFolder.resolve(newWorldName);

        // 复制 bingo-world 到新目录
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

            // 删除 uid.dat 防止世界冲突
            Files.deleteIfExists(targetPath.resolve("uid.dat"));

        } catch (IOException e) {
            player.sendMessage(RED + "[Reset] world error: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // 创建并加载新世界
        World newWorld = Bukkit.createWorld(new WorldCreator(newWorldName));
        if (newWorld == null) {
            player.sendMessage(RED + "[Reset] load world failed.");
            return;
        }
        newWorld.setAutoSave(false);
        // 重置新世界时间到早上（时间 0 为黎明）
        newWorld.setTime(0);

        // 获取新世界 (0, 最高方块Y+1, 0)
        Location spawnLoc = new Location(newWorld, -1028, 63, -1390);
        // 传送玩家
        player.teleport(spawnLoc);

        // 清空玩家背包
        player.getInventory().clear();
        // 恢复玩家状态：血量、饥饿值、饱和度，并移除所有药水效果
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setSaturation(20F);
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));

        player.sendMessage(GREEN + "[Reset] Welcome to PixelBingo!");
    }
}




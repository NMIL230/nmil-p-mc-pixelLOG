package nmil.mceeg.plugin;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static nmil.mceeg.plugin.util.VersionControl.getPluginVersion;

public class MainCommandExecutor implements CommandExecutor {

    private final MainPluginCallback callback;

    public MainCommandExecutor(MainPluginCallback callback) {
        this.callback = callback;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be run by a player.");
            return true;
        }

        Player player = (Player) sender;
        switch (label.toLowerCase()) {
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
            default:
                player.sendMessage(ChatColor.RED + "Unknown command.");
                return false;
        }

        return true;
    }

    private void handlePlStart(Player player) {
        player.sendMessage(ChatColor.GREEN + "Started collecting logs.");
        callback.getNmilPlayer(player).startCollectingLogFileOnPlayer();
    }

    private void handlePlStop(Player player) {
        player.sendMessage(ChatColor.GREEN + "Stopped collecting logs.");
        callback.getNmilPlayer(player).stopCollectingLogFileOnPlayer();

    }

    private void handlePlStartOp(Player player, String[] args) {
        if (!player.isOp()) {
            player.sendMessage(ChatColor.RED + "You must be an OP to use this command.");
            return;
        }

        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Usage: /pl-start-op [username]");
            return;
        }

        String targetName = args[0];
        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Player " + targetName + " is not online.");
            return;
        }

        // TODO: Implement logic to start collecting logs for the target player
        callback.getNmilPlayer(target).startCollectingLogFileOnPlayer();

        player.sendMessage(ChatColor.GREEN + "Started collecting logs for " + targetName + ".");
    }

    private void handlePlStopOp(Player player, String[] args) {
        if (!player.isOp()) {
            player.sendMessage(ChatColor.RED + "You must be an OP to use this command.");
            return;
        }

        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Usage: /pl-stop-op [username]");
            return;
        }

        String targetName = args[0];
        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Player " + targetName + " is not online.");
            return;
        }

        callback.getNmilPlayer(target).stopCollectingLogFileOnPlayer();
        player.sendMessage(ChatColor.GREEN + "Stopped collecting logs for " + targetName + ".");
    }

    private void handlePlVersion(Player player) {
        player.sendMessage(ChatColor.AQUA + "Plugin version: " + getPluginVersion());
    }
}

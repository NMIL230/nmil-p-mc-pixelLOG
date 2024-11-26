package nmil.mceeg.plugin;

import nmil.mceeg.plugin.player.NmilPlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.event.server.ServerListPingEvent;

public class MainListener implements Listener {
    private MainPluginCallback callback;

    public MainListener(MainPluginCallback callback) {
        this.callback = callback;
    }

    @EventHandler
    public void on(ServerListPingEvent event) {

        event.setMotd("§8» §6§lPixelLOG§7 V0.0.1 Enabled");
//        event.setMaxPlayers(250);
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setGameMode(GameMode.ADVENTURE);

        callback.playerJoinHandler(player);

    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        callback.playerQuitHandler(player);

    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
    }







}

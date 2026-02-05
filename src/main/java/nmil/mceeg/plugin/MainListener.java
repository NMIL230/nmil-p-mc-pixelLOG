package nmil.mceeg.plugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;

public class MainListener implements Listener {
    private MainPluginCallback callback;

    public MainListener(MainPluginCallback callback) {
        this.callback = callback;
    }

    @EventHandler
    public void on(ServerListPingEvent event) {
        event.setMotd("§8» §6§lPixelLOG§7 Enabled");
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
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

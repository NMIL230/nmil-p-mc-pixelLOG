package nmil.mceeg.plugin;
import nmil.mceeg.plugin.Logger.Logger;
import nmil.mceeg.plugin.player.PixelPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public interface MainPluginCallback {
    MainPlugin getMainPlugin();
    HashMap<Player, PixelPlayer> getOnlinePlayerMap();
    PixelPlayer getNmilPlayer(Player player);
    void playerJoinHandler(Player player);
    void playerQuitHandler(Player player);
    void playerRespawnHandler(Player player);

    void addLogToPlayer(Player player, Map<String, Object> log);
    Logger getPixelLogger();
    void sendSpigotLog(String msg);

    void sendWebSocketMessage(String json);
}

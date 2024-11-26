package nmil.mceeg.plugin;

import nmil.mceeg.plugin.Logger.Logger;
import nmil.mceeg.plugin.player.NmilPlayer;

import nmil.mceeg.plugin.type.LogType;
import nmil.mceeg.plugin.util.WebSocketServerController;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class MainPlugin extends JavaPlugin implements MainPluginCallback {

    HashMap<Player, NmilPlayer> onlinePlayerMap;


    MainListener mainListener;

    Logger logger;


    WebSocketServerController wsServerController;

    private MainCommandExecutor commandExecutor;



    @Override
    public void onEnable() {
        getLogger().info("PixelLOG initializing...");

        onlinePlayerMap = new HashMap<>();

        // Load all the Listeners
        this.mainListener = new MainListener(this);
        getServer().getPluginManager().registerEvents(mainListener, this);

        // Register the command executor
        commandExecutor = new MainCommandExecutor(this);
        this.getCommand("pl-version").setExecutor(commandExecutor);
        this.getCommand("pl-start").setExecutor(commandExecutor);
        this.getCommand("pl-stop").setExecutor(commandExecutor);
        this.getCommand("pl-start-op").setExecutor(commandExecutor);
        this.getCommand("pl-stop-op").setExecutor(commandExecutor);


        // Hawkeye
        logger = new Logger(this);

        // WebSocket
        // startNewWebSocketServer("Main", new InetSocketAddress("localhost", 8887));

        getLogger().info("PixelLOG Enabled");

    }

    @Override
    public void onDisable() {
        getLogger().info("PixelLOG Finalizing...");
        // stopNewWebSocketServer();
        getLogger().info("PixelLOG Disabled");
    }

    @Override
    public MainPlugin getMainPlugin() {
        return this;
    }


    @Override
    public HashMap<Player, NmilPlayer> getOnlinePlayerMap() {
        return onlinePlayerMap;
    }

    @Override
    public NmilPlayer getNmilPlayer(Player player) {

        return onlinePlayerMap.get(player);
    }



    @Override
    public void playerJoinHandler(Player player) {
        // if (!player.isOp()) {}
        player.getInventory().clear();
        onlinePlayerMap.put(player, new NmilPlayer(player, this));
    }

    @Override
    public void playerQuitHandler(Player player) {
        player.getInventory().clear();
        long delay = 130L;


//        onlinePlayerMap.get(player).handlePlayerUsePortKeyOrQuitMinecraft();
//        new BukkitRunnable() {
//            @Override
//            public void run() {
//                onlinePlayerMap.remove(player);
//            }
//        }.runTaskLater(Objects.requireNonNull(this), delay); // 100 ticks = 5 seconds

    }

    @Override
    public void playerRespawnHandler(Player player) {

    }

    @Override
    public void addLogToPlayer(Player player, Map<String, Object> log) {
        NmilPlayer nmilPlayer = onlinePlayerMap.get(player);
        if (nmilPlayer != null && nmilPlayer.LoggerController != null) {
            nmilPlayer.LoggerController.handleLog(log, LogType.EVENT_LOG);
        }
    }



    @Override
    public Logger getPixelLogger() {
        return logger;
    }

    @Override
    public void sendSpigotLog(String msg) {
        getLogger().info(msg);
    }

    @Override
    public void sendWebSocketMessage(String json) {
        wsServerController.broadcast(json);
        //getLogger().info("Hawkeye: " +  json);
    }




    private void startNewWebSocketServer(String usage, InetSocketAddress address) {
        wsServerController = new WebSocketServerController(usage, this, address );
        wsServerController.startServer();
    }


    private void stopNewWebSocketServer() {
        if (wsServerController != null) {
            try {
                wsServerController.stop();
            } catch (InterruptedException e) {
                this.getLogger().warning("Interrupted while stopping the WebSocket server: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }


}

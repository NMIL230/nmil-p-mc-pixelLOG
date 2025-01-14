package nmil.mceeg.plugin.player;

import nmil.mceeg.plugin.Logger.LoggerController;
import nmil.mceeg.plugin.Logger.RealTimeLoggerController;
import nmil.mceeg.plugin.MainPluginCallback;
import org.bukkit.entity.Player;

public class NmilPlayer {

    public Player player;
    public LoggerController loggerController;
    private MainPluginCallback callback;
    public RealTimeLoggerController realTimeLoggerController;

    public NmilPlayer(Player player, MainPluginCallback callback) {
        this.callback = callback;
        this.player = player;
        loggerController = new LoggerController(this,player,callback);
        realTimeLoggerController = new RealTimeLoggerController(this, player, callback);
    }



    public void handlePlayerQuitMinecraft() {
        if (loggerController != null && loggerController.active) {
            loggerController.stopCapture();
        }
    }

    public void startCollectingLogFileOnPlayer() {
        if (loggerController != null) {
            loggerController.startCapture(player);
        }
    }

    public void stopCollectingLogFileOnPlayer() {
        if (loggerController != null) {
            loggerController.stopCapture();
        }
    }

    public void startRealTimeCollectingLogsOnPlayer() {
        if (realTimeLoggerController != null) {
            realTimeLoggerController.startRealTimeCapture(player);
        }
    }

    public void stopRealTimeCollectingLogsOnPlayer() {
        if (realTimeLoggerController != null) {
            realTimeLoggerController.stopRealTimeCapture();
        }
    }



    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}


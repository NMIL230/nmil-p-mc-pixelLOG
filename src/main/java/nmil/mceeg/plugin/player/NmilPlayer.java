package nmil.mceeg.plugin.player;

import nmil.mceeg.plugin.Logger.LoggerController;
import nmil.mceeg.plugin.MainPluginCallback;
import org.bukkit.entity.Player;

public class NmilPlayer {

    public Player player;
    public LoggerController loggerController;
    private MainPluginCallback callback;


    public NmilPlayer(Player player, MainPluginCallback callback) {
        this.callback = callback;
        this.player = player;
        loggerController = new LoggerController(this,player,callback);
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



    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}


package nmil.mceeg.plugin.player;

import nmil.mceeg.plugin.Logger.LoggerController;
import nmil.mceeg.plugin.MainPluginCallback;
import org.bukkit.entity.Player;

public class NmilPlayer {

    public Player player;
    public nmil.mceeg.plugin.Logger.LoggerController LoggerController;
    private MainPluginCallback callback;


    public NmilPlayer(Player player, MainPluginCallback callback) {
        this.callback = callback;
        this.player = player;
        LoggerController = new LoggerController(this,player,callback);
    }



    public void handlePlayerQuitMinecraft() {

    }

    public void startCollectingLogFileOnPlayer() {
        if (LoggerController != null) {
            LoggerController.startCapture(player);
        }
    }

    public void stopCollectingLogFileOnPlayer() {
        if (LoggerController != null) {
            LoggerController.stopCapture(true);
        }
    }



    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}


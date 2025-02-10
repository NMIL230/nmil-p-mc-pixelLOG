package nmil.mceeg.plugin.Logger;
import nmil.mceeg.plugin.MainPluginCallback;
import nmil.mceeg.plugin.type.LogType;
import org.bukkit.entity.Player;

import java.util.Map;

import static org.bukkit.Bukkit.getServer;

public class Logger {

    private MainPluginCallback callback;
    private final ObservationSpaceGetter observationSpaceGetter;

    private int OBSERVATION_RADIUS = 3;
    private int MAX_TARGET_DISTANCE = 10;

    public Logger(MainPluginCallback callback) {
        this.callback = callback;
        observationSpaceGetter = new ObservationSpaceGetter(this);
        EventListenerCallback eventCallback = this::handleEventLog;
        PlayerEventListener playerEventListener = new PlayerEventListener(eventCallback);
        getServer().getPluginManager().registerEvents(playerEventListener, callback.getMainPlugin());
    }
    
    public Map<String, Object> getPlayerHighFreqLog(Player player) {
        return observationSpaceGetter.getPlayerObservationSpace(player, LogType.HIGH_FREQUENCY_LOG,null);
    }
    public Map<String, Object> getPlayerLowFreqLog(Player player) {
        return observationSpaceGetter.getPlayerObservationSpace(player, LogType.LOW_FREQUENCY_LOG,null);
    }
    public void handleEventLog(Player player, Map<String, Object> eventInfo, String type) {
        callback.addLogToPlayer(player, observationSpaceGetter.getPlayerObservationSpace(player, LogType.EVENT_LOG, eventInfo));
    }

    public void setOBSERVATION_RADIUS(int OBSERVATION_RADIUS) {
        this.OBSERVATION_RADIUS = OBSERVATION_RADIUS;
    }
    public void setMAX_TARGET_DISTANCE(int MAX_TARGET_DISTANCE) {
        this.MAX_TARGET_DISTANCE = MAX_TARGET_DISTANCE;
    }


    public int getOBSERVATION_RADIUS() {
        return OBSERVATION_RADIUS;
    }
    public int getMAX_TARGET_DISTANCE() {
        return MAX_TARGET_DISTANCE;
    }
}

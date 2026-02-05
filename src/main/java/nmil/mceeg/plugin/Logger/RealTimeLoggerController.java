package nmil.mceeg.plugin.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nmil.mceeg.plugin.MainPluginCallback;
import nmil.mceeg.plugin.player.PixelPlayer;
import nmil.mceeg.plugin.type.CaptureMessageType;
import nmil.mceeg.plugin.type.LogType;
import nmil.mceeg.plugin.type.MessageType;
import nmil.mceeg.plugin.util.UTCDatetime;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static nmil.mceeg.plugin.util.VersionControl.getPluginVersion;

public class RealTimeLoggerController {
    private final PixelPlayer pixelPlayer;
    private final Player player;
    private final MainPluginCallback callback;

    private final Set<BukkitRunnable> playerTasks;
    // private final List<Map<String, Object>> logCache;
    // private File logFile;

    private final Gson gson;
    private String startTime;
    private String endTime;

    private int logCount;
    public boolean active;
    private static final long LOW_UPDATE_RATE = 20L;
    private static final long HIGH_UPDATE_RATE = 20L;
    private static final long DELAY = 0L;

    private static final int CACHE_SIZE_THRESHOLD = 500;


    public RealTimeLoggerController(PixelPlayer pixelPlayer, Player player, MainPluginCallback callback) {
        this.pixelPlayer = pixelPlayer;
        this.player = player;
        this.callback = callback;
        this.playerTasks = new HashSet<>();
        // this.logCache = new ArrayList<>();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.logCount = 0;
        this.active = false;
    }

    public void handleLog(Map<String, Object> log, LogType type) {
        if (active) {
            logCount++;
            log.put("type", type);
            sendMessage(singleLogMessageWrapper(log, type));

        }
    }

    public void startRealTimeCapture(Player player) {
        active = true;
        callback.sendSpigotLog("PixelLOG: Start Running Commentary on " + player.getName());

        startTime = UTCDatetime.getUTCDatetime();
        // setupLogFile();

        BukkitRunnable highFrequencyTask = new BukkitRunnable() {
            @Override
            public void run() {
                handleLog(callback.getPixelLogger().getPlayerHighFreqLog(player), LogType.HIGH_FREQUENCY_LOG);
            }
        };

        playerTasks.add(highFrequencyTask);

        sendMessage(sessionMessageWrapper(CaptureMessageType.CAPTURE_START));


        highFrequencyTask.runTaskTimer(callback.getMainPlugin(), DELAY, HIGH_UPDATE_RATE);
    }

    public void stopRealTimeCapture() {
        active = false;
        callback.sendSpigotLog("PixelLOG: Stop Running Commentary on " + player.getName() + " saved " + logCount + " logs");

        for (BukkitRunnable task : playerTasks) {
            task.cancel();
        }
        endTime = UTCDatetime.getUTCDatetime();
        sendMessage(sessionMessageWrapper(CaptureMessageType.CAPTURE_STOP));


        // flushLogsToFile();
    }

    void sendMessage(String wrapped) {
        callback.sendWebSocketMessage(wrapped);
    }
    String singleLogMessageWrapper(Map<String, Object> log, LogType type) {
        Map<String, Object> msgWrapper = new HashMap<>();
        msgWrapper.put("title", MessageType.LOG_MESSAGE);
        msgWrapper.put("username", player.getName());
        msgWrapper.put("log", log);
        msgWrapper.put("type", type);
        Gson gson = new Gson();
        return gson.toJson(msgWrapper);
    }


    String sessionMessageWrapper(CaptureMessageType message) {
        Map<String, Object> msgWrapper = new HashMap<>();
        msgWrapper.put("title", MessageType.CAPTURE_MESSAGE);
        if (message.equals(CaptureMessageType.CAPTURE_START)) {
            msgWrapper.put("game_start_time", UTCDatetime.getUTCDatetime());
        } else {
            msgWrapper.put("game_end_time", UTCDatetime.getUTCDatetime());
        }
        msgWrapper.put("plugin_version", getPluginVersion());
        msgWrapper.put("username", player.getName());
        msgWrapper.put("message", message);
        Gson gson = new Gson();
        return gson.toJson(msgWrapper);
    }


}
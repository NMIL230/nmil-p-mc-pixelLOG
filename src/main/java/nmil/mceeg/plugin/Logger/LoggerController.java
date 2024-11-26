package nmil.mceeg.plugin.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nmil.mceeg.plugin.MainPluginCallback;
import nmil.mceeg.plugin.player.NmilPlayer;
import nmil.mceeg.plugin.type.*;
import nmil.mceeg.plugin.util.UTCDatetime;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static nmil.mceeg.plugin.util.VersionControl.getPluginVersion;

public class LoggerController {
    private final NmilPlayer nmilPlayer;
    private final Player player;
    private final MainPluginCallback callback;

    private final Set<BukkitRunnable> playerTasks;
    private final List<Map<String, Object>> logCache;
    private File logFile;

    private final Gson gson;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private int logCount;
    private boolean active;
    private static final long LOW_UPDATE_RATE = 20L;
    private static final long HIGH_UPDATE_RATE = 5L;
    private static final long DELAY = 0L;

    private static final int CACHE_SIZE_THRESHOLD = 200;

    private static final String savingDir = "spigot/pixelLogs/";


    public LoggerController(NmilPlayer nmilPlayer, Player player, MainPluginCallback callback) {
        this.nmilPlayer = nmilPlayer;
        this.player = player;
        this.callback = callback;
        this.playerTasks = new HashSet<>();
        this.logCache = new ArrayList<>();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.logCount = 0;
        this.active = false;
    }

    public void handleLog(Map<String, Object> log, LogType type) {
        if (active) {
            logCount++;
            log.put("type", type);
            logCache.add(log);

            if (logCache.size() >= CACHE_SIZE_THRESHOLD) {
                flushLogsToFile();
            }
        }
    }

    public void startCapture(Player player) {
        active = true;
        callback.sendSpigotLog("PixelLOG: Start collecting logs on " + player.getName());

        startTime = LocalDateTime.now();
        setupLogFile();

        BukkitRunnable highFrequencyTask = new BukkitRunnable() {
            @Override
            public void run() {
                handleLog(callback.getPixelLogger().getPlayerHighFreqLog(player), LogType.HIGH_FREQUENCY_LOG);
            }
        };

        playerTasks.add(highFrequencyTask);
        highFrequencyTask.runTaskTimer(callback.getMainPlugin(), DELAY, HIGH_UPDATE_RATE);

        sendLoggerMessage(CaptureMessageType.CAPTURE_START, false);
    }

    public void stopCapture(boolean gameFinished) {
        active = false;
        callback.sendSpigotLog("PixelLOG: Stop collecting logs on " + player.getName() + " saved " + logCount + " logs");

        for (BukkitRunnable task : playerTasks) {
            task.cancel();
        }

        flushLogsToFile();

        endTime = LocalDateTime.now();
        sendLoggerMessage(CaptureMessageType.CAPTURE_STOP, gameFinished);
    }

    private void flushLogsToFile() {
        if (logCache.isEmpty()) {
            return;
        }

        try (FileWriter writer = new FileWriter(logFile, true)) {
            for (Map<String, Object> log : logCache) {
                writer.write(gson.toJson(log));
                writer.write(System.lineSeparator());
            }
            logCache.clear();
        } catch (IOException e) {
            callback.sendSpigotLog("PixelLOG: Failed to write logs to file: " + e.getMessage());
        }
    }

    private void setupLogFile() {
        String logfileDirPath = "spigot/pixelLogs/" + player.getName();
        String fileName = player.getName() + "_" + UTCDatetime.getUTCDatetime() + ".json";

        File logDir = new File(logfileDirPath);
        if (!logDir.exists() && !logDir.mkdirs()) {
            callback.sendSpigotLog("PixelLOG: Failed to create log directory: " + logfileDirPath);
            return;
        }

        logFile = new File(logDir, fileName);
        try {
            if (!logFile.exists() && !logFile.createNewFile()) {
                callback.sendSpigotLog("PixelLOG: Failed to create log file: " + logFile.getAbsolutePath());
            }
        } catch (IOException e) {
            callback.sendSpigotLog("PixelLOG: Error creating log file: " + e.getMessage());
        }
    }

    private void sendLoggerMessage(CaptureMessageType message, boolean gameFinished) {
        Map<String, Object> msgWrapper = new HashMap<>();
        msgWrapper.put("title", MessageType.CAPTURE_MESSAGE);
        msgWrapper.put("username", player.getName());
        msgWrapper.put("message", message);
        msgWrapper.put("plugin_version", getPluginVersion());
        msgWrapper.put("finished", gameFinished);

        if (message == CaptureMessageType.CAPTURE_START) {
            msgWrapper.put("game_start_time", UTCDatetime.getUTCDatetime());
        } else {
            msgWrapper.put("game_end_time", UTCDatetime.getUTCDatetime());
        }

        callback.sendSpigotLog(gson.toJson(msgWrapper));
    }
}
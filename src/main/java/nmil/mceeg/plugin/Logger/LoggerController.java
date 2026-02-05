package nmil.mceeg.plugin.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nmil.mceeg.plugin.MainPluginCallback;
import nmil.mceeg.plugin.player.PixelPlayer;
import nmil.mceeg.plugin.type.*;
import nmil.mceeg.plugin.util.UTCDatetime;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static nmil.mceeg.plugin.util.VersionControl.getPluginVersion;

public class LoggerController {
    private final PixelPlayer pixelPlayer;
    private final Player player;
    private final MainPluginCallback callback;

    private final Set<BukkitRunnable> playerTasks;
    private final List<Map<String, Object>> logCache;
    private File logFile;

    private final Gson gson;
    private String startTime;
    private String endTime;

    private int logCount;
    public boolean active;
    private static final long LOW_UPDATE_RATE = 20L;
    private static final long HIGH_UPDATE_RATE = 20L;
    private static final long DELAY = 0L;

    private static final int CACHE_SIZE_THRESHOLD = 500;


    public LoggerController(PixelPlayer pixelPlayer, Player player, MainPluginCallback callback) {
        this.pixelPlayer = pixelPlayer;
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

        startTime = UTCDatetime.getUTCDatetime();
        setupLogFile();

        BukkitRunnable highFrequencyTask = new BukkitRunnable() {
            @Override
            public void run() {
                handleLog(callback.getPixelLogger().getPlayerHighFreqLog(player), LogType.HIGH_FREQUENCY_LOG);
            }
        };

        playerTasks.add(highFrequencyTask);
        highFrequencyTask.runTaskTimer(callback.getMainPlugin(), DELAY, HIGH_UPDATE_RATE);
    }

    public void stopCapture() {
        active = false;
        callback.sendSpigotLog("PixelLOG: Stop collecting logs on " + player.getName() + " saved " + logCount + " logs");

        for (BukkitRunnable task : playerTasks) {
            task.cancel();
        }
        endTime = UTCDatetime.getUTCDatetime();
        flushLogsToFile();
    }

    private void flushLogsToFile() {
        if (logCache.isEmpty()) {
            return;
        }

        try (FileWriter writer = new FileWriter(logFile, false)) {
            Map<String, Object> fileStructure = new HashMap<>();
            if (logFile.exists() && logFile.length() > 0) {
                fileStructure = gson.fromJson(new FileReader(logFile), Map.class);
            } else {
                fileStructure.put("_id", UUID.randomUUID().toString());
                fileStructure.put("filename", logFile.getName());
                fileStructure.put("username", player.getName());
                fileStructure.put("game_start_time", startTime.toString());
                fileStructure.put("game_end_time", startTime.toString());
                fileStructure.put("plugin_version", getPluginVersion());
                fileStructure.put("logs", new ArrayList<>());
            }

            List<Map<String, Object>> logs = (List<Map<String, Object>>) fileStructure.get("logs");
            logs.addAll(logCache);

            if (endTime != null) {
                fileStructure.put("game_end_time", endTime.toString());
            }

            writer.write(gson.toJson(fileStructure));
            logCache.clear();
        } catch (IOException e) {
            callback.sendSpigotLog("PixelLOG: Failed to write logs to file: " + e.getMessage());
        }
    }

    private void setupLogFile() {
        String logfileDirPath = "PixelLogs/" + player.getName();
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

}
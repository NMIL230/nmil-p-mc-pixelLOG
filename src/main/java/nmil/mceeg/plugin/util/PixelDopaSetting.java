package nmil.mceeg.plugin.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class PixelDopaSetting {
    private static final String SETTINGS_FILE_NAME = "pixeldopa-setting.json";

    public static File getSettingFile() {
        File settingsFile = new File(SETTINGS_FILE_NAME);
        if (settingsFile.exists() && settingsFile.isFile()) {
            return settingsFile;
        }
        return null;
    }

}

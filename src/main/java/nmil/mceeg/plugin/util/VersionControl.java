package nmil.mceeg.plugin.util;

import nmil.mceeg.plugin.type.VersionType;

import java.util.HashMap;
import java.util.Map;

public class VersionControl {

    public static String getPluginVersion() {
        return String.valueOf(VersionType.PluginVersion.PixelLOG_v0_0_1);
    }

}

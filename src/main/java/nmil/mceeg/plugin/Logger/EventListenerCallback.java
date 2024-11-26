package nmil.mceeg.plugin.Logger;
import org.bukkit.entity.Player;

import java.util.Map;

public interface EventListenerCallback {
    void handleEventInfo(Player player, Map<String, Object> eventInfo, String type);
}

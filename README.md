
# PixelLog

PixelLog is a research-oriented Minecraft Spigot plugin designed to capture granular player information across any game mode. It seamlessly integrates continuous state measurements (e.g., location, view) and discrete event occurrences (e.g., interactions, inventory changes), enabling detailed temporal analyses of player behaviors.

The plugin is specifically tailored for **Spigot version 1.20.4**.

> **Detailed Documentation:** For a comprehensive list of all captured data points and log structures, please refer to the [Data Collection Reference](DATA_REFERENCE.md).

## Features
- **High-Frequency State Logging**: Captures location, orientation, and status at configurable rates (e.g., 20Hz).
- **Event Tracking**: detailed logging of interactions, combat, inventory changes, and environmental manipulation.
- **Ray-Tracing**: Records precise line-of-sight interactions with blocks and entities.
- **Structured Output**: Generates clean, machine-readable JSON log files for easy analysis.

## Installation

### Prerequisites
- A Spigot Server running version **1.20.4**. (Other versions are currently not supported).
- Java Development Kit (JDK) 21 (if building from source).

### Option A: Using Pre-built Release
1. Download the latest release of PixelLog from the [Releases](https://github.com/NMIL230/nmil-mc-pixelLOG/releases) page.
2. Place the `.jar` file into the `plugins` directory of your Spigot server.

### Option B: Building from Source (Maven)
If you prefer to compile the plugin yourself:
1. Clone the repository.
2. Run the Maven package command in the root directory.
3. Locate the compiled jar file at `target/PixelLOG.jar`.
4. Move this file to your server's `plugins` directory.

### Initializing
1. Start the Spigot server:
   ```bash
   ./start.sh 
   ```

2. Verify installation by checking the server logs for the following confirmation message:
   ```text
   PixelLOG Enabled
   ```



## Usage

PixelLog provides administrative commands to control data collection sessions.

### Commands

| Command | Description | Usage |
| --- | --- | --- |
| **pl-start** | Initiates data logging for yourself. | `/pl-start` |
| **pl-stop** | Terminates data logging for yourself. | `/pl-stop` |
| **pl-start-op** | Initiates logging for a specific player (OP only). | `/pl-start-op [username]` |
| **pl-stop-op** | Terminates logging for a specific player (OP only). | `/pl-stop-op [username]` |
| **pl-version** | Displays the current plugin version. | `/pl-version` |

### Log Files

Log files are generated automatically when a logging session is stopped via command or when the player disconnects.

* **Location:** `PixelLogs/` directory within your Spigot server folder.
* **Format:** Structured JSON.

#### Simple Log Output Example

Each log file contains session metadata followed by a chronological list of states and events.

```json
{
  "logfile_id": "550e8400-e29b-41d4-a716-446655440000",
  "username": "Steve",
  "game_start_time": "2026-02-05T10:00:00Z",
  "entries": [
    {
      "type": "HIGH_FREQUENCY_LOG_20Hz",
      "time": "2026-02-05T10:00:01.050Z",
      "location": {"x": 100.5, "y": 64.0, "z": -200.5},
      "view": {"pitch": 15.0, "yaw": 90.0}
    },
    {
      "type": "EVENT_LOG",
      "event": "BlockBreakEvent",
      "event_info": {
        "block_type": "DIAMOND_ORE",
        "location": "100, 63, -200"
      }
    }
  ]
}

```

*For the full JSON schema and definition of all event fields, see [DATA_REFERENCE.md](https://www.google.com/search?q=DATA_REFERENCE.md).*

## License

This project is licensed under the MIT License. See the LICENSE file for details.

## Support

* Please use **GitHub Issues** for bug reports and feature requests.
* For security-related reports, contact: `mark.lu@wustl.edu`
* **Note:** When reporting issues, please include your Spigot version, a snippet of the server log, and a minimal reproduction case.


